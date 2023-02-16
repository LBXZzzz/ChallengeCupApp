package cn.mrra.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.mrra.android.R
import cn.mrra.android.ble.scan.LeManager
import cn.mrra.android.ble.status.LeStatusReceiver
import cn.mrra.android.ble.status.LeStatusReceiver.Companion.registerBluetoothStatusReceiver
import cn.mrra.android.common.base.BaseFragment
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.FragmentConnectionBinding
import cn.mrra.android.ui.adapter.ConnectionListAdapter

class ConnectionFragment : BaseFragment<FragmentConnectionBinding>() {

    override var _binding: FragmentConnectionBinding? = null

    private lateinit var leManager: LeManager

    private lateinit var leStatusReceiver: LeStatusReceiver

    @Suppress("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
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
                    swConnectionSwitch.run {
                        isChecked = true
                        isEnabled = true
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_on)}"
                }
                turningOff {
                    swConnectionSwitch.run {
                        isChecked = false
                        isEnabled = false
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_turning_off)}"
                }
                off {
                    swConnectionSwitch.run {
                        isChecked = false
                        isEnabled = true
                    }
                    tvConnectionStatus.text =
                        "${getString(R.string.bluetooth)} : ${getString(R.string.status_off)}"
                }
                error {
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
            val adapter = ConnectionListAdapter(
                leManager.leScanResult,
                lifecycleScope
            )
            rvConnectionList.adapter = adapter
            srfConnectRefresh.setOnRefreshListener {
                adapter.clearResult()
                srfConnectRefresh.isRefreshing = false
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (leManager.isBluetoothEnabled) {
            toastMsg(getString(R.string.scanning), requireContext(), Toast.LENGTH_SHORT)
            leManager.startLeScan()
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

}