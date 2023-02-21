package cn.mrra.android.ble.status

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class LeStatusReceiver private constructor(
    initialStatus: Boolean,
    private val builder: LeStatusCallbackBuilder
) : BroadcastReceiver() {

    companion object {
        fun Context.registerBluetoothStatusReceiver(
            initialStatus: Boolean,
            builder: LeStatusCallbackBuilder.() -> Unit
        ) = LeStatusReceiver(
            initialStatus,
            LeStatusCallbackBuilder().apply(builder)
        ).also {
            registerReceiver(it, IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            })
        }
    }

    init {
        if (initialStatus) builder.on() else builder.off()
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        builder.turningOn()
                    }
                    BluetoothAdapter.STATE_ON -> {
                        builder.on()
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        builder.turningOff()
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        builder.off()
                    }
                    else -> {
                        builder.error()
                    }
                }
            }
        }
    }
}