package life.chenshi.keepaccounts.module.common.utils

import android.app.Activity
import android.content.Intent
import java.io.Serializable


inline fun <reified T> Activity.startActivity() {
    this.startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Activity.startActivity(block: Intent.() -> Unit) {
    this.startActivity(Intent(this, T::class.java).apply {
        block()
    })
}

inline fun <reified T> Activity.getValueFromIntent(key: String) =
    when (T::class) {
        String::class -> intent.getStringExtra(key) as? T
        Int::class -> intent.getIntExtra(key, 0) as? T
        Boolean::class -> intent.getBooleanExtra(key, false) as? T
        Serializable::class -> intent.getSerializableExtra(key) as? T
        else -> intent.extras?.get(key) as? T
    }
