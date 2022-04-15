package life.chenshi.keepaccounts.module.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.io.Serializable


inline fun <reified T> Context.startActivity() {
    this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
    this.startActivity(Intent(this, T::class.java).apply {
        block()
    })
}

inline fun <reified T> Activity.intents(key: String) = lazy(LazyThreadSafetyMode.NONE) {
    when (T::class) {
        String::class -> intent.getStringExtra(key) as? T
        Int::class -> intent.getIntExtra(key, 0) as? T
        Boolean::class -> intent.getBooleanExtra(key, false) as? T
        Serializable::class -> intent.getSerializableExtra(key) as? T
        else -> intent.extras?.get(key) as? T
    }
}

inline fun <reified T> Fragment.arguments(key: String) = lazy(LazyThreadSafetyMode.NONE) {
    when (T::class) {
        String::class -> arguments?.getString(key) as? T
        Int::class -> arguments?.getInt(key, 0) as? T
        Boolean::class -> arguments?.getBoolean(key, false) as? T
        Serializable::class -> arguments?.getSerializable(key) as? T
        else -> arguments?.get(key) as? T
    }
}