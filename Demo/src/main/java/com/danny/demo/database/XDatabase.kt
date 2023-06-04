package com.danny.demo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.danny.demo.database.book.Book
import com.danny.demo.database.book.BookDao

//@Database(entities = [User::class, Book::class], version = 3, exportSchema = false)
//abstract class XDatabase: RoomDatabase() {
//    abstract fun userDao(): UserDao
//    abstract fun bookDao(): BookDao
//
//    companion object {
//        private var instance: XDatabase? = null
//
//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("create table Book (id integer primary key autoincrement not null" +
//                        ", name text not null, pages integer not null)")
//            }
//
//        }
//
//        val MIGRATION_2_3 = object : Migration(2, 3) {
//
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("alter table Book add column author text not null default 'unknown'")
//            }
//
//        }
//
//        @Synchronized
//        fun generateDataBase(context: Context): XDatabase {
//            instance?.let {
//                return it
//            }
//            return Room.databaseBuilder(context.applicationContext, XDatabase::class.java, "x")
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
//                .build().apply {
//                    instance = this
//                }
//        }
//    }
//}
