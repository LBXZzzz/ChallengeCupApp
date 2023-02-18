package cn.mrra.android.entity

import androidx.annotation.StringRes
import cn.mrra.android.R

enum class ControlMode(@StringRes val label: Int) {
    NOT_CONNECTED(R.string.mrra_control_not_connected),
    TRY(R.string.mrra_control_try),
    ERROR(R.string.mrra_control_error),

    INITIATIVE(R.string.mrra_control_mode_passive),
    PASSIVE(R.string.mrra_control_mode_initiative),
    MEMORY(R.string.mrra_control_mode_memory),
    STOP(R.string.mrra_control_mode_stop),

    MEMORY_REMEMBER(R.string.mrra_control_mode_remember),
    MEMORY_REAPPEARANCE(R.string.mrra_control_mode_reappearance)
}