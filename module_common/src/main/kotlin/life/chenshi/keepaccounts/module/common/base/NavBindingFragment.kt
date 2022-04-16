package life.chenshi.keepaccounts.module.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class NavBindingFragment<T : ViewDataBinding> : BaseFragment() {
    private var _binding: T? = null

    // 使用幕后字段不会引用， 编译后只会生成get方法，不会引起泄漏
    protected val binding: T
        get() = _binding!!

    @Deprecated("初始化view逻辑和初始加载数据请求都放在initView()中, 避免view重建反复请求")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (_binding == null) {
            _binding = DataBindingUtil.inflate(inflater, setLayoutId(), container, false)
            initView()
        }
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
    }

    /**
     * 初始化view, 只调用一次, 因为navigation会重建view, 为了避免这个情况,
     * 必须将view缓存起来, 后续初始化的操作只需执行一次, 所以这里只会回调一次
     */
    open fun initView() {}

    /**
     * 初始化监听器, view重建会调用多次
     */
    open fun initListener() {}

    /**
     * 初始化liveData监听, view重建会调用多次, 监听时使用viewLifeCycleOwner
     */
    open fun initObserver() {}

    /**
     * dataBinding是动态生成的, 这里使用泛型, 无法确定, 必须传一个layoutId
     */
    protected abstract fun setLayoutId(): Int

    override fun onDestroy() {
        super.onDestroy()
        _binding?.unbind()
        _binding = null
    }
}