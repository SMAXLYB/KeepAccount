package life.chenshi.keepaccounts.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import life.chenshi.keepaccounts.database.dao.BookDao
import life.chenshi.keepaccounts.database.dao.RecordDao
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.global.MyApplication

@Database(entities = [Record::class, Book::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "db_keep_accounts.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            MyApplication.getInstance(),
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                            // .addMigrations(MIGRATION_1_TO_2)
                            .build()
                    }
                }
            }

            return INSTANCE as AppDatabase
        }

        // 数据库升级, 新增账本id字段
        private val MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                        CREATE TABLE tb_books (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            description TEXT)
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE UNIQUE INDEX index_tb_books_name ON tb_books (name)
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        INSERT INTO tb_books VALUES (1, '默认账本','这是默认账本')
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE TABLE new_tb_records (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            money INTEGER NOT NULL, 
                            remark TEXT,
                            time INTEGER NOT NULL,
                            category INTEGER NOT NULL,
                            record_type INTEGER NOT NULL,
                            book_id INTEGER NOT NULL DEFAULT 1, 
                            FOREIGN KEY(book_id) REFERENCES tb_books(id) ON UPDATE NO ACTION ON DELETE CASCADE)
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        CREATE INDEX index_tb_records_money_time_category_record_type_book_id ON new_tb_records (money, time, category, record_type, book_id)
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        INSERT INTO new_tb_records (id, money, remark, time, category, record_type)
                        SELECT id, money, remark, time, category, record_type FROM tb_records
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE tb_records")
                database.execSQL("ALTER TABLE new_tb_records RENAME TO tb_records")
            }
        }
    }

    abstract fun getRecordDao(): RecordDao

    abstract fun getBookDao(): BookDao
}