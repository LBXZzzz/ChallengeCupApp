package cn.mrra.android.ble.connect

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.ScanResult
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
 * @param context 上下文
 * @param scanResult 即将要连接的设备
 * @param targetUUID 要操作的目标 [UUID] 集合
 * @param onCharacteristicRead 读特征值的监听，详见 [readCharacteristicValue]
 * @param onCharacteristicWrite 写特征值的监听，详见 [writeCharacteristicValue]
 * @param onCharacteristicChanged 监听特征值的回调，详见 [notifyCharacteristicValue]
 * */
@Suppress("MissingPermission")
class LeConnectController(
    context: Context,
    val scanResult: ScanResult,
    private val targetUUID: Set<UUIDFilter>,
    private val onCharacteristicRead: (UUID, ByteArray?) -> Unit = { _, _ -> },
    private val onCharacteristicWrite: (UUID, ByteArray?) -> Unit = { _, _ -> },
    private val onCharacteristicChanged: (UUID, ByteArray?) -> Unit = { _, _ -> }
) : BluetoothGattCallback() {

    private val _leConnectStatus = MutableStateFlow(LeConnectStatus.NOT_CONNECTED)

    /**
     * 当前的连接状态
     * */
    val leConnectStatus: StateFlow<LeConnectStatus> = _leConnectStatus.asStateFlow()

    private val device = scanResult.device!!

    private var leGatt: BluetoothGatt? = null

    private val leCharacteristicMap =
        hashMapOf<UUIDFilter, BluetoothGattCharacteristic>()

    /**
     * 连接设备，将从 [leConnectStatus] 中获得连接状态
     * */
    fun connectLeDevice(context: Context, connectFilter: (ScanResult) -> Boolean) {
        if (_leConnectStatus.value == LeConnectStatus.CONNECTED ||
            _leConnectStatus.value == LeConnectStatus.CONNECTING
        ) return
        if (connectFilter(scanResult)) {
            _leConnectStatus.value = LeConnectStatus.CONNECTING
            leGatt = device.connectGatt(context, false, this)
        } else {
            _leConnectStatus.value = LeConnectStatus.CONNECT_ERROR
        }
    }

    /**
     * 断开连接，仅连接成功时有效
     * */
    fun disconnectLeDevice() {
        leGatt?.disconnect()
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

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            leCharacteristicMap.clear()
            leGatt = gatt
            _leConnectStatus.value = LeConnectStatus.CONNECTED
        } else {
            _leConnectStatus.value = LeConnectStatus.NOT_CONNECTED
            leCharacteristicMap.clear()
            leGatt = null
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        targetUUID.map { filter ->
            val characteristic = gatt.getService(filter.serviceUUID)
                .getCharacteristic(filter.characteristicUUID)
            if (characteristic == null) {
                _leConnectStatus.value = LeConnectStatus.CHARACTERISTIC_FILTER_ERROR
                leCharacteristicMap.clear()
                return
            }
            leCharacteristicMap.put(filter, characteristic)
        }
        _leConnectStatus.value = LeConnectStatus.CHARACTERISTIC_FILTER_COMPLETED
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) = onCharacteristicRead(characteristic.uuid, characteristic.value)

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) = onCharacteristicWrite(characteristic.uuid, characteristic.value)

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) = onCharacteristicChanged(characteristic.uuid, characteristic.value)

}