/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.component

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 原生数据库demo
 */
class XDbOpenHelper(val context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version) {
    private val createTableX = "create table tableX (" +
            "id integer primary key autoincrement," +
            "name text," +
            "price real," +
            "category_id)"

    private val createTableXT = "create table tableXT (" +
            "id integer primary key autoincrement," +
            "gender text," +
            "price real)"

    private val alterTableX = "alter table tableX add column category_id integer"

    private val dropTableX = "drop table if exists tableX"

    private val dropTableXT = "drop table if exists tableXT"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableX)
        db.execSQL(createTableXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {// 升到2加表
            db.execSQL(createTableXT)
        }
        if (oldVersion <= 2) {// 升到3加字段
            db.execSQL(alterTableX)
        }
    }
}
