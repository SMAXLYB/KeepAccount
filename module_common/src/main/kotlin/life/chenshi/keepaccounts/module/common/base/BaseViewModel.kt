package life.chenshi.keepaccounts.module.common.base

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

open class BaseViewModel : ViewModel() {
}

inline fun <reified VM : ViewModel> ComponentActivity.appViewModels(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { BaseApplication.application.viewModelStore },
        { defaultViewModelProviderFactory }
    )
}

inline fun <reified VM : ViewModel> Fragment.appViewModels(): Lazy<VM> {
    return createViewModelLazy(
        VM::class,
        { BaseApplication.application.viewModelStore },
        { defaultViewModelProviderFactory }
    )
}