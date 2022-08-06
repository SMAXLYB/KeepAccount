package life.chenshi.keepaccounts.module.common.constant

/**
 * 时间正序
 */
const val SORT_BY_DATE_ASC = 0;

/**
 * 时间倒序
 */
const val SORT_BY_DATE_DESC = 1;

/**
 * 金额正序
 */
const val SORT_BY_MONEY_ASC = 2;

/**
 * 金额倒序
 */
const val SORT_BY_MONEY_DESC = 3;

sealed class SortOption {
    object SortByTimeAsc : SortOption()
    object SortByTimeDesc : SortOption()
    object SortByMoneyAsc : SortOption()
    object SortByMoneyDesc : SortOption()
}

/**
 * 查看全部
 */
const val SHOW_TYPE_ALL = 0;

/**
 * 只看收入
 */
const val SHOW_TYPE_INCOME = 1;

/**
 * 只看支出
 */
const val SHOW_TYPE_OUTCOME = 2;