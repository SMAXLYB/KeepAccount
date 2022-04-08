package life.chenshi.keepaccounts.module.setting.vm

import androidx.lifecycle.MutableLiveData
import life.chenshi.keepaccounts.module.common.base.BaseViewModel

class AllSettingViewModel : BaseViewModel() {
    val title = MutableLiveData<String>("全部设置")
}