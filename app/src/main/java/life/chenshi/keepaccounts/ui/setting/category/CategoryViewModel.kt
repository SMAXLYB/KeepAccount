package life.chenshi.keepaccounts.ui.setting.category

import androidx.lifecycle.*
import kotlinx.coroutines.*
import life.chenshi.keepaccounts.constant.STATE_DELETE
import life.chenshi.keepaccounts.constant.STATE_NORMAL
import life.chenshi.keepaccounts.database.AppDatabase
import life.chenshi.keepaccounts.database.entity.Category
import life.chenshi.keepaccounts.database.entity.SubCategory

class CategoryViewModel : ViewModel() {
    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val categoryDao = AppDatabase.getDatabase().getCategoryDao()
    private val subCategoryDao = AppDatabase.getDatabase().getSubCategoryDao()

    // 当前选中主类
    val currentCategory = MutableLiveData<Category>()
    lateinit var categories: LiveData<List<Category>>

    // 当前选中子类
    val currentSubCategory = MutableLiveData<SubCategory>()
    var subCategories = MediatorLiveData<List<SubCategory>>()
    private var tempSubCategories: LiveData<List<SubCategory>>? = null

    // 当前删除模式
    val isDeleteMode = MutableLiveData<Boolean>(false)

    init {
        getAllCategory()
    }

    suspend fun insertCategory(category: Category) = withContext(Dispatchers.IO) {
        val existCategory = categoryDao.getCategoryBy(category.name)
        // 如果已经存在并且为-1,重新标记0, 否则走正常流程
        existCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            categoryDao.updateCategory(this)
            return@withContext
        }
        categoryDao.insertCategory(category)
    }

    /**
     * 删除主类, 连带删除当下所有子类
     */
    suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) {
        awaitAll(
            async {
                categoryDao.deleteCategory(category.id!!)
                // 如果删除了选中, 将当选选中清空
                currentCategory.value?.let {
                    if (it.id == category.id) {
                        currentCategory.postValue(null)
                    }
                }
            },
            async {
                // 连带删除当下所有子类
                subCategoryDao.deleteAllSubCategoryBy(category.id!!)
                // 如//果包含选中子类,还要置空
                currentSubCategory.value?.takeIf {
                    it.categoryId == category.id
                }?.run {
                    currentSubCategory.postValue(null)
                }
            }
        )
    }

    /**
     * 删除子类
     */
    suspend fun deleteSubCategory(subCategory: SubCategory) {
        subCategoryDao.deleteSubCategoryBy(subCategory.id!!)
        // 如果删除了选中, 将选中清空
        currentSubCategory.value?.let {
            if (it.id == subCategory.id) {
                currentSubCategory.value = null
            }
        }
    }

    suspend fun insertSubCategory(subCategory: SubCategory) = withContext(Dispatchers.IO) {
        val existSubCategory =
            subCategoryDao.getSubCategoryBy(subCategory.categoryId, subCategory.name)
        existSubCategory?.takeIf { it.state == STATE_DELETE }?.run {
            this.state = STATE_NORMAL
            subCategoryDao.updateSubCategory(this)
            return@withContext
        }
        subCategoryDao.insertSubCategory(subCategory)
    }


    private fun getAllCategory() {
        categories = categoryDao.getAllCategoryBy(STATE_NORMAL).asLiveData()
    }

    fun getAllSubCategoryByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            if (tempSubCategories != null) {
                subCategories.removeSource(tempSubCategories!!)
            }
            tempSubCategories =
                subCategoryDao.getALLSubCategoryBy(categoryId, STATE_NORMAL).asLiveData()
            subCategories.addSource(tempSubCategories!!) {
                subCategories.value = it
            }
        }
    }

    /**
     * 获取主类在adapter中的位置
     */
    suspend fun getCurrentCategoryInAdapterPosition(category: Category) =
        withContext(Dispatchers.Default) {
            categories.value?.asSequence()?.forEachIndexed { index, c ->
                if (category.id == c.id) {
                    return@withContext index
                }
            }
            return@withContext null
        }

    /**
     * 获取子类在adapter中的位置
     */
    suspend fun getCurrentSubCategoryInAdapterPosition(subCategory: SubCategory) =
        withContext(Dispatchers.Default) {
            subCategories.value?.asSequence()?.forEachIndexed { index, s ->
                if (subCategory.id == s.id) {
                    return@withContext index
                }
            }
            return@withContext null
        }
}