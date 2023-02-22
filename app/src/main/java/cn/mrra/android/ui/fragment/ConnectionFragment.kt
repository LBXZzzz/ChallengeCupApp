package cn.mrra.android.ui.fragment

import android.bluetooth.BluetoothDevice
import cn.mrra.android.common.startActivity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.mrra.android.R
import cn.mrra.android.ble.scan.LeManager
import cn.mrra.android.ble.scan.LeScanResult
import cn.mrra.android.ble.status.LeStatusReceiver
import cn.mrra.android.ble.status.LeStatusReceiver.Companion.registerBluetoothStatusReceiver
import cn.mrra.android.common.base.ACTION_NAVIGATE
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.FragmentConnectionBinding
import cn.mrra.android.databinding.ItemConnectionListBinding
import cn.mrra.android.mrra.MRRA
import cn.mrra.android.ui.activity.MRRAActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ConnectionFragment : SimpleFragment<FragmentConnectionBinding>() {

    override val layoutId: Int = R.layout.fragment_connection

    private lateinit var leManager: LeManager

    private lateinit var leStatusReceiver: LeStatusReceiver

    private lateinit var adapter: ConnectionListAdapter

    @Suppress("SetTextI18n")
    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        leManager = LeManager(requireContext(), viewLifecycleOwner)
        with(binding) {
            swConnectionSwitch.isEnabled = false
            swConnectionSwitch.setOnCheckedChangeListener { _, isChecked ->
                swConnectionSwitch.isEnabled = false
                tvConnectionStatus.text =
                    if (isChecked) "${getString(R.string.bluetooth)} : ${getString(R.string.status_turning_on)}"
                    else "${getString(R.string.bluetooth)} : ${getString(R.string.status_turning_off)}"
                leManager.isBluetoothEnabled = isChecked
            }
            leStatusReceiver = requireContext().registerBluetoothStatusReceiver(
                leManager.isBluetoothEnabled
            ) {
                turningOn {
                    swConnectionSwitch.run {
                        isChecked = true
                        isEnabled = false
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_turning_on)}"
                }
                on {
                    clearResult()
                    startLeScan()
                    swConnectionSwitch.run {
                        isChecked = true
                        isEnabled = true
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_on)}"
                }
                turningOff {
                    clearResult()
                    swConnectionSwitch.run {
                        isChecked = false
                        isEnabled = false
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_turning_off)}"
                }
                off {
                    clearResult()
                    swConnectionSwitch.run {
                        isChecked = false
                        isEnabled = true
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_off)}"
                }
                error {
                    clearResult()
                    swConnectionSwitch.run {
                        isChecked = true
                        isEnabled = true
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_error)}"
                }
            }
            rvConnectionList.layoutManager = LinearLayoutManager(requireContext())
                .apply { orientation = LinearLayoutManager.VERTICAL }
            adapter = ConnectionListAdapter(
                leManager.leScanResult,
                {
                    startActivity<MRRAActivity> {
                        action = ACTION_NAVIGATE
                        putExtra("id", R.id.mrra_mrra)
                    }
                    MRRA.INSTANCE.controller.connectLeDevice(it)
                },
                lifecycleScope
            )
            rvConnectionList.adapter = adapter
            srfConnectRefresh.setOnRefreshListener {
                startLeScan()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (leManager.isBluetoothEnabled) {
            toastMsg(getString(R.string.scanning), requireContext(), Toast.LENGTH_SHORT)
            startLeScan()
        }
    }

    override fun onPause() {
        super.onPause()
        if (leManager.isBluetoothEnabled) {
            toastMsg(getString(R.string.stop_scanning), requireContext(), Toast.LENGTH_SHORT)
            leManager.stopLeScan()
        }
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(leStatusReceiver)
        super.onDestroyView()
    }

    private fun clearResult() {
        if (::adapter.isInitialized) {
            adapter.clearResult()
        }
    }

    private fun startLeScan() {
        binding.srfConnectRefresh.isRefreshing = true
        lifecycleScope.launch {
            delay(500L)
            leManager.startLeScan()
            binding.srfConnectRefresh.isRefreshing = false
        }
    }

    @Suppress("MissingPermission")
    class ConnectionListAdapter(
        private val resultFlow: SharedFlow<LeScanResult>,
        private val onClick: (BluetoothDevice) -> Unit,
        coroutineScope: CoroutineScope
    ) : RecyclerView.Adapter<ConnectionListAdapter.ConnectionViewHolder>() {

        private val addressSet = hashSetOf<String>()
        private val resultList = mutableListOf<BluetoothDevice>()

        init {
            MRRA.INSTANCE.controller.leDevice.value?.let {
                addressSet.add(it.address)
                resultList.add(it)
                notifyItemInserted(0)
            }
            coroutineScope.launch {
                resultFlow.collect {
                    if (it is LeScanResult.Failure) {
                        toastMsg("${it.state}")
                    } else {
                        val device = it.scanResult.device
                        // 筛选未配对，类型为 BLE 的蓝牙设备
                        if (device.bondState == BluetoothDevice.BOND_NONE
                            && device.type == BluetoothDevice.DEVICE_TYPE_LE
                            && addressSet.add(device.address)
                        ) {
                            resultList.add(device)
                            notifyItemInserted(resultList.size - 1)
                        }
                    }
                }
            }
            coroutineScope.launch {
                MRRA.INSTANCE.controller.leDevice.collect {
                    if (it != null && addressSet.contains(it.address)) {
                        notifyItemChanged(resultList.indexOf(it))
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
                holder.text = device.alias ?: device.name ?: device.address
            } else {
                holder.text = device.name ?: device.address
            }
            holder.binding.ivConnectConnect.visibility =
                if (MRRA.INSTANCE.controller.leDevice.value == device) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            holder.binding.tvConnectItem.setOnClickListener {
                onClick(device)
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
            val binding = ItemConnectionListBinding.bind(itemView)

            var text: CharSequence
                get() = binding.tvConnectItem.text
                set(value) {
                    binding.tvConnectItem.text = value
                }
        }

    }

}