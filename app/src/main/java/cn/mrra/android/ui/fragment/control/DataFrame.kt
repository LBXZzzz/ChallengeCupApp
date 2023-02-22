package cn.mrra.android.ui.fragment.control

import cn.mrra.android.entity.ControlMode
import cn.mrra.android.entity.MemoryRecord
import kotlin.math.round

class DataFrame(val values: ByteArray) {

    companion object {
        fun Int.formatP0ToAngle0(): Int {
            return round(360 * ((this - 32768) / 32768F)).toInt()
        }

        fun Int.formatP1ToAngle1(): Int = formatP0ToAngle0()

        fun Int.formatP2ToAngle2(): Int {
            return round(0.02197 * this).toInt()
        }
    }

    private val data = Array(11) { 0 }

    val head: Char get() = data[0].toChar()

    val mode: Int get() = data[1]

    val p0: Int get() = data[2] + (data[3] shl 8)

    val p1: Int get() = data[4] + (data[5] shl 8)

    val p2: Int get() = data[6] + (data[7] shl 8)

    val passiveMode: Int get() = data[8]

    val stopFlag: Int get() = data[9]

    val tail: Char get() = data[10].toChar()

    val isPassive: Boolean get() = mode == 1

    val isInitiative: Boolean get() = mode == 2

    val isMemory: Boolean get() = mode == 3

    val isStop: Boolean get() = mode == 0 && stopFlag == 1

    val controlMode: ControlMode
        get() {
            return when {
                isPassive -> ControlMode.PASSIVE
                isInitiative -> ControlMode.INITIATIVE
                isMemory -> ControlMode.REAPPEARANCE
                isStop -> ControlMode.STOP
                else -> ControlMode.ERROR
            }
        }

    init {
        when {
            values.size != 11 -> {
                throw IllegalArgumentException("ths size of the characteristic is not 11")
            }
            else -> {
                for (i in values.indices) {
                    data[i] = values[i].toInt() shl 24 ushr 24
                }
            }
        }
    }

    object SimpleDataFrameBuilder {

        private fun setAll(mode: Int, stop: Int, passiveMode: Int): ByteArray {
            return ByteArray(11) {
                when (it) {
                    0 -> 'L'.code.toByte()
                    1 -> mode.toByte()
                    2, 3, 4, 5, 6, 7 -> 0
                    8 -> passiveMode.toByte()
                    9 -> stop.toByte()
                    10 -> 'X'.code.toByte()
                    else -> 0
                }
            }
        }

        fun buildPassive(passiveMode: Int): DataFrame {
            return DataFrame(setAll(1, 0, passiveMode))
        }

        fun buildInitiative(): DataFrame {
            return DataFrame(setAll(2, 0, 0))
        }

        fun buildMemory(record: MemoryRecord): DataFrame {
            return DataFrame(
                ByteArray(11) {
                    when (it) {
                        0 -> 'L'.code.toByte()
                        // 1. mode 3代表记忆模式
                        1 -> 3.toByte()
                        2 -> (record.p0 shl 24 ushr 24).toByte()
                        3 -> (record.p0 shl 16 ushr 24).toByte()
                        4 -> (record.p1 shl 24 ushr 24).toByte()
                        5 -> (record.p1 shl 16 ushr 24).toByte()
                        6 -> (record.p2 shl 24 ushr 24).toByte()
                        7 -> (record.p2 shl 16 ushr 24).toByte()
                        // 8. stop 置为 0 代表运动
                        8 -> 0.toByte()
                        // 9. initMode 非主动模式置 0
                        9 -> 0.toByte()
                        10 -> 'X'.code.toByte()
                        else -> 0
                    }
                }
            )
        }

        fun buildStop(): DataFrame {
            return DataFrame(setAll(0, 1, 0))
        }

    }

}