package cn.mrra.android.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.mrra.android.entity.MemoryRecord

@Database(entities = [MemoryRecord::class], version = 1)
abstract class RoomDao : RoomDatabase() {

    abstract fun memoryRecordDao(): MemoryRecordDao

}