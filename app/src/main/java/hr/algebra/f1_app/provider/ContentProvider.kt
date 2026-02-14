package hr.algebra.f1_app.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import hr.algebra.f1_app.dao.Repository
import hr.algebra.f1_app.dao.getRepository
import hr.algebra.f1_app.model.F1Driver
import java.net.URI

private const val AUTHORITY = "hr.algebra.f1_app.provider"
private const val PATH = "drivers"
val F1_PROVIDER_CONTENT_URI: Uri = "content://$AUTHORITY/$PATH".toUri()

private const val DRIVERS = 10
private const val DRIVER_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)){
    // "content://hr.algebra.f1_app.provider/drivers : 10
    addURI(AUTHORITY, PATH, DRIVERS)
    //"content://hr.algebra.f1_app.provider/drivers/22 : 20
    addURI(AUTHORITY, "$PATH/#", DRIVER_ID)
    this
}

class F1Provider : ContentProvider() {

    private lateinit var repository: Repository

    // "content://hr.algebra.f1_app.provider/drivers  -> SVI ITEMS add
    // "content://hr.algebra.f1_app.provider/drivers/22  -> SINGLE ITEM delete, select, update
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            DRIVERS -> return repository.delete(selection, selectionArgs)
            DRIVER_ID -> {
                val id = uri.lastPathSegment
                if(id != null) {
                    return repository.delete("${F1Driver::_id.name}=?", arrayOf(id))
                }
            }
        }

        throw IllegalArgumentException("WRONG URI")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(F1_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = repository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            DRIVERS -> return repository.update(values, selection, selectionArgs)
            DRIVER_ID -> {
                val id = uri.lastPathSegment
                if(id != null) {
                    return repository.update(values,"${F1Driver::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }
}