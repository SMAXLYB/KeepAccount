package life.chenshi.keepaccounts.module.common.utils

fun String?.ifNullOrBlank(defaultValue: () -> String?): String? = if (this.isNullOrBlank()) {
    defaultValue()
} else {
    this
}