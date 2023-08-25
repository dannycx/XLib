# 绘制优化

## 绘制原理
* view绘制主要有三个步骤：measure、layout、draw。运行在应用程序应用框架层，而真正将数据渲染到屏幕上是依赖 Native 层的 SurfaceFlinger 服务来完成。
* 绘制过程主要由CPU来完成measure、layout、record、execute的数据计算工作 和 GPU负责栅格化、渲染。
* CPU 和 GPU 通过图形驱动层进行连接，图形驱动层维护一个队列，CPU负责将display list添加到该队列，GPU负责从队列中取出数据进行绘制。

## 卡顿原因
* 布局过于复杂，16ms 内无法完成渲染
* 同一时间动画执行次数过多，导致CPU或GPU负载过重
* view过度绘制，导致某些像素同一帧绘制多次
* UI线程耗时操作
* GC回收时暂停时间过长 或 频繁GC产生大量暂停时间

## 卡顿分析


## 优化方案
* 合理运用布局，减少层级嵌套
* include，布局复用
* merge，去除多余层级
* viewstub，按需显示，提高加载速度
    - Viewstub只能加载一次，加载后置空，不能嵌套merge，操作还需操作具体view
* 避免 GPU 过度绘制：
```
1. 在XML中存在控件重叠且都设有背景
2. view的ondraw在同一区域绘制多次

检查：开发者工具打开 GPU 过度绘制开关
白-蓝-绿-粉-红，颜色越浅越好
1. 移除不必要的background 
2. 自定义view的ondraw中使用canvas.clipRect指定区域绘制，防止重叠的组件过度绘制
```
