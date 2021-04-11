package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @description: 使用自定义字体的textView
 * @author lyb
 * @date 2021年04月10日 13:40
 */
class SansFontTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun setTypeface(t: Typeface?) {
        val tf = Typeface.createFromAsset(context.assets, "fonts/ProductSans-Regular.ttf")
        super.setTypeface(tf)
    }

}