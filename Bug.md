#待修复Bug
 - [Record] 当年月日不为当前年月日时,时间点可以自由选择
 - 屏幕旋转,数据丢失
 - [Index] 在当前月份,新增其他月份记录,会刷新界面,且刷新的记录是1月份
 - [Index] 下拉刷新,收入支出要跟着刷新 liveData
 - 修复BigDecimal精度问题(已修复)
 - 主类切换后,子类选择清空
 
#待完善
 - [Index] 可以分类(支出/收入)查看
 - [Index] 列表展示每日收支
 - [Index] 记住上次选择时间
 - [Index] 列表Icon根据收支大小变化
 - [Analyze] MarkerView显示时间
 - 添加是否主动刷新功能
 - [Category] 完善退出删除模式
 - [Record] 新增标签
 - [Record] 新增附件

#待优化
 - [Record] 消费类型可以自定义
 - [Record] 金额限制小数位数
 - [Record] 输入备注时不遮挡内容
 - [Index, Analyze] 数据使用LiveData而不是MediatorLiveData
 - [Analyze] 折线图高度动态变化
 - [Index] 选择查看类型,当前类型高亮
 - 捕获必要的exception
 - [Book] 优化新建账本按钮
 - [Record] 优化界面,可以选择账本
 - [Analyze] 可以选择账本查看
 - 删除提示可以自定义
 - Date改用Long
 - 将收支类型放入entity中
 - [Record]账单详情可以切换