package cn.mrra.android.ble.connect

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/**
 * 蓝牙的连接控制类，这样封装的原因/目的是：不对外暴露 [BluetoothGatt] 和
 * [BluetoothGattCharacteristic] ，而仅仅通过目标通道的 [UUID]
 * 来对对应特征值来进行读或写或监听操作
 * <p/>
 *
 * 不对外暴露太多东西，可以降低外部的代码复杂度，但是功能性降低，
 * 不过目前该项目只需要读写和监听特征值，所以这样封装是没有问题的
 * <p/>
 *
 * 请注意：并不是所有的通道都可以用于读写和监听的，一般一个蓝牙通道只支持
 * （读取）（写入）或（监听） 三个中的一种操作，详细参数在蓝牙模块的说明书上应有说明，
 * 具体支持哪种操作应重点关注蓝牙通道的操作符，解释起来比较复杂
 *
 * @param targetUUID 要操作的目标 [UUID] 集合，必须全部匹配才会扫描成功，否则认为扫描失败
 * */
@Suppress("MissingPermission", "StaticFieldLeak")
class LeController constructor(
    private val context: Context,
    private val targetUUID: Set<UUIDFilter>
) : BluetoothGattCallback() {

    private val _leConnectStatus = MutableStateFlow(LeConnectStatus.NOT_CONNECTED)

    /**
     * 当前的连接状态
     *
     * @see LeConnectStatus
     * */
    val leConnectStatus: StateFlow<LeConnectStatus> = _leConnectStatus.asStateFlow()

    private val _leDevice = MutableStateFlow<BluetoothDevice?>(null)

    /**
     * 当前选中的蓝牙设备
     * */
    val leDevice = _leDevice.asStateFlow()

    private var leGatt: BluetoothGatt? = null

    private val leCharacteristicMap = hashMapOf<UUIDFilter, BluetoothGattCharacteristic>()

    private val leListeners = hashSetOf<LeCharacteristicListener>()

    /**
     * 连接设备，将从 [leConnectStatus] 中获得连接状态
     * */
    fun connectLeDevice(
        device: BluetoothDevice,
        connectFilter: (BluetoothDevice) -> Boolean = { true }
    ) {
        if (_leConnectStatus.value == LeConnectStatus.CONNECTED ||
            _leConnectStatus.value == LeConnectStatus.CONNECTING
        ) {
            disconnectLeDevice()
        }
        leGatt = null
        _leDevice.value = device
        if (connectFilter(device)) {
            _leConnectStatus.value = LeConnectStatus.CONNECTING
            leGatt = device.connectGatt(context, false, this)
            if (leGatt == null) {
                _leConnectStatus.value = LeConnectStatus.CONNECT_ERROR
            }
        } else {
            _leConnectStatus.value = LeConnectStatus.CONNECT_ERROR
        }
    }

    /**
     * 断开连接，仅连接成功时有效
     * */
    fun disconnectLeDevice() {
        leGatt?.disconnect()
        leGatt = null
        _leDevice.value = null
    }

    /**
     * 扫描服务，应在连接成功后执行，否则总是返回 false
     *
     * @return 如果成功扫描，返回 true
     * */
    fun discoverService(): Boolean {
        return leGatt?.discoverServices() ?: false
    }

    /**
     * 读取指定通道的特征值，读取成功后，将在 [onCharacteristicRead] 中获得回调
     * <p/>
     *
     * 应在连接成功后执行，否则总是返回 false
     *
     * @param uuidFilter 目标 [UUID] ，此 [UUID] 应被包含于 [uuidFilter] 中
     * @return 如果成功读取，返回 true
     * */
    fun readCharacteristicValue(uuidFilter: UUIDFilter): Boolean {
        val characteristic = leCharacteristicMap[uuidFilter] ?: return false
        val gatt = leGatt ?: return false
        return gatt.readCharacteristic(characteristic)
    }

    /**
     * 对指定通道写入特征值，写入成功后，将在 [onCharacteristicWrite] 中获得回调
     * <p/>
     *
     * 应在连接成功后执行，否则总是返回 false
     *
     * @param uuidFilter 目标 [UUID] ，此 [UUID] 应被包含于 [uuidFilter] 中
     * @return 如果成功写入，返回 true
     * */
    fun writeCharacteristicValue(uuidFilter: UUIDFilter, values: ByteArray): Boolean {
        val characteristic = leCharacteristicMap[uuidFilter] ?: return false
        val gatt = leGatt ?: return false
        characteristic.value = values
        return gatt.writeCharacteristic(characteristic)
    }

    /**
     * 监听（或取消监听）指定通道的特征值，写入成功后，将在 [onCharacteristicChanged] 中获得回调
     * <p/>
     *
     * 应在连接成功后执行，否则总是返回 false
     *
     * @param uuidFilter 目标 [UUID] ，此 [UUID] 应被包含于 [uuidFilter] 中
     * @param enable true 代表监听，false 代表取消监听，默认值为 true
     * @return 如果成功监听，返回 true
     * */
    fun notifyCharacteristicValue(uuidFilter: UUIDFilter, enable: Boolean = true): Boolean {
        val characteristic = leCharacteristicMap[uuidFilter] ?: return false
        val gatt = leGatt ?: return false
        return gatt.setCharacteristicNotification(characteristic, enable)
    }

    /**
     * 增加特征值操作监听
     * */
    fun addLeCharacteristicListener(listener: LeCharacteristicListener) {
        leListeners.add(listener)
    }

    /**
     * 移除特征值操作监听
     *
     * @return 指定的监听原本是否存在于监听列表中
     * */
    fun removeLeCharacteristicListener(listener: LeCharacteristicListener): Boolean {
        return leListeners.remove(listener)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        leCharacteristicMap.clear()
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            leGatt = gatt
            _leConnectStatus.value = LeConnectStatus.CONNECTED
        } else {
            _leConnectStatus.value = LeConnectStatus.NOT_CONNECTED
            leGatt = null
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        leCharacteristicMap.clear()
        targetUUID.map { filter ->
            val characteristic = gatt.getService(
                filter.serviceUUID
            )?.getCharacteristic(filter.characteristicUUID)
            if (characteristic == null) {
                leCharacteristicMap.clear()
                _leConnectStatus.value = LeConnectStatus.CHARACTERISTIC_FILTER_ERROR
                return
            }
            leCharacteristicMap[filter] = characteristic
        }
        _leConnectStatus.value = LeConnectStatus.SUCCESS
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        runCatching {
            leListeners.forEach {
                it.onCharacteristicRead(characteristic)
            }
        }.onFailure {
            _leConnectStatus.value = LeConnectStatus.OPERATION_ERROR
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        runCatching {
            leListeners.forEach {
                it.onCharacteristicWrite(characteristic)
            }
        }.onFailure {
            _leConnectStatus.value = LeConnectStatus.OPERATION_ERROR
        }
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        runCatching {
            leListeners.forEach {
                it.onCharacteristicChanged(characteristic)
            }
        }.onFailure {
            _leConnectStatus.value = LeConnectStatus.OPERATION_ERROR
        }
    }

}