package life.chenshi.keepaccounts.ui.setting.category

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.common.utils.ToastUtil
import life.chenshi.keepaccounts.common.view.CustomDialog
import life.chenshi.keepaccounts.constant.STATE_DELETE
import life.chenshi.keepaccounts.constant.STATE_NORMAL
import life.chenshi.keepaccounts.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.MajorCategory
import life.chenshi.keepaccounts.database.entity.MinorCategory
import life.chenshi.keepaccounts.databinding.LayoutAddCategoryBinding
import life.chenshi.keepaccounts.databinding.LayoutAddSubCategoryBinding

class CategoryViewModel : ViewModel() {
    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val majorCategoryDao = AppDatabase.getDatabase().getMajorCategoryDao()
    private val minorCategoryDao = AppDatabase.getDatabase().getMinorCategoryDao()

    // 当前选中主类
    val currentMajorCategory = MutableLiveData<MajorCategory?>()

    // 上一次选中主类
    var lastMajorCategory = MutableLiveData<MajorCategory>()

    // 所有主类
    lateinit var majorCategories: LiveData<List<MajorCategory>>

    // 当前选中子类
    val currentMinorCategory = MutableLiveData<MinorCategory>()

    // 所有子类
    var minorCategories = MediatorLiveData<List<MinorCategory>>()

    // 上一次选中子类
    var lastMinorCategory = MutableLiveData<MinorCategory>()
    private var tempMinorCategories: LiveData<List<MinorCategory>>? = null

    // 当前删除模式
    val isDeleteMode = MutableLiveData<Boolean>(false)

    // 是否从其他界面跳转而来
    val business = MutableLiveData<String>()

    init {
        getAllMajorCategory()
    }

