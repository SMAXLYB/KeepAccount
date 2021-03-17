package life.chenshi.keepaccounts.common.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import java.lang.ref.WeakReference

object StatusBarUtil {

    private lateinit var activity: WeakReference<Activity>

    fun init(activity: Activity): StatusBarUtil {
        this.activity = WeakReference(activity)
        return this
    }

    /**
     * 全屏透明
     */
    fun setTransparent(): StatusBarUtil {
        activity.get()?.window?.apply {
            // 由window绘制bar
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            statusBarColor = Color.TRANSPARENT
        }
        return this
    }

    /**
     * 黑暗模式 M以上有效
     */
    fun setDarkMode(darkMode: Boolean): StatusBarUtil {
        StatusBarUtil.activity.get()?.window?.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var systemUiVisibility: Int = decorView.systemUiVisibility
                systemUiVisibility = if (darkMode) {
                    systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                decorView.systemUiVisibility = systemUiVisibility
            }
        }
        return this
    }

    /**
     * 全屏纯色
     * @param colorId 颜色id
     * @param fullScreen 是否全屏
     */
    fun setColor(@ColorRes colorId: Int, fullScreen: Boolean = false): StatusBarUtil {
        StatusBarUtil.activity.get()?.window?.apply {

            // 由window绘制bar
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (fullScreen) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            }
            statusBarColor = activity.get()?.getColorById(colorId) ?: Color.TRANSPARENT
        }
        return this
    }

    /**
     * 增大PaddingTop
     */
    fun addPaddingTop(view: View): StatusBarUtil {
        val layoutParams = view.layoutParams
        if (layoutParams != null && layoutParams.height > 0 && view.paddingTop == 0) {
            val statusBarHeight = getStatusBarHeight()
            layoutParams.height += statusBarHeight
            view.setPadding(view.paddingLeft, view.paddingTop + statusBarHeight, view.paddingRight, view.paddingBottom)
        }

        return this
    }

    /**
     * 获取状态栏高度
     */
    private fun getStatusBarHeight(): Int {
        activity.get()?.let {
            val resId = it.resources.getIdentifier("status_bar_height", "dimen", "android")
            return it.resources.getDimensionPixelSize(resId)
        }
        return 0
    }
}