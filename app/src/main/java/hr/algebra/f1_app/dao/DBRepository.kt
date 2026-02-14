package hr.algebra.f1_app.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.f1_app.model.F1Driver

private const val DB_NAME = "drivers.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "drivers"

private val CREATE_TABLE = """
    create table $TABLE_NAME(
        ${F1Driver::_id.name} integer primary key autoincrement,
        ${F1Driver::fullName.name} text not null,
        ${F1Driver::firstName.name} text not null,
        ${F1Driver::lastName.name} text not null,
        ${F1Driver::code.name} text not null,
        ${F1Driver::teamName.name} text not null,
        ${F1Driver::teamColour.name} text not null,
        ${F1Driver::driverNumber.name} integer not null,
        ${F1Driver::headshotPath.name} text not null,
        ${F1Driver::meetingKey.name} integer not null,
        ${F1Driver::sessionKey.name} integer not null,
        ${F1Driver::countryCode.name} text,
        ${F1Driver::favorite.name} integer not null
    )
""".trimIndent()

private const val DROP_TABLE = "drop table $TABLE_NAME"

class DBRepository(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    Repository {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

    override fun update(
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun insert(values: ContentValues?) =
        writableDatabase.insert(TABLE_NAME, null, values)
}