    suspend fun insertCategory(majorCategory: MajorCategory) = withContext(Dispatchers.IO) {
        val existCategory = majorCategoryDao.getMajorCategoryBy(majorCategory.name)
        // 如果已经存在并且为-1,重新标记0, 否则走正常流程
        existCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            majorCategoryDao.updateMajorCategory(this)
            return@withContext
        }
        majorCategoryDao.insertMajorCategory(majorCategory)
    }

    /**
     * 删除主类, 连带删除当下所有子类
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun deleteMajorCategory(majorCategory: MajorCategory) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            awaitAll(
                async {
                    majorCategoryDao.deleteMajorCategory(majorCategory.id!!)
                    // 如果删除了选中, 将当选选中清空
                    currentMajorCategory.value?.let {
                        if (it.id == majorCategory.id) {
                            currentMajorCategory.postValue(null)
                        }
                    }
                },
                async {
                    // 连带删除当下所有子类
                    minorCategoryDao.deleteAllMinorCategoryBy(majorCategory.id!!)
                    // 如果包含选中子类,还要置空
                    currentMinorCategory.value?.takeIf {
                        it.majorCategoryId == majorCategory.id
                    }?.run {
                        currentMajorCategory.postValue(null)
                        currentMinorCategory.postValue(null)
                    }
                }
            )
        }
    }

    /**
     * 删除子类
     */
    fun deleteMinorCategory(minorCategory: MinorCategory) {
        viewModelScope.launch {
            minorCategoryDao.deleteMinorCategoryBy(minorCategory.id!!)
        }
        // 如果删除了选中, 将选中清空
        currentMinorCategory.value?.takeIf { it.id == minorCategory.id }?.run {
            currentMinorCategory.value = null
        }
    }

    /**
     * 新增子类
     * @param minorCategory MinorCategory
     */
    private suspend fun insertMinorCategory(minorCategory: MinorCategory) = withContext(Dispatchers.IO) {
        val existSubCategory =
            minorCategoryDao.getMinorCategoryBy(minorCategory.majorCategoryId, minorCategory.name)
        existSubCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            minorCategoryDao.updateMinorCategory(this)
            return@withContext
        }
        minorCategoryDao.insertMinorCategory(minorCategory)
    }


    private fun getAllMajorCategory() {
        majorCategories = majorCategoryDao.getAllMajorCategoryBy(STATE_NORMAL).asLiveData()
    }

    /**
     * 根据 主类id获取所有子类
     * @param majorCategoryId Int
     */
    fun getAllMinorCategoryByMajorCategoryId(majorCategoryId: Int) {
        viewModelScope.launch {
            if (tempMinorCategories != null) {
                minorCategories.removeSource(tempMinorCategories!!)
            }
            tempMinorCategories =
                minorCategoryDao.getALLMinorCategoryBy(majorCategoryId, STATE_NORMAL).asLiveData()
            minorCategories.addSource(tempMinorCategories!!) {
                minorCategories.value = it
            }
        }
    }

    /**
     * 获取主类在adapter中的位置
     * @param recordType 主类类型, 收入/支出 需要考虑所在位置不一样
     */
    suspend fun getCurrentMajorCategoryInAdapterPosition(majorCategory: MajorCategory, recordType: Int) =
        withContext(Dispatchers.Default) {
            majorCategories.value?.asSequence()
                ?.filter { it.recordType == recordType }
                ?.forEachIndexed { index, c ->
                    if (majorCategory.id == c.id) {
                        return@withContext index
                    }
                }
            return@withContext null
        }

    /**
     * 获取子类在adapter中的位置
     */
    suspend fun getCurrentMinorCategoryInAdapterPosition(minorCategory: MinorCategory) =
        withContext(Dispatchers.Default) {
            minorCategories.value?.asSequence()?.forEachIndexed { index, s ->
                if (minorCategory.id == s.id) {
                    return@withContext index
                }
            }
            return@withContext null
        }

    /**
     * 是否要删除前确认
     * @param callback Function1<Boolean, Unit>
     */
    fun confirmBeforeDelete(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            DataStoreUtil.readFromDataStore(SWITCHER_CONFIRM_BEFORE_DELETE, true)
                .take(1)
                .collect {
                    callback.invoke(it)
                }
        }
    }

    /**
     * 删除主类
     * @param activity fragmentActivity
     * @param majorCategory MajorCategory
     */
    fun deleteMajorCategoryWithDialog(activity: FragmentActivity, majorCategory: MajorCategory) {
        CustomDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("删除主类")
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
            .setClosedButtonEnable(false)
            .setPositiveButton("确定") { dialog, _ ->
                deleteMajorCategory(majorCategory)
                dialog.dismiss()
            }
            .setNegativeButton("取消")
            .build()
            .showNow()
    }

    /**
     * 删除子类
     * @param activity FragmentActivity
     * @param minorCategory MinorCategory
     */
    fun deleteMinorCategoryWithDialog(activity: FragmentActivity, minorCategory: MinorCategory) {
        CustomDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("删除子类")
            .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
            .setClosedButtonEnable(false)
            .setPositiveButton("确定") { dialog, _ ->
                deleteMinorCategory(minorCategory)
                dialog.dismiss()
            }
            .setNegativeButton("取消")
            .build()
            .showNow()
    }

    /**
     * 添加主类弹窗
     * @param activity fragmentActivity
     * @param recordType 收入/支出
     */
    fun addCategory(activity: FragmentActivity, recordType: Int) {
        CustomDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("添加主类")
            .setContentView {
                LayoutAddCategoryBinding.inflate(activity.layoutInflater)
            }
            .setPositiveButton("确定") { dialog, binding ->
                binding as LayoutAddCategoryBinding
                val text = binding.etCategoryName.text?.toString()?.trim()
                if (text.isNullOrEmpty()) {
                    binding.etCategoryName.setBackgroundColor(Color.parseColor("#A4FFD1D1"))
                    return@setPositiveButton
                }
                viewModelScope.launch {
                    kotlin.runCatching {
                        insertCategory(MajorCategory(name = binding.etCategoryName.text.toString(), recordType = recordType))
                    }.onSuccess {
                        ToastUtil.showSuccess("添加成功")
                        dialog.dismiss()
                    }.onFailure {
                        if (it is SQLiteConstraintException) {
                            ToastUtil.showFail("主类名称重复,换一个试试吧~")
                        } else {
                            throw it
                        }
                    }
                }
            }
            .build()
            .showNow()
    }

    /**
     * 添加子类
     * @param activity FragmentActivity
     * @param recordType 收入/支出
     */
    fun addSubCategory(activity: FragmentActivity, recordType: Int) {
        CustomDialog.Builder(activity)
            .setCancelable(false)
            .setTitle("添加子类")
            .setContentView {
                LayoutAddSubCategoryBinding.inflate(activity.layoutInflater).apply {
                    tvCategoryName.text = currentMajorCategory.value!!.name
                }
            }
            .setPositiveButton("确定") { dialog, binding ->
                binding as LayoutAddSubCategoryBinding
                val text = binding.etSubCategoryName.text?.toString()?.trim()
                if (text.isNullOrEmpty()) {
                    binding.etSubCategoryName.setBackgroundColor(Color.parseColor("#A4FFD1D1"))
                    return@setPositiveButton
                }
                viewModelScope.launch {
                    // 保证在添加子类时,已经选择了主类
                    kotlin.runCatching {
                        insertMinorCategory(
                            MinorCategory(
                                name = binding.etSubCategoryName.text.toString(),
                                majorCategoryId = currentMajorCategory.value!!.id!!,
                                recordType = recordType
                            )
                        )
                    }.onSuccess {
                        ToastUtil.showSuccess("添加成功")
                        dialog.dismiss()
                    }.onFailure {
                        if (it is SQLiteConstraintException) {
                            // FOREIGN KEY外键约束 唯一约束UNIQUE
                            if (it.message!!.contains("UNIQUE")) {
                                ToastUtil.showFail("子类名称重复,换一个试试吧~")
                            } else if (it.message!!.contains("FOREIGN")) {
                                ToastUtil.showFail("添加失败,主类不存在!")
                                dialog.dismiss()
                            }
                        } else {
                            ToastUtil.showFail("添加失败")
                        }
                    }
                }
            }
            .build()
            .showNow()
    }
}