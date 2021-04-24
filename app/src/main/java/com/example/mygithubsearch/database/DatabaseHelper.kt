package com.example.mygithubsearch.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mygithubsearch.database.DatabaseContract.FavoriteColumns
import com.example.mygithubsearch.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME


internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {

    companion object {
        private const val DATABASE_NAME = "dbgithubseacrh"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${FavoriteColumns.USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${FavoriteColumns.NAME} TEXT NOT NULL," +
                " ${FavoriteColumns.AVATAR} TEXT NOT NULL," +
                " ${FavoriteColumns.COMPANY} TEXT NOT NULL," +
                " ${FavoriteColumns.LOCATION} TEXT NOT NULL," +
                " ${FavoriteColumns.FOLLOWERS} TEXT NOT NULL," +
                " ${FavoriteColumns.FOLLOWING} TEXT NOT NULL," +
                " ${FavoriteColumns.REPOSITORY} TEXT NOT NULL," +
                " ${FavoriteColumns.FAVORITE} TEXT NOT NULL)"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}