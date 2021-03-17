package life.chenshi.keepaccounts.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.constant.DB_CURRENT_BOOK_ID
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Book
import java.util.*

class SettingViewModel : ViewModel() {
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }

    fun hasDefaultBook(doIfHas: (Int) -> Unit, doIfNot: (() -> Unit)? = null) {
        viewModelScope.launch {
            var currentBookId = -1
            DataStoreUtil.readFromDataStore(DB_CURRENT_BOOK_ID, -1)
                .collect {
                    currentBookId = it
                    if (currentBookId == -1) {
                        doIfNot?.invoke()
                        return@collect
                    }
                    doIfHas(currentBookId)
                }
        }
    }

    suspend fun getBookNameById(id: Int): Book =
        mBookDao.getBookById(id)

    fun getGreetContent(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 1 until 6 -> {
                "凌晨了，起的真早呀"
            }
            in 6 until 10 -> {
                "早上好，记得吃早餐呦"
            }
            in 10 until 12 -> {
                "上午好，开启工作新一天"
            }
            in 12 until 13 -> {
                "中午好，吃完饭午休一下吧"
            }
            in 13 until 17 -> {
                "下午好，喝杯咖啡吧"
            }
            in 17 until 18 -> {
                "傍晚，下班回家啦"
            }
            in 18 until 23 -> {
                "晚上好，放松一下吧"
            }
            else -> {
                "午夜了，注意休息哦"
            }
        }
    }
}