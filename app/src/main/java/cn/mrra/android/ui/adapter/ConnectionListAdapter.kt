package cn.mrra.android.ui.adapter

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.mrra.android.R
import cn.mrra.android.ble.scan.LeScanResult
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.ItemConnectionListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Suppress("MissingPermission")
class ConnectionListAdapter(
    private val resultFlow: SharedFlow<LeScanResult>,
    coroutineScope: CoroutineScope
) : RecyclerView.Adapter<ConnectionListAdapter.ConnectionViewHolder>() {

    private val addressSet = hashSetOf<String>()
    private val resultList = mutableListOf<BluetoothDevice>()

    init {
        coroutineScope.launch {
            resultFlow.collect {
                if (it is LeScanResult.Failure) {
                    toastMsg("${it.state}")
                } else {
                    val device = it.scanResult.device
                    // 筛选未配对，类型不为未知的蓝牙设备
                    if (device.bondState == BluetoothDevice.BOND_NONE
                        && device.type != BluetoothDevice.DEVICE_TYPE_UNKNOWN
                        && addressSet.add(device.address)
                    ) {
                        resultList.add(device)
                        notifyItemInserted(resultList.size - 1)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        return ConnectionViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        val device = resultList[position]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            holder.text(device.alias ?: device.name ?: device.address)
        } else {
            holder.text(device.name ?: device.address)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun clearResult() {
        val size = resultList.size
        addressSet.clear()
        resultList.clear()
        notifyItemRangeRemoved(0, size)
    }

    class ConnectionViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_connection_list,
                parent,
                false
            )
    ) {
        private var binding = ItemConnectionListBinding.bind(itemView)

        fun text(text: String) {
            binding.tvConnectItem.text = text
        }
    }

}