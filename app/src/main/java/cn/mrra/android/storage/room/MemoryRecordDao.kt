package cn.mrra.android.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.mrra.android.entity.MemoryRecord

@Dao
interface MemoryRecordDao {

    @Query("SELECT * FROM  memory_record ORDER BY id ASC")
    fun selectRecords(): List<MemoryRecord>

    @Insert
    fun insertRecords(records: List<MemoryRecord>)

    @Query("DELETE FROM memory_record")
    fun deleteRecords()

}