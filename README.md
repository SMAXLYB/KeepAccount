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
- [AndroidBaseFrameMVVM](https://github.com/Quyunshuo/AndroidBaseFrameMVVM)

  在组件化中application生命周期分发的处理上，参考了此库的的方法，使用SPI来暴露服务，用autoService自动完成一些模版工作，比起手动扫描然后收集来的方便的多，感谢作者提供了一个非常好的思路。

### 三、代码提交类型
- [feat]增加新功能
- [fix]修复bug
- [docs]增加文档
- [style]代码格式化
- [build]构建工具相关
- [refactor]重构
- [revert]git revert时自动生成
