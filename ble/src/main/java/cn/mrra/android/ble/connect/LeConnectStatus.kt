package cn.mrra.android.ble.connect

enum class LeConnectStatus {

    /**
     * 未连接
     * */
    NOT_CONNECTED,

    /**
     * 正在连接
     * */
    CONNECTING,

    /**
     * 已连接
     * */
    CONNECTED,

    /**
     * 特征值过滤错误
     * */
    CHARACTERISTIC_FILTER_ERROR,

    /**
     * 特征值过滤完成
     * */
    CHARACTERISTIC_FILTER_SUCCESS,

    /**
     * 连接错误
     * */
    CONNECT_ERROR

}