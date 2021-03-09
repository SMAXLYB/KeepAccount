package life.chenshi.keepaccounts.ui.setting.category

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import life.chenshi.keepaccounts.common.utils.DataStoreUtil
import life.chenshi.keepaccounts.constant.STATE_DELETE
import life.chenshi.keepaccounts.constant.STATE_NORMAL
import life.chenshi.keepaccounts.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.MajorCategory
import life.chenshi.keepaccounts.database.entity.MinorCategory

class CategoryViewModel : ViewModel() {
    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val categoryDao = AppDatabase.getDatabase().getCategoryDao()
    private val subCategoryDao = AppDatabase.getDatabase().getSubCategoryDao()

    // 当前选中主类
    val currentCategory = MutableLiveData<MajorCategory>()
    lateinit var categories: LiveData<List<MajorCategory>>

    // 当前选中子类
    val currentSubCategory = MutableLiveData<MinorCategory>()
    var subCategories = MediatorLiveData<List<MinorCategory>>()
    private var tempMinorCategories: LiveData<List<MinorCategory>>? = null

    // 当前删除模式
    val isDeleteMode = MutableLiveData<Boolean>(false)

    init {
        getAllCategory()
    }

    suspend fun insertCategory(majorCategory: MajorCategory) = withContext(Dispatchers.IO) {
        val existCategory = categoryDao.getMajorCategoryBy(majorCategory.name)
        // 如果已经存在并且为-1,重新标记0, 否则走正常流程
        existCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            categoryDao.updateMajorCategory(this)
            return@withContext
        }
        categoryDao.insertMajorCategory(majorCategory)
    }

    /**
     * 删除主类, 连带删除当下所有子类
     */
    suspend fun deleteCategory(majorCategory: MajorCategory) = withContext(Dispatchers.IO) {
        awaitAll(
            async {
                categoryDao.deleteMajorCategory(majorCategory.id!!)
                // 如果删除了选中, 将当选选中清空
                currentCategory.value?.let {
                    if (it.id == majorCategory.id) {
                        currentCategory.postValue(null)
                    }
                }
            },
            async {
                // 连带删除当下所有子类
                subCategoryDao.deleteAllMinorCategoryBy(majorCategory.id!!)
                // 如//果包含选中子类,还要置空
                currentSubCategory.value?.takeIf {
                    it.majorCategoryId == majorCategory.id
                }?.run {
                    currentSubCategory.postValue(null)
                }
            }
        )
    }

    /**
     * 删除子类
     */
    suspend fun deleteSubCategory(minorCategory: MinorCategory) {
        subCategoryDao.deleteMinorCategoryBy(minorCategory.id!!)
        // 如果删除了选中, 将选中清空
        currentSubCategory.value?.let {
            if (it.id == minorCategory.id) {
                currentSubCategory.value = null
            }
        }
    }

    suspend fun insertSubCategory(minorCategory: MinorCategory) = withContext(Dispatchers.IO) {
        val existSubCategory =
            subCategoryDao.getMinorCategoryBy(minorCategory.majorCategoryId, minorCategory.name)
        existSubCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            subCategoryDao.updateMinorCategory(this)
            return@withContext
        }
        subCategoryDao.insertMinorCategory(minorCategory)
    }


    private fun getAllCategory() {
        categories = categoryDao.getAllMajorCategoryBy(STATE_NORMAL).asLiveData()
    }

    fun getAllSubCategoryByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            if (tempMinorCategories != null) {
                subCategories.removeSource(tempMinorCategories!!)
            }
            tempMinorCategories =
                subCategoryDao.getALLMinorCategoryBy(categoryId, STATE_NORMAL).asLiveData()
            subCategories.addSource(tempMinorCategories!!) {
                subCategories.value = it
            }
        }
    }

    /**
     * 获取主类在adapter中的位置
     */
    suspend fun getCurrentCategoryInAdapterPosition(majorCategory: MajorCategory) =
        withContext(Dispatchers.Default) {
            categories.value?.asSequence()?.forEachIndexed { index, c ->
                if (majorCategory.id == c.id) {
                    return@withContext index
                }
            }
            return@withContext null
        }

    /**
     * 获取子类在adapter中的位置
     */
    suspend fun getCurrentSubCategoryInAdapterPosition(minorCategory: MinorCategory) =
        withContext(Dispatchers.Default) {
            subCategories.value?.asSequence()?.forEachIndexed { index, s ->
                if (minorCategory.id == s.id) {
                    return@withContext index
                }
            }
            return@withContext null
        }

    fun confirmBeforeDelete(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            DataStoreUtil.readFromDataStore(SWITCHER_CONFIRM_BEFORE_DELETE, true)
                .take(1)
                .collect {
                    callback.invoke(it)
                }
        }
    }
}