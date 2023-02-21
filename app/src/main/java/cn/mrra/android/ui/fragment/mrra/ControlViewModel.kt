package cn.mrra.android.ui.fragment.mrra

import cn.mrra.android.ble.connect.UUIDFilter

private const val TARGET_BLE_DEVICE_NAME = "MRRA"
private const val TARGET_SERVICE_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val WRITE_TARGET_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val NOTIFY_TARGET_UUID = "0000ffe2-0000-1000-8000-00805f9b34fb"

private val writeUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, WRITE_TARGET_UUID)
private val notifyUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, NOTIFY_TARGET_UUID)

private const val HANDLER_MSG_REAPPEARANCE = 1
private const val HANDLER_MSG_WRITE_DATA_FRAME = 2
private const val HANDLER_MSG_REAPPEARANCE_DISPATCH = 3

