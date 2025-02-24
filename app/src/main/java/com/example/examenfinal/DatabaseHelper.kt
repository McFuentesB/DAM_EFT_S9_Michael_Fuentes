package com.example.examenfinal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT UNIQUE, " +
                "$COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_USERS"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    // Obtener un usuario por nombre de usuario
    fun getUser(username: String): Cursor? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        return db.rawQuery(query, arrayOf(username))
    }

    // Actualizar la contraseña de un usuario
    fun updatePassword(username: String, newPassword: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PASSWORD, newPassword)
        }
        val whereClause = "$COLUMN_USERNAME = ?"
        val whereArgs = arrayOf(username)
        db.update(TABLE_USERS, values, whereClause, whereArgs)
    }

    // Método para validar un usuario y contraseña
    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        // Si hay algún resultado, el usuario y la contraseña son válidos
        val isValidUser = cursor.moveToFirst()
        cursor.close() // Cerrar el cursor
        return isValidUser
    }

    // Método para registrar un nuevo usuario
    fun registerUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        // Comprobamos si el usuario ya existe
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?", arrayOf(username))

        return if (cursor.moveToFirst()) {
            // El usuario ya existe
            cursor.close()
            false
        } else {
            // Insertamos el nuevo usuario
            db.insert(TABLE_USERS, null, values)
            cursor.close()
            true
        }
    }
}
