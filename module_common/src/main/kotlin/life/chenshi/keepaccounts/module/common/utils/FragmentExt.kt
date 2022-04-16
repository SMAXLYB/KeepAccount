package life.chenshi.keepaccounts.module.common.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import java.io.Serializable

inline fun <reified T> Fragment.arguments(key: String) = lazy(LazyThreadSafetyMode.NONE) {
    when (T::class) {
        String::class -> arguments?.getString(key) as? T
        Int::class -> arguments?.getInt(key, 0) as? T
        Boolean::class -> arguments?.getBoolean(key, false) as? T
        Serializable::class -> arguments?.getSerializable(key) as? T
        else -> arguments?.get(key) as? T
    }
}

val Fragment.navController: NavController?
    get() = try {
        NavHostFragment.findNavController(this)
    } catch (e: IllegalStateException) {
        null
    }

fun Fragment.onBackPressed() {
    // 如果有nav
    navController?.let {
        if (it.previousBackStackEntry == null) {
            requireActivity().onBackPressed()
        } else {
            it.navigateUp()
        }
        return
    }

    // 如果没有nav
    requireActivity().onBackPressed()
}