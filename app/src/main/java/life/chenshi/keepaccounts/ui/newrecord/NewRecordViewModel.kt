package life.chenshi.keepaccounts.ui.newrecord

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordDatabase
import java.util.*

class NewRecordViewModel : ViewModel() {

    private val mRecordDatabase by lazy { RecordDatabase.getDatabase() }
    // 选择好的时间
    val mCurrentChooseCalendar: Calendar = Calendar.getInstance()
    // 最后一次选择的类型
    var lastSelectedCategoryIndex = 0
    val categoryViews = mutableListOf<View>()

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            mRecordDatabase.getRecordDao().insertRecord(record)
        }
    }
}