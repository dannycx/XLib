# ANR + 内存泄漏

## 非静态内部类使用
> 持有外部类引用
```
class TestActivity : AppCompatActivity() {
    private inner class TestThread : Thread() {
        override fun run() {
            // 任务
        }
    }
}
```
* 静态内部类
* 独立类 + 弱引用

## Handlers或Runnables
> 例：handler使用，activity关闭，handler消息未执行完毕，内部类持有外部类无法回收。
```
class TestActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { /* 任务 */ }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
```
* onDestroy移除所有回调

## 匿名监听器保持对activity或view引用
```
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.view).setOnClickListener {
            // click事件持有activity对象
        }
    }
}
```
* onDestroy清除监听器或使用静态类

## 静态视图或上下文引用
```
class TestActivity : AppCompatActivity() {
    companion object {
        private var staticView: View? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        staticView = findViewById(R.id.view)
    }
}
```
* 避免对视图或上下文使用静态引用，如果有必要，可以使用弱引用。

## LiveData使用不当
> 观察LiveData时没有考虑生命周期
```
class TestActivity : AppCompatActivity() {
    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.testLiveData.observe(this, { data ->
            /* 更新UI */
        })
    }
}
```
* 使用生命周期感知的组件来观察LiveData，例如在Fragment中使用viewLifecycleOwner

## 单例持有上下文对象
```
object TestSingleton {
    var context: Context? = null
}
```
* 改为application上下文

## bitmaps占用资源
```
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.large_image)
        // 使用位图
    }
}
```
* 谨慎使用，使用后回收recycle(),利用图形加载库Glide或Picasso或Coil

## webview上下文引用
```
class TestActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        webView = findViewById(R.id.web_view)
        // 设置WebView
    }
}
```
* 各生命周期调用webview相关处理，onDestroy清除webview，使用application上下文

## RecyclerView适配器中的事件监听器
> RecyclerView适配器中的事件监听器可能会对Activity或Fragment保持引用
```
class TestAdapter(private val items: List<Item>, private val activity: AppCompatActivity) : RecyclerView.Adapter<Holder>() {
    // 适配器实现
}
```
* 使用回调或lambda,避免activity或fragment上下文传给adapter

## 资源未释放
```
class TestActivity : AppCompatActivity() {
    private val receiver = TestReceiver()
    ...

    override fun onStart() {
        registerReceiver(receiver, IntentFilter("Test_ACTION"))
        ...
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        ...
        super.onStop()
    }
}
```
* 广播注册 + 反注册
* 服务开启 + 关闭
* 服务绑定 + 解绑


