package life.chenshi.keepaccounts.module.common.utils

fun String?.ifNullOrEmpty(defaultValue: () -> String): String = if (this.isNullOrEmpty()) {
    defaultValue()
} else {
    this
}