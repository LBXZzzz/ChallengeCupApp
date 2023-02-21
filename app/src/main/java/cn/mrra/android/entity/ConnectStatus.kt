package cn.mrra.android.entity

import androidx.annotation.StringRes
import cn.mrra.android.R

enum class ConnectStatus(@StringRes val label: Int) {
    NOT_CONNECTED(R.string.mrra_control_not_connected),
    CONNECTING(R.string.mrra_control_connecting),
    CONNECT_ERROR(R.string.mrra_control_connect_error),
    VERIFYING(R.string.mrra_control_verifying),
    VERIFICATION_FAILED(R.string.mrra_control_verification_failed),
    SUCCESS(R.string.mrra_connect_status_success)
}