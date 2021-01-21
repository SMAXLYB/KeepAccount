package life.chenshi.keepaccounts.ui.setting

import androidx.lifecycle.ViewModel
import life.chenshi.keepaccounts.database.AppDatabase

class SettingViewModel : ViewModel() {
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }

    fun hasDefaultBook(doIfHas: (Int) -> Unit, doIfNot: (Unit) -> Unit) {

    }
}