package cn.mrra.android.storage.room

import androidx.room.Room
import cn.mrra.android.common.base.appContext

private val room by lazy {
    Room.databaseBuilder(
        appContext,
        RoomDao::class.java,
        "mrra_memory"
    ).build()
}

val MemoryRecordDatabaseDao by lazy { room.memoryRecordDao() }