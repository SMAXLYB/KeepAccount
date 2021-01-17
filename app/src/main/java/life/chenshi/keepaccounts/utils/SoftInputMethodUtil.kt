package life.chenshi.keepaccounts.utils

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 *  +--------------------+
 *  |     状态栏栏高度    |
 *  |--------------------+  rect.top
 *  |        rect        |
 *  |  可见内容区域高度   |
 *  |                    |
 *  |--------------------+  rect.bottom
 *  |                    |
 *  |       键盘高度     |
 *  |                    |
 *  +--------------------+
 *  |    导航栏高度       |
 *  +--------------------+
 */
fun EditText.isSoftInputMethodVisible(): Boolean {
    // 计算总高度
    val deviceHeight = rootView.height
    Log.d("soft", "isSoftInputMethodVisible: $deviceHeight")
    val visibleRect = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleRect)

    return deviceHeight * 2 / 3 > visibleRect.bottom
}

fun EditText.hideSoftInputMethod() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}