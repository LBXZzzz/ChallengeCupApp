package cn.mrra.android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory_record")
data class MemoryRecord(
    @ColumnInfo val tag: String,
    @PrimaryKey val id: Long,
    @ColumnInfo val delay: Long,
    @ColumnInfo val p0: Int,
    @ColumnInfo val p1: Int,
    @ColumnInfo val p2: Int
)
