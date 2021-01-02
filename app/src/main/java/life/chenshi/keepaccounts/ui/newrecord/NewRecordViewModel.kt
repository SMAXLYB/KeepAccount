package life.chenshi.keepaccounts.ui.newrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.Record
import life.chenshi.keepaccounts.database.RecordDatabase

class NewRecordViewModel : ViewModel() {

    private val recordDatabase by lazy { RecordDatabase.getDatabase() }

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            recordDatabase.getRecordDao().insertRecord(record)
        }
    }
}