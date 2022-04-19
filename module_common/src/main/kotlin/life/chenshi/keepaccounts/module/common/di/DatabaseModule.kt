package life.chenshi.keepaccounts.module.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import life.chenshi.keepaccounts.module.common.database.AppDatabase
import life.chenshi.keepaccounts.module.common.database.dao.BookDao
import life.chenshi.keepaccounts.module.common.database.dao.MajorCategoryDao
import life.chenshi.keepaccounts.module.common.database.dao.MinorCategoryDao
import life.chenshi.keepaccounts.module.common.database.dao.RecordDao
import javax.inject.Singleton

/*
* @Module标记一个类是提供对象地方
* @InstallIn标记module安装在哪个组件，也就是给哪些android类提供服务
*
*
*
*/
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    /**
     * @param context 从hilt中获取全局context
     * Provides标记此处提供依赖项
     * Singleton标记在全局范围内提供同一个依赖项， 不会重新创建
     */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    // 因为appDatabase是单例的，所以这里可以不用用singleton注解
    @Provides
    fun provideRecordDao(appDatabase: AppDatabase): RecordDao {
        return appDatabase.getRecordDao()
    }

    @Provides
    fun getBookDao(appDatabase: AppDatabase): BookDao {
        return appDatabase.getBookDao()
    }

    @Provides
    fun getMajorCategoryDao(appDatabase: AppDatabase): MajorCategoryDao {
        return appDatabase.getMajorCategoryDao()
    }

    @Provides
    fun getMinorCategoryDao(appDatabase: AppDatabase): MinorCategoryDao {
        return appDatabase.getMinorCategoryDao()
    }
}