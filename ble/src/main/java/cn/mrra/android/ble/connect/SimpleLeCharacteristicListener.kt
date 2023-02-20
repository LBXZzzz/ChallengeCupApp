package cn.mrra.android.ble.connect

import android.bluetooth.BluetoothGattCharacteristic

open class SimpleLeCharacteristicListener : LeCharacteristicListener {

    override fun onCharacteristicRead(characteristic: BluetoothGattCharacteristic) {
    }

    override fun onCharacteristicWrite(characteristic: BluetoothGattCharacteristic) {
    }

    override fun onCharacteristicChanged(characteristic: BluetoothGattCharacteristic) {
    }

}