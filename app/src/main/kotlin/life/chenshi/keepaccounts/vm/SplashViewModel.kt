package life.chenshi.keepaccounts.vm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {
    private val _counter = MutableStateFlow(3)
    val counter: StateFlow<Int> = _counter

    fun countTo(int: Int) {
        _counter.tryEmit(int)
    }
}