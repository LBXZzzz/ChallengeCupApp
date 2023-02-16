package cn.mrra.android.ble.scan

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.core.content.getSystemService
import androidx.lifecycle.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * 蓝牙管理者
 * */
@Suppress("MissingPermission")
class LeManager(
    context: Context,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver {

    companion object {
        const val SCAN_SUCCESS_NO_ERROR = 0
        const val SCAN_FAILED_ALREADY_STARTED = 1
        const val SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = 2
        const val SCAN_FAILED_INTERNAL_ERROR = 3
        const val SCAN_FAILED_FEATURE_UNSUPPORTED = 4
        const val SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5
        const val SCAN_FAILED_SCANNING_TOO_FREQUENTLY = 6
    }

    private val coroutineScope = MainScope()

    private val bluetoothManager = context.getSystemService<BluetoothManager>()

    private val bluetoothAdapter = bluetoothManager!!.adapter

    private val leScanner = bluetoothAdapter.bluetoothLeScanner

    private var isBackground: Boolean = true

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private var isLeScanning: Boolean = false

    private val leScanCallback = LeScanCallback()

    private val _leScanResult = MutableSharedFlow<LeScanResult>(
        0, 0,
        BufferOverflow.SUSPEND
    )

    /**
     * 在这个 [SharedFlow] 中返回扫描结果
     *
     * @see [LeScanResult]
     * */
    val leScanResult: SharedFlow<LeScanResult> = _leScanResult.asSharedFlow()

    /**
     * 判断蓝牙是否已打开以及控制蓝牙开启和关闭
     * */
    var isBluetoothEnabled: Boolean
        set(value) {
            if (value && !bluetoothAdapter.isEnabled) bluetoothAdapter.enable()
            else if (!value && bluetoothAdapter.isEnabled) bluetoothAdapter.disable()
        }
        get() = bluetoothAdapter.isEnabled

    /**
     * 开始扫描
     *
     * @see [stopLeScan]
     * */
    fun startLeScan() {
        if (!isLeScanning && !isBackground) {
            leScanner.startScan(
                null,
                ScanSettings.Builder().build(),
                leScanCallback
            )
            isLeScanning = true
        }
    }

    /**
     * 结束扫描
     *
     * @see [startLeScan]
     * */
    fun stopLeScan() {
        if (isLeScanning) {
            leScanner.stopScan(leScanCallback)
            isLeScanning = false
        }
    }

    override fun onCreate(owner: LifecycleOwner){
        isBackground = false
    }

    override fun onStart(owner: LifecycleOwner){
        isBackground = false
    }

    override fun onResume(owner: LifecycleOwner) {
        isBackground = false
        if (isLeScanning) startLeScan()
    }

    override fun onPause(owner: LifecycleOwner) {
        isBackground = true
        if (isLeScanning) stopLeScan()
    }

    override fun onStop(owner: LifecycleOwner){
        isBackground = true
    }

    override fun onDestroy(owner: LifecycleOwner) {
        coroutineScope.cancel()
        stopLeScan()
    }

    internal inner class LeScanCallback internal constructor() : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            coroutineScope.launch {
                _leScanResult.emit(LeScanResult.Success(result))
            }
        }

        override fun onScanFailed(errorCode: Int) {
            coroutineScope.launch {
                _leScanResult.emit(LeScanResult.Failure(errorCode))
            }
        }
    }

}