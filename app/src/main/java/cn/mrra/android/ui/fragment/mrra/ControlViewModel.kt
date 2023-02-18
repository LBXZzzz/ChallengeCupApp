package cn.mrra.android.ui.fragment.mrra

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import cn.mrra.android.R
import cn.mrra.android.ble.connect.LeConnectController
import cn.mrra.android.ble.connect.LeConnectStatus
import cn.mrra.android.ble.connect.UUIDFilter
import cn.mrra.android.common.base.BaseViewModel
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.toastMsg
import cn.mrra.android.entity.ControlMode
import cn.mrra.android.entity.MemoryRecord
import cn.mrra.android.ui.fragment.control.DataFrame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TARGET_BLE_DEVICE_NAME = "MRRA"
private const val TARGET_SERVICE_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val WRITE_TARGET_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val NOTIFY_TARGET_UUID = "0000ffe2-0000-1000-8000-00805f9b34fb"

private val writeUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, WRITE_TARGET_UUID)
private val notifyUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, NOTIFY_TARGET_UUID)

@Suppress("MissingPermission")
class ControlViewModel : BaseViewModel() {

    private var errorCount = 0

    private val leController = LeConnectController(
        setOf(writeUUIDFilter, notifyUUIDFilter),
        onCharacteristicChanged = {
            if (connectStatus.value == LeConnectStatus.CHARACTERISTIC_FILTER_SUCCESS) {
                runCatching {
                    _dataFrame.value = DataFrame(it.value)
                }.onFailure {
                    errorCount++
                    if (errorCount >= 10) disconnect()
                }
            }
        }
    )

    val connectStatus: StateFlow<LeConnectStatus> = leController.leConnectStatus

    private val _dataFrame = MutableStateFlow<DataFrame?>(null)
    val dataFrame = _dataFrame.asStateFlow()

    private val _mode = MutableStateFlow(ControlMode.NOT_CONNECTED)
    val mode = _mode.asStateFlow()

    init {
        viewModelScope.launch {
            targetLeDevice.collect {
                it?.let { device ->
                    leController.disconnectLeDevice()
                    leController.connectLeDevice(appContext, device) { d ->
                        d.name == TARGET_BLE_DEVICE_NAME
                    }
                }
            }
        }
        viewModelScope.launch {
            connectStatus.collect {
                if (it == LeConnectStatus.CONNECTED) {
                    leController.discoverService()
                }
            }
        }
    }

    fun disconnect() {
        leController.disconnectLeDevice()
    }

    fun write(dataFrame: DataFrame): Boolean {
        if (dataFrame.isStop || _mode.value == ControlMode.STOP) {
            if (dataFrame.isStop) stopReappearance()
            if (leController.writeCharacteristicValue(writeUUIDFilter, dataFrame.values)) {
//                _mode.value = mode
                return true
            }
        }
        toastMsg(appContext.getString(R.string.mrra_control_please_stop))
        return false
    }

    private val mainHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.arg1 == 1) {
                val record = msg.obj as MemoryRecord

            }
        }
    }

    fun writeReappearance(records: List<MemoryRecord>) {
        val uptime = SystemClock.uptimeMillis() + 1000
        for (record in records) {
            mainHandler.sendMessageAtTime(Message.obtain().apply {
                arg1 = 1
                obj = record
            }, uptime + record.delay)
        }
    }

    fun stopReappearance() {
        mainHandler.removeMessages(1)
    }

}