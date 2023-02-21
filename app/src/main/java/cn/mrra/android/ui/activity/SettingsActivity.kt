package cn.mrra.android.ui.activity

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.ActivitySettingsBinding

class SettingsActivity : SimpleActivity<ActivitySettingsBinding>() {

    override val layoutId: Int = R.layout.activity_settings

    private lateinit var customaryLang: String
    private lateinit var currentLang: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        with(binding) {
//            cvSettingEn.alpha = 0.4F
//            cvSettingCn.alpha = 0.4F
//
//            cvSettingEn.setOnClickListener {
//                if (currentLang == "en") return@setOnClickListener false
//                runBlocking {
//                    setLocale(LOCALE_EN)
//                    saveLocale(LOCALE_EN)
//                    setHighLight(false)
//                    false
//                }
//            }
//
//            cn.setOnClickListener {
//                if (currentLang == "zh") return@setOnClickListener false
//                runBlocking {
//                    setLocale(LOCALE_ZH)
//                    saveLocale(LOCALE_ZH)
//                    setHighLight(true)
//                    false
//                }
//            }
//
            tvSettingSave.setOnClickListener {
                startActivity<MRRAActivity>()
            }
//
//            getLocale().let { locale ->
//                if (locale.language == "zh") {
//                    customaryLang = "zh"
//                    setHighLight(true)
//                } else {
//                    customaryLang = "en"
//                    setHighLight(false)
//                }
//
//            }
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        return if (keyCode == KeyEvent.KEYCODE_BACK && currentLang != customaryLang) {
//            AlertDialog.Builder(this)
//                .setMessage(resources.getString(R.string.setting_change_language))
//                .setPositiveButton(resources.getString(R.string.setting_change_language_ok)) { _, _ ->
//                    restartApplication()
//                }
//                .setNegativeButton(resources.getString(R.string.setting_change_language_cancel)) { _, _ ->
//                    setHighLight(customaryLang == "zh")
//                }
//                .show()
//            true
//        } else {
//            super.onKeyDown(keyCode, event)
//        }
//    }
//
//    private fun setHighLight(isCN: Boolean) {
//        val highlight: CircleView
//        val formal: CircleView
//        currentLang = if (isCN) {
//            highlight = cn
//            formal = en
//            "zh"
//        } else {
//            highlight = en
//            formal = cn
//            "en"
//        }
//        highlight.isEnabled = false
//        formal.isEnabled = true
//        ObjectAnimator.ofFloat(
//            highlight,
//            "alpha",
//            highlight.alpha,
//            1F
//        ).apply {
//            duration = 200L
//            setAutoCancel(true)
//            start()
//        }
//        ObjectAnimator.ofFloat(
//            formal,
//            "alpha",
//            formal.alpha,
//            0.4F
//        ).apply {
//            duration = 200L
//            setAutoCancel(true)
//            start()
//        }
//        highlight.recoverCircleRadius()
//        formal.reductionCircleRadius()
//    }

}