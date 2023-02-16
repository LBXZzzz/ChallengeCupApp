package cn.mrra.android.ble.connect

import java.util.*

/**
 * 用于过滤 [UUID] 时使用
 *
 * @param serviceUUID 目标蓝牙服务的 [UUID]
 * @param characteristicUUID [UUID] 为 [serviceUUID] 的蓝牙服务下目标特征值的 [UUID]
 * */
data class UUIDFilter(
    val serviceUUID: UUID,
    val characteristicUUID: UUID
) {

    constructor(serviceUUID: String, characteristicUUID: String) :
            this(UUID.fromString(serviceUUID), UUID.fromString(characteristicUUID))

}