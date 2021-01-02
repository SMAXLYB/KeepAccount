package life.chenshi.keepaccounts.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import life.chenshi.keepaccounts.global.MyApplication

@Database(entities = [Record::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecordDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "db_keep_accounts.db"

        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getDatabase(): RecordDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            MyApplication.getInstance(),
                            RecordDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }

            return INSTANCE as RecordDatabase
        }
    }

    abstract fun getRecordDao(): RecordDao
}