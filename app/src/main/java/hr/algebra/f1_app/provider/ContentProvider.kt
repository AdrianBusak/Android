package hr.algebra.f1_app.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import hr.algebra.f1_app.dao.Repository
import hr.algebra.f1_app.dao.getRepository
import hr.algebra.f1_app.model.F1Driver

// ✅ URI koji F1Fetcher traži (linija 71)
val F1_PROVIDER_CONTENT_URI: Uri = "content://hr.algebra.f1.provider/drivers".toUri()

private const val AUTHORITY = "hr.algebra.f1.provider"
private const val PATH = "drivers"

private const val DRIVERS = 10
private const val DRIVER_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, DRIVERS)
    addURI(AUTHORITY, "$PATH/#", DRIVER_ID)
    this
}

class F1Provider : ContentProvider() {

    private lateinit var repository: Repository

    override fun onCreate(): Boolean {
        return try {

            repository = getRepository(context!!)
            Log.d("F1Provider", "✅ Provider CREATED - Repository OK")
            true
        } catch (e: Exception) {
            Log.e("F1Provider", "❌ Provider FAILED", e)
            false
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = try {
        when (URI_MATCHER.match(uri)) {
            DRIVERS -> repository.query(projection, selection, selectionArgs, sortOrder)
            else -> {
                Log.w("F1Provider", "Unknown URI: $uri")
                null
            }
        }
    } catch (e: Exception) {
        Log.e("F1Provider", "Query error", e)
        null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = try {
        Log.d("F1Provider", "INSERT called with URI: $uri")
        when (URI_MATCHER.match(uri)) {
            DRIVERS -> {
                val id = repository.insert(values)
                Log.d("F1Provider", "Inserted ID: $id")
                ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, id)
            }
            else -> {
                Log.w("F1Provider", "Unknown INSERT URI: $uri")
                null
            }
        }
    } catch (e: Exception) {
        Log.e("F1Provider", "Insert error", e)
        null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = try {
        when (URI_MATCHER.match(uri)) {
            DRIVERS -> repository.delete(selection, selectionArgs ?: emptyArray())
            DRIVER_ID -> {
                val id = uri.lastPathSegment ?: return 0
                repository.delete("${F1Driver::_id.name}=?", arrayOf(id))
            }
            else -> 0
        }
    } catch (e: Exception) {
        Log.e("F1Provider", "Delete error", e)
        0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = try {
        when (URI_MATCHER.match(uri)) {
            DRIVERS -> repository.update(values, selection, selectionArgs)
            DRIVER_ID -> {
                val id = uri.lastPathSegment ?: return 0
                repository.update(values, "${F1Driver::_id.name}=?", arrayOf(id))
            }
            else -> 0
        }
    } catch (e: Exception) {
        Log.e("F1Provider", "Update error", e)
        0
    }

    override fun getType(uri: Uri): String? = null
}
