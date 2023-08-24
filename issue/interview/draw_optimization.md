# 绘制优化

## 绘制原理
* view绘制主要有三个步骤：measure、layout、draw。运行在应用程序应用框架层，而真正将数据渲染到屏幕上是依赖 Native 层的 SurfaceFlinger 服务来完成。
* 绘制过程主要由CPU来完成measure、layout、record、execute的数据计算工作 和 GPU负责栅格化、渲染。
* CPU 和 GPU 通过图形驱动层进行连接，图形驱动层维护一个队列，CPU负责将display list添加到该队列，GPU负责从队列中取出数据进行绘制。

## 卡顿原因：
* 布局过于复杂，16ms 内无法完成渲染
* 同一时间动画执行次数过多，导致CPU或GPU负载过重
* view过度绘制，导致某些像素同一帧绘制多次
* UI线程耗时操作
* GC回收时暂停时间过长 或 频繁GC产生大量暂停时间

