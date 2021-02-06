package life.chenshi.keepaccounts.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreUtil {
    @PublishedApi
    internal lateinit var dataStore: DataStore<Preferences>
    private const val preferenceName = "KeepAccounts"

    fun init(context: Context) {
        dataStore = context.createDataStore(name = preferenceName)
    }

    suspend inline fun <reified T : Any> writeToDataStore(key: String, value: T) {
        dataStore.edit {
            it[preferencesKey<T>(key)] = value
        }
    }

    inline fun <reified T> readFromDataStore(key: String, default: T): Flow<T> {
        return dataStore.data.map {
            it[preferencesKey(key)] ?: default
        }
    }
}