package life.chenshi.keepaccounts.module.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
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
