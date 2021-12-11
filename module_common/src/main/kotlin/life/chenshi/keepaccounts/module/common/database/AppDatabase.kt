package life.chenshi.keepaccounts.module.common.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import life.chenshi.keepaccounts.module.common.base.BaseApplication
import life.chenshi.keepaccounts.module.common.constant.TB_BOOKS
import life.chenshi.keepaccounts.module.common.constant.TB_MAJOR_CATEGORIES
import life.chenshi.keepaccounts.module.common.constant.TB_MINOR_CATEGORIES
import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.dao.MajorCategoryDao
import life.chenshi.keepaccounts.module.common.database.dao.MinorCategoryDao
import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import life.chenshi.keepaccounts.module.common.database.entity.Book
import life.chenshi.keepaccounts.module.common.database.entity.MajorCategory
import life.chenshi.keepaccounts.module.common.database.entity.MinorCategory
import life.chenshi.keepaccounts.module.common.database.entity.Record
import java.io.BufferedReader
import java.io.InputStreamReader

@Database(entities = [Record::class, Book::class, MajorCategory::class, MinorCategory::class], version = 1)
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
                            BaseApplication.application,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                            // .addMigrations(MIGRATION_1_TO_2)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    // 第一次打开应用时预填充数据
                                    // 开启事务
                                    db.beginTransaction()
                                    try {
                                        // 默认账本
                                        val bookSql = "insert into $TB_BOOKS (id,name,description) values(?,?,?)"
                                        db.execSQL(bookSql, arrayOf("1","日常账本","记录日常开支"))
                                        // 默认分类
                                        val majorSql = "insert into $TB_MAJOR_CATEGORIES (id,name,record_type) values(?,?,?)"
                                        val minorSql = "insert into $TB_MINOR_CATEGORIES (id,name,record_type,major_category_id) values(?,?,?,?)"
                                        getDefaultMajorCategories().forEach {
                                            db.execSQL(majorSql, arrayOf(it.id.toString(), it.name, it.recordType.toString()))
                                        }
                                        getDefaultMinorCategories().forEach {
                                            db.execSQL(
                                                minorSql,
                                                arrayOf(it.id.toString(), it.name, it.recordType.toString(), it.majorCategoryId.toString())
                                            )
                                        }
                                        // 提交事务
                                        db.setTransactionSuccessful()
                                    } finally {
                                        // 关闭事务
                                        db.endTransaction()
                                    }
                                }
                            })
                            .build()
                    }
                }
            }

            return INSTANCE as AppDatabase
        }

        fun getDefaultMajorCategories(): List<MajorCategory> {
            val inputStream = BaseApplication.application.assets.open("jsons/default_major_categories.json")
            BufferedReader(InputStreamReader(inputStream, "utf-8")).use { reader ->
                val buffer = StringBuffer()
                reader.forEachLine {
                    buffer.append(it)
                }
                return Gson().fromJson(buffer.toString(), object : TypeToken<List<MajorCategory>>() {}.type)
            }
        }

        fun getDefaultMinorCategories(): List<MinorCategory> {
            val inputStream = BaseApplication.application.assets.open("jsons/default_minor_categories.json")
            BufferedReader(InputStreamReader(inputStream, "utf-8")).use { reader ->
                val buffer = StringBuffer()
                reader.forEachLine {
                    buffer.append(it)
                }
                return Gson().fromJson(buffer.toString(), object : TypeToken<List<MinorCategory>>() {}.type)
            }
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

    abstract fun getMajorCategoryDao(): MajorCategoryDao

    abstract fun getMinorCategoryDao(): MinorCategoryDao
}