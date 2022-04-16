package life.chenshi.keepaccounts.module.common.constant

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import life.chenshi.keepaccounts.module.common.base.IBaseRouterService

/**
 * noInline不内联, lambda函数会生成对象
 * 不使用crossInline, 会导致extra被内联到调用处, 如果有return可能会提前返回, 使用crossInline避免
 */
inline fun <reified Service : IBaseRouterService> Context.navTo(
    dest: Path = Path(""), crossinline extra: (Bundle.() -> Unit) = {}
) {
    return ARouter.getInstance().navigation(Service::class.java).navTo(this, dest) {
        extra.invoke(this)
    }
}

/**
 * @param path 请在本文件中定义path, 以及选择path
 */
@JvmInline
value class Path(val path: String)

// Record模块
const val RECORD_EDIT = "/record/edit_record"

// Setting模块
const val PATH_SETTING_ALL_SETTING = "/setting/all_setting"
const val PATH_SETTING_THEME = "/setting/theme"

// Search模块
const val SEARCH_MAIN_ACTIVITY = "/search/main_activity"

// Category模块
const val ROUTE_CATEGORY = "/category/route"
const val PATH_CATEGORY_ALL = "/category/all_category"

// Book模块
const val ROUTE_BOOK = "/book/route"
