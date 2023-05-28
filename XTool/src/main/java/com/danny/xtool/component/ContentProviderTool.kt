/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.component

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * create by danny in 20200523.
 * 内容提供者
 */
// uri：content://com.danny.provider/*   匹配任意表内容uri
// uri：content://com.danny.provider/tableX/#   匹配tableX表任意一行数据内容uri
class ContentProviderTool: ContentProvider() {
    private val tableXDir = 0
    private val tableXItem = 1
    private val tableXTDir = 2
    private val tableXTItem = 3
    private val authority = "com.danny.provider"
    private val tableDir = "vnd.android.cursor.dir"
    private val tableItem = "vnd.android.cursor.item"

    private var dbHelper: XDbOpenHelper? = null

    private val uriMatcher by lazy {// 懒加载
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authority, "tableX", tableXDir)
        uriMatcher.addURI(authority, "tableX/#", tableXItem)
        uriMatcher.addURI(authority, "tableXT", tableXTDir)
        uriMatcher.addURI(authority, "tableXT/#", tableXTItem)
        uriMatcher
    }

    /**
     * 插入
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? = dbHelper?.let {
        val db = it.writableDatabase
        val uriResult = when(uriMatcher.match(uri)) {
            tableXDir, tableXItem -> {
                val newTableXId = db.insert("tableX", null, values)
                Uri.parse("content://${authority}/tableX/$newTableXId")
            }

            tableXTDir, tableXTItem -> {
                val newTableXTId = db.insert("tableXT", null, values)
                Uri.parse("content://${authority}/tableXT/$newTableXTId")
            }
            else -> null
        }
        uriResult
    }

    /**
     * 查询
     *
     * uri：content://com.danny.provider/tableX  协议声明 + authority + path
     * projection：查询列
     * selection-selectionArgs：约束条件
     * sortOrder：排序
     */
    @SuppressLint("Recycle")
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = dbHelper?.let {
        val db = it.readableDatabase
        val c = when(uriMatcher.match(uri)) {
            tableXDir ->
                db.query("tableX", projection, selection, selectionArgs
                    , null, null, sortOrder)
            tableXItem -> {
                val tableXId = uri.pathSegments[1]
                db.query("tableX", projection, "id = ?", arrayOf(tableXId)
                    , null, null, sortOrder)
            }
            tableXTDir ->
                db.query("tableXT", projection, selection, selectionArgs
                    , null, null, sortOrder)
            tableXTItem -> {
                val tableXTId = uri.pathSegments[1]
                db.query("tableXT", projection, "id = ?", arrayOf(tableXTId)
                    , null, null, sortOrder)
            }
            else -> null
        }
        c
    }

    /**
     * 完成数据库创建升级
     * true - ContentProvider初始化成功
     */
    override fun onCreate(): Boolean = context?.let {
        dbHelper = XDbOpenHelper(it, "x.db", 1)
        true
    } ?: false

    /**
     * 更新
     */
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = dbHelper?.let {
        val db = it.writableDatabase
        val updateRows = when(uriMatcher.match(uri)) {
            tableXDir ->  db.update("tableX", values, selection, selectionArgs)
            tableXItem -> {
                val updateId = uri.pathSegments[1]
                db.update("tableX", values, "id = ?", arrayOf(updateId))
            }
            tableXTDir -> db.update("tableXT", values, selection, selectionArgs)
            tableXTItem -> {
                val updateTId = uri.pathSegments[1]
                db.update("tableXT", values, "id = ?", arrayOf(updateTId))
            }
            else -> 0
        }
        updateRows
    } ?: 0

    /**
     * 删除
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = dbHelper?.let {
        val db = it.writableDatabase
        val deleteRows = when(uriMatcher.match(uri)) {
            tableXDir ->  db.delete("tableX", selection, selectionArgs)
            tableXItem -> {
                val updateId = uri.pathSegments[1]
                db.delete("tableX", "id = ?", arrayOf(updateId))
            }
            tableXTDir -> db.delete("tableXT", selection, selectionArgs)
            tableXTItem -> {
                val updateTId = uri.pathSegments[1]
                db.delete("tableXT", "id = ?", arrayOf(updateTId))
            }
            else -> 0
        }
        deleteRows
    } ?: 0

    /**
     * 根据传入内容的uri返回MIME类型
     * uri：content://com.danny.provider/tableX      vnd.android.cursor.dir/vad.com.danny.provider.tableX
     * uri：content://com.danny.provider/tableX/1    vnd.android.cursor.item/vad.com.danny.provider.tableX
     */
    override fun getType(uri: Uri): String? = when(uriMatcher.match(uri)) {
        tableXDir -> "${tableDir}/vad.${authority}.tableX"
        tableXItem -> "${tableItem}/vad.${authority}.tableX"
        tableXTDir -> "${tableDir}/vad.${authority}.tableXT"
        tableXTItem -> "${tableItem}/vad.${authority}.tableXT"
        else -> null
    }
}