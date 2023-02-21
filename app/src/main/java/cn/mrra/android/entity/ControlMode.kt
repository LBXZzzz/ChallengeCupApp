package cn.mrra.android.entity

import androidx.annotation.StringRes
import cn.mrra.android.R

enum class ControlMode(@StringRes val label: Int) {
    NOT_CONNECTED(R.string.mrra_control_not_connected),
    CONNECTING(R.string.mrra_control_connecting),
    ERROR(R.string.mrra_control_connect_error),

    INITIATIVE(R.string.mrra_control_mode_passive),
    PASSIVE(R.string.mrra_control_mode_initiative),
    MEMORY(R.string.mrra_control_mode_memory),
    REAPPEARANCE(R.string.mrra_control_mode_reappearance),
    STOP(R.string.mrra_control_mode_stop)
}