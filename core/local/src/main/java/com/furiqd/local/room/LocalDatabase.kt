package com.furiqd.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furiqd.local.entity.JointPetEntity
import com.furiqd.local.entity.LocalUserEntity

@Database(entities = [JointPetEntity::class, LocalUserEntity::class],
    version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun userDao(): UserDao

}
