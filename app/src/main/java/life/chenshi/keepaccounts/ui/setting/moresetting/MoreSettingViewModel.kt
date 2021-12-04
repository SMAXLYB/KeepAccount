package life.chenshi.keepaccounts.ui.setting.moresetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.utils.storage.DataStoreUtil

class MoreSettingViewModel : ViewModel() {

    fun writeToDataStore(key: String, b: Boolean) {
        viewModelScope.launch {
            DataStoreUtil.writeToDataStore(key, b)
        }
    }

    fun readFromDataStore(key: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            DataStoreUtil.readFromDataStore(key, false)
                .take(1)
                .collect {
                    callback.invoke(it)
                }
        }
    }
}