package com.example.aniconnect

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.random.Random

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(SQL_CREATE_TBL_MAIN)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // upgrade policy: simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val TAG = "ANIconnect:MyDbHelper"
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "aniconnect.db"

        const val TBL_MAIN = "anime"
        const val COL_ID = "_id"
        const val COL_NAME = "name"
        const val COL_EPS_USER = "episodes_progress"
        const val COL_EPS_CUR = "episodes_current"
        const val COL_EPS_TOTAL = "episodes_total"
        const val COL_TIME = "release_dt" // datetime in iso8601 T-separated
        const val COL_STRM_URL = "stream_url"
        const val COL_WEB_URL = "web_url"
        const val COL_MAGN_URL = "magnet_url"
        const val SQL_CREATE_TBL_MAIN = """
            CREATE TABLE $TBL_MAIN (
                $COL_ID         INTEGER PRIMARY KEY NOT NULL,
                $COL_NAME       TEXT,
                $COL_EPS_USER   INTEGER,
                $COL_EPS_CUR    INTEGER,
                $COL_EPS_TOTAL  INTEGER,
                $COL_TIME       TEXT,
                $COL_STRM_URL   TEXT,
                $COL_WEB_URL    TEXT,
                $COL_MAGN_URL   TEXT )
            """
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TBL_MAIN"
    }
}