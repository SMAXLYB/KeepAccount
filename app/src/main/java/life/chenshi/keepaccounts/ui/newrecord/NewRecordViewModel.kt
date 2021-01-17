package life.chenshi.keepaccounts.ui.newrecord

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.entity.Record
import life.chenshi.keepaccounts.database.AppDatabase
import java.util.*

class NewRecordViewModel : ViewModel() {

    private val mRecordDao by lazy { AppDatabase.getDatabase().getRecordDao() }

    // 选择好的时间
    val mCurrentChooseCalendar: Calendar = Calendar.getInstance()

    // 最后一次选择的类型
    var lastSelectedCategoryIndex = 0
    val categoryViews = mutableListOf<View>()

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            mRecordDao.insertRecord(record)
        }
    }

    fun updateRecord(record: Record) {
        viewModelScope.launch {
            mRecordDao.updateRecord(record)
        }
    }
}