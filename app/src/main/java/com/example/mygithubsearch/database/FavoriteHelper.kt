package com.example.mygithubsearch.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mygithubsearch.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.example.mygithubsearch.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FavoriteHelper(context)
                }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC",
        )
    }


    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }


    fun deleteById(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }
}