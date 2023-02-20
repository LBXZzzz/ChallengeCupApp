package cn.mrra.android.ui.fragment.mrra

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.SystemClock
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import cn.mrra.android.R
import cn.mrra.android.ble.connect.LeController
import cn.mrra.android.ble.connect.LeConnectStatus
import cn.mrra.android.ble.connect.UUIDFilter
import cn.mrra.android.common.base.BaseViewModel
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.toastMsg
import cn.mrra.android.entity.ControlMode
import cn.mrra.android.entity.MemoryRecord
import cn.mrra.android.storage.room.MemoryRecordDatabaseDao
import cn.mrra.android.ui.fragment.control.DataFrame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.max

private const val TARGET_BLE_DEVICE_NAME = "MRRA"
private const val TARGET_SERVICE_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val WRITE_TARGET_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
private const val NOTIFY_TARGET_UUID = "0000ffe2-0000-1000-8000-00805f9b34fb"

private val writeUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, WRITE_TARGET_UUID)
private val notifyUUIDFilter = UUIDFilter(TARGET_SERVICE_UUID, NOTIFY_TARGET_UUID)

private const val HANDLER_MSG_REAPPEARANCE = 1
private const val HANDLER_MSG_WRITE_DATA_FRAME = 2
private const val HANDLER_MSG_REAPPEARANCE_DISPATCH = 3

