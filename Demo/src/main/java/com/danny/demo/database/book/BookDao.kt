package com.danny.demo.database.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Long

    @Query("select * from book")
    fun queryAllBook(): List<Book>

}