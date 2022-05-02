package life.chenshi.keepaccounts.module.setting.vm

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import life.chenshi.keepaccounts.module.common.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AllSettingViewModel @Inject constructor() : BaseViewModel() {
    val title = MutableLiveData("")

    val rightIconAction = MutableLiveData<Pair<Drawable, () -> Unit>>()
}