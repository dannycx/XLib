package com.danny.demo.database

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long

    @Update
    fun updateUser(newUser: User)

//    @Query("select * from user")
//    fun queryAllUser(user: User): ArrayList<User>

    @Query("select * from user where age > :age")
    fun queryUserOlderThan(age: Int): Long

    @Delete
    fun deleteUser(user: User)

    @Query("delete from user where name = :name")
    fun deleteUserByName(name: String): Int
}