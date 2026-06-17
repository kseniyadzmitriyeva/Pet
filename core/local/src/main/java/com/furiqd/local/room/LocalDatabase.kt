package com.furiqd.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furiqd.local.entity.JointPetEntity

@Database(entities = [JointPetEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}
