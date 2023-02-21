package cn.mrra.android.ble.connect

import android.bluetooth.BluetoothGattCharacteristic

interface LeCharacteristicListener {

    /**
     * 读特征值的监听
     * */
    fun onCharacteristicRead(characteristic: BluetoothGattCharacteristic)

    /**
     * 写特征值的监听
     * */
    fun onCharacteristicWrite(characteristic: BluetoothGattCharacteristic)

    /**
     * 监听特征值的监听
     * */
    fun onCharacteristicChanged(characteristic: BluetoothGattCharacteristic)

}