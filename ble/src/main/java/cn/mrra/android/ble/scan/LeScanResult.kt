package cn.mrra.android.ble.scan

import android.bluetooth.le.ScanResult

sealed class LeScanResult(
    /**
     * 状态码
     *
     * @see LeManager.SCAN_SUCCESS_NO_ERROR
     * @see LeManager.SCAN_FAILED_ALREADY_STARTED = 1
     * @see LeManager.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED
     * @see LeManager.SCAN_FAILED_INTERNAL_ERROR = 3
     * @see LeManager.SCAN_FAILED_FEATURE_UNSUPPORTED = 4
     * @see LeManager.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5
     * @see LeManager.SCAN_FAILED_SCANNING_TOO_FREQUENTLY = 6
     * */
    val state: Int,
    /**
     * 扫描结果，若扫描失败，则为空
     * */
    private val _scanResult: ScanResult?
) {

    /**
     * 扫描结果，若扫描失败，则为空
     * */
    val scanResult: ScanResult get() = _scanResult!!

    /**
     * 扫描成功时返回扫描的结果，此时返回的错误代码为 [LeManager.SCAN_SUCCESS_NO_ERROR]
     *
     * @param scanResult 扫描到的蓝牙低功耗设备
     * */
    class Success(scanResult: ScanResult) : LeScanResult(
        LeManager.SCAN_SUCCESS_NO_ERROR,
        scanResult
    )

    /**
     * 扫描失败时返回此类型，此时返回的蓝牙低功耗设备扫描结果为空
     *
     * @param errorCode 错误代码，详见 [LeManager.Companion]
     * */
    class Failure(errorCode: Int) : LeScanResult(
        errorCode,
        null
    )

}
