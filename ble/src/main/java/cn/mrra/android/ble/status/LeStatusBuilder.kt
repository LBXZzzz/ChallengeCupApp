package cn.mrra.android.ble.status

class StatusCallbackBuilder {

    internal var turningOn: () -> Unit = {}
    internal var on: () -> Unit = {}
    internal var turningOff: () -> Unit = {}
    internal var off: () -> Unit = {}
    internal var error: () -> Unit = {}

    fun turningOn(operation: () -> Unit) {
        turningOn = operation
    }

    fun on(operation: () -> Unit) {
        on = operation
    }

    fun turningOff(operation: () -> Unit) {
        turningOff = operation
    }

    fun off(operation: () -> Unit) {
        off = operation
    }

    fun error(operation: () -> Unit) {
        error = operation
    }

}
