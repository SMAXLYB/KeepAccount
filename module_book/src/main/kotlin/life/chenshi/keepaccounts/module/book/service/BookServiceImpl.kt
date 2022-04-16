package life.chenshi.keepaccounts.module.book.service

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.alibaba.android.arouter.facade.annotation.Route
import life.chenshi.keepaccounts.module.book.ui.MainActivity
import life.chenshi.keepaccounts.module.common.constant.Path
import life.chenshi.keepaccounts.module.common.constant.ROUTE_BOOK
import life.chenshi.keepaccounts.module.common.constant.START_DESTINATION
import life.chenshi.keepaccounts.module.common.service.IBookRouterService

@SuppressWarnings("unused")
@Route(path = ROUTE_BOOK)
class BookServiceImpl : IBookRouterService {

    override fun navTo(context: Context, dest: Path, extra: Bundle.() -> Unit) {
        val data = bundleOf(START_DESTINATION to dest.path).apply(extra)
        MainActivity.start(context, data)
    }


    override fun init(context: Context) {

    }
}