@file:Suppress("NOTHING_TO_INLINE")

package cn.mrra.android.common.log

import android.util.Log
import androidx.annotation.IntDef
import kotlin.annotation.AnnotationTarget.*

object Level {
    const val V = 1
    const val D = 2
    const val I = 4
    const val W = 8
    const val E = 16
    const val WTF = 32
    const val NONE = 64
}

@Target(FIELD, LOCAL_VARIABLE, PROPERTY, VALUE_PARAMETER)
@IntDef(value = [Level.V, Level.D, Level.I, Level.W, Level.E, Level.WTF, Level.NONE])
private annotation class LogLevel

@PublishedApi
@LogLevel
internal const val LOG_LEVEL = Level.V

@PublishedApi
internal const val UNDEFINED_TAG = "Undefined"

inline fun logV(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.V) Log.v(tag, msg)
}

inline fun logD(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.D) Log.d(tag, msg)
}

inline fun logI(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.I) Log.i(tag, msg)
}

inline fun logW(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.W) {
        Log.w(tag, msg)
    }
}

inline fun logE(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.E) Log.e(tag, msg)
}

inline fun logWTF(tag: String = UNDEFINED_TAG, msg: String) {
    if (LOG_LEVEL <= Level.WTF) Log.wtf(tag, msg)
}
