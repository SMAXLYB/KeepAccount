package life.chenshi.keepaccounts.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

object DataStoreUtil {
    @PublishedApi
    internal lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context) {
        dataStore = context.dataStore
    }

    suspend inline fun <reified T : Any> writeToDataStore(key: String, value: T) {
        dataStore.edit {
            when (value::class) {
                Int::class -> {
                    val pKey = intPreferencesKey(key)
                    it[pKey] = value as Int
                }
                String::class -> {
                    val pKey = stringPreferencesKey(key)
                    it[pKey] = value as String
                }
                Boolean::class -> {
                    val pKey = booleanPreferencesKey(key)
                    it[pKey] = value as Boolean
                }
                Long::class -> {
                    val pKey = longPreferencesKey(key)
                    it[pKey] = value as Long
                }
                Float::class -> {
                    val pKey = floatPreferencesKey(key)
                    it[pKey] = value as Float
                }
                Double::class -> {
                    val pKey = doublePreferencesKey(key)
                    it[pKey] = value as Double
                }
            }
        }
    }

    inline fun <reified T : Any> readFromDataStore(key: String, default: T): Flow<T> {
        return dataStore.data.map {
            val pKey = when (default::class) {
                Int::class -> {
                    intPreferencesKey(key)
                }
                String::class -> {
                    stringPreferencesKey(key)
                }
                Boolean::class -> {
                    booleanPreferencesKey(key)
                }
                Long::class -> {
                    longPreferencesKey(key)
                }
                Float::class -> {
                    floatPreferencesKey(key)
                }
                else -> doublePreferencesKey(key)
            }
            (it[pKey] ?: default) as T
        }
    }
}