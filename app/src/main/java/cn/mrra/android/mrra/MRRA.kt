package cn.mrra.android.mrra

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.SystemClock
import cn.mrra.android.R
import cn.mrra.android.ble.connect.LeConnectStatus
import cn.mrra.android.ble.connect.LeController
import cn.mrra.android.ble.connect.SimpleLeCharacteristicListener
import cn.mrra.android.ble.connect.UUIDFilter
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.toastMsg
import cn.mrra.android.entity.ConnectStatus
import cn.mrra.android.entity.ControlMode
import cn.mrra.android.entity.MemoryRecord
import cn.mrra.android.ui.fragment.control.DataFrame
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

private const val TARGET_SERVICE_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val WRITE_TARGET_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val NOTIFY_TARGET_UUID = "0000ffe2-0000-1000-8000-00805f9b34fb"

private val writeUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, WRITE_TARGET_UUID)
private val notifyUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, NOTIFY_TARGET_UUID)

private const val HANDLER_MSG_REAPPEARANCE = 1
private const val HANDLER_MSG_WRITE_DATA_FRAME = 2
private const val HANDLER_MSG_DISPATCH_REAPPEARANCE = 3

class MRRA private constructor(context: Context) {

    companion object {
        lateinit var INSTANCE: MRRA
            private set

        fun init(context: Context) {
            INSTANCE = MRRA(context)
        }
    }

    val controller = LeController(context, setOf(writeUUIDFilter, notifyUUIDFilter))

    private val _mode = MutableStateFlow(ControlMode.NOT_CONNECTED)

    val mode = _mode.asStateFlow()

    private val _status = MutableStateFlow(ConnectStatus.NOT_CONNECTED)

    val status = _status.asStateFlow()

    private val _dataFrame = MutableSharedFlow<DataFrame>(
        0, 0, BufferOverflow.SUSPEND
    )

    val dataFrame = _dataFrame.asSharedFlow()

    private val mrraThread = HandlerThread("LeController").apply { start() }
    private val mrraHandler = object : Handler(mrraThread.looper) {
        @Suppress("UNCHECKED_CAST")
        override fun handleMessage(msg: Message) {
            runCatching {
                when (msg.arg1) {
                    HANDLER_MSG_REAPPEARANCE -> {
                        reappearance(msg.obj as List<MemoryRecord>)
                    }
                    HANDLER_MSG_WRITE_DATA_FRAME -> {
                        writeDataFrame(msg.obj as DataFrame)
                    }
                    HANDLER_MSG_DISPATCH_REAPPEARANCE -> {
                        dispatchReappearance(msg.obj as MemoryRecord?)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun Handler.reappearance(records: List<MemoryRecord>) {
        _mode.value = ControlMode.MEMORY_REAPPEARANCE
        // 延迟 1 秒开始
        val uptime = SystemClock.uptimeMillis() + 1000
        var lastTime = uptime
        for (record in records) {
            lastTime = max(lastTime, uptime + record.delay)
            sendMessageAtTime(Message.obtain().apply {
                arg1 = HANDLER_MSG_DISPATCH_REAPPEARANCE
                obj = record
            }, uptime + record.delay)
        }
        sendEmptyMessageAtTime(
            HANDLER_MSG_DISPATCH_REAPPEARANCE,
            lastTime + 2000L
        )
    }

    private fun Handler.writeDataFrame(dataFrame: DataFrame) {
        val res: Boolean
        if (_mode.value == ControlMode.STOP) {
            _mode.value = dataFrame.controlMode
            res = controller.writeCharacteristicValue(
                writeUUIDFilter,
                dataFrame.values
            )
        } else {
            _mode.value = ControlMode.STOP
            removeMessages(HANDLER_MSG_REAPPEARANCE)
            removeMessages(HANDLER_MSG_DISPATCH_REAPPEARANCE)
            res = controller.writeCharacteristicValue(
                writeUUIDFilter,
                DataFrame.SimpleDataFrameBuilder.buildStop().values
            )
            if (!dataFrame.isStop) {
                sendMessage(Message.obtain().apply {
                    arg1 = HANDLER_MSG_WRITE_DATA_FRAME
                    obj = dataFrame
                })
            }
        }
        if (res) {
            toastMsg(appContext.getString(R.string.mrra_control_write_success))
        } else {
            toastMsg(appContext.getString(R.string.mrra_control_write_fail))
        }
    }

    private fun dispatchReappearance(record: MemoryRecord?) {
        controller.writeCharacteristicValue(
            writeUUIDFilter,
            if (record != null) {
                DataFrame.SimpleDataFrameBuilder.buildMemory(record).values
            } else {
                _mode.value = ControlMode.STOP
                DataFrame.SimpleDataFrameBuilder.buildStop().values
            }
        )
    }

    fun writeInitiative() {
        mrraHandler.sendMessage(Message.obtain().apply {
            arg1 = HANDLER_MSG_WRITE_DATA_FRAME
            obj = DataFrame.SimpleDataFrameBuilder.buildInitiative()
        })
    }

    fun writePassive(passiveMode: Int) {
        mrraHandler.sendMessage(Message.obtain().apply {
            arg1 = HANDLER_MSG_WRITE_DATA_FRAME
            obj = DataFrame.SimpleDataFrameBuilder.buildPassive(passiveMode)
        })
    }

    fun writeMemory(records: List<MemoryRecord>) {
        mrraHandler.sendMessage(Message.obtain().apply {
            arg1 = HANDLER_MSG_REAPPEARANCE
            obj = records
        })
    }

    fun writeStop() {
        mrraHandler.sendMessage(Message.obtain().apply {
            arg1 = HANDLER_MSG_WRITE_DATA_FRAME
            obj = DataFrame.SimpleDataFrameBuilder.buildStop()
        })
    }

    init {
        @OptIn(DelicateCoroutinesApi::class)
        // 全局就可以了，因为这个类本来就是跟随进程的生命周期一同运行的
        GlobalScope.launch(Dispatchers.Main) {
            controller.leConnectStatus.collect {
                _status.value = when (it) {
                    LeConnectStatus.NOT_CONNECTED -> {
                        ConnectStatus.NOT_CONNECTED
                    }
                    LeConnectStatus.CONNECTING -> {
                        ConnectStatus.CONNECTING
                    }
                    LeConnectStatus.CONNECTED -> {
                        controller.discoverService()
                        ConnectStatus.VERIFYING
                    }
                    LeConnectStatus.SUCCESS -> {
                        ConnectStatus.SUCCESS
                    }
                    LeConnectStatus.CONNECT_ERROR -> {
                        ConnectStatus.CONNECT_ERROR
                    }
                    LeConnectStatus.OPERATION_ERROR,
                    LeConnectStatus.CHARACTERISTIC_FILTER_ERROR -> {
                        ConnectStatus.VERIFICATION_FAILED
                    }
                }
            }
        }

        controller.addLeCharacteristicListener(
            object : SimpleLeCharacteristicListener() {
                private var errorCount = 0

                override fun onCharacteristicChanged(
                    characteristic: BluetoothGattCharacteristic
                ) {
                    runCatching {
                        @OptIn(DelicateCoroutinesApi::class)
                        GlobalScope.launch {
                            _dataFrame.emit(DataFrame(characteristic.value))
                            errorCount = 0
                        }
                    }.onFailure {
                        errorCount++
                        if (errorCount > 5) {
                            throw IllegalArgumentException("MRRA Data Formatter Error.")
                        }
                    }
                }
            })
    }

}