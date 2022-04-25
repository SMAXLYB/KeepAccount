# 简介
KeepAccounts目前是一款个人练手项目，功能以记账为主。主开发语言是kotlin，采用MVVM Jetpack架构。
### 一、工程结构
- buildSrc

  依赖版本控制

- app

  壳，没有实际业务代码，展示启动页

- module-common

  公共模块, 包含base/util

- module-home

  主页模块

- module-search

  搜索模块

- library-view

  自定view或者第三方控件

### 二、第三方库以及资料
- [Material design](https://material.io/design/)
  
  字体：
  正文普通 16sp
  正文小体 14sp
  按钮    14sp
  副标题   16sp
  标题    20sp 
  
  形状:
  圆角按钮 MaterialButton
  圆角图片 ShapeableImageView
  圆角卡片 MaterialShapeDrawable
  
  圆角:
  小: 4dp
  中: 8dp
  大: 12dp
  
- 主题切换

  [App为了漂亮脸蛋也要美颜，Theme 与 Style 的使用，附一键变装 demo](https://juejin.cn/post/6844904200673968141#heading-28)
  
  [Android 夜间模式切换，颜色渐变效果实现](https://blog.csdn.net/tyzlmjj/article/details/49255019)

  [Telegram](https://github.com/DrKLO/Telegram)

  [AndroidBaseFrameMVVM](https://github.com/Quyunshuo/AndroidBaseFrameMVVM)
  
  在组件化中application生命周期分发的处理上，参考了此库的的方法，使用SPI来暴露服务，用autoService自动完成一些模版工作，比起手动扫描然后收集来的方便的多，感谢作者提供了一个非常好的思路。

  [JetPack](https://developer.android.com/jetpack)
  - DataBinding 任何普通变量都可以直接进行绑定,但是必须调用binding.xxx=value才会变化,如果要使得改变原有变量也能改变ui,那么就要使用可观察对象.

### 三、代码提交类型
- [feat]增加新功能
- [fix]修复bug
- [docs]增加文档
- [style]代码格式化
- [build]构建工具相关
- [refactor]重构
- [revert]git revert时自动生成
- [test]测试
  
- [命名规范](https://cloud.tencent.com/developer/article/1408620)
