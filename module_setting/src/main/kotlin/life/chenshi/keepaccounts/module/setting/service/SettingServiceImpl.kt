package life.chenshi.keepaccounts.module.setting.service

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.alibaba.android.arouter.facade.annotation.Route
import life.chenshi.keepaccounts.module.common.constant.Path
import life.chenshi.keepaccounts.module.common.constant.ROUTE_SETTING
import life.chenshi.keepaccounts.module.common.constant.START_DESTINATION
import life.chenshi.keepaccounts.module.common.service.ISettingRouterService
import life.chenshi.keepaccounts.module.setting.ui.MainActivity

@SuppressWarnings("unused")
@Route(path = ROUTE_SETTING)
class SettingServiceImpl : ISettingRouterService {

    override fun navTo(context: Context, dest: Path, extra: (Bundle.() -> Unit)) {
        val data = bundleOf(START_DESTINATION to dest.path).apply(extra)
        MainActivity.start(context, data)
    }


    override fun init(context: Context) {

    }
}