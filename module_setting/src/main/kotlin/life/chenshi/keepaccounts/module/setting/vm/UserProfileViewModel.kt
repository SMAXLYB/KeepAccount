package life.chenshi.keepaccounts.module.setting.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.setting.repo.UserProfileRepo
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repo: UserProfileRepo) : ViewModel() {

    private val _totalNumOfBook = MutableStateFlow(0)
    val totalNumOfBook: StateFlow<Int>
        get() = _totalNumOfBook

    fun getTotalNumOfBook() {
        viewModelScope.launch {
            _totalNumOfBook.emit(repo.getTotalNumOfBook())
        }
    }
}