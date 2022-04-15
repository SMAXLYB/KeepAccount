package life.chenshi.keepaccounts.module.common.service

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.template.IProvider
import life.chenshi.keepaccounts.module.common.constant.Path

interface ICategoryService : IProvider {
    fun navTo(context: Context, dest: Path = Path(""), extra: (Bundle.() -> Unit)? = null)
}