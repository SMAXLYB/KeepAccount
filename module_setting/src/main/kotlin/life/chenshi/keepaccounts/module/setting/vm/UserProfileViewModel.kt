package life.chenshi.keepaccounts.module.setting.vm

import androidx.lifecycle.ViewModel
import life.chenshi.keepaccounts.module.common.database.AppDatabase

class UserProfileViewModel : ViewModel() {
    private val mBookDao by lazy { AppDatabase.getDatabase().getBookDao() }
}