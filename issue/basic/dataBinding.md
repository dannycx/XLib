# DataBinding

## 引入
```
android {
    ...
    dataBinding {
        enabled = true
    }
}
```

## ViewStub
```
1.view_stub_layout.xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <FrameLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <ViewStub
            android:id="@+id/view_stub_test"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout="@layout/view_stub_test"/>
    </FrameLayout>
</layout>

2.view_stub_test.xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="48dp"
        android:gravity="center_vertical"
        android:paddingStart="10dp"
        android:paddingEnd="10dp"
        android:paddingTop="8dp"
        android:paddingBottom="8dp"
        android:background="@drawable/vector_xxx_bg">

        <ImageView
            android:id="@+id/xxx"
            android:layout_width="32dp"
            android:layout_height="32dp"
            android:padding="4dp"
            android:src="@drawable/vector_xxx"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
    </LinearLayout>
</layout>

3.ViewStubLayout.kt
class ViewStubLayout(context: Context, attrs: AttributeSet? = null, defAttr: Int = 0, defRes: Int = 0)
    : FrameLayout(context, attrs, defAttr, defRes) {
    constructor(context: Context, attrs: AttributeSet? = null): this(context, attrs, 0)

    private lateinit var mViewStubBinding: ViewStubTestBinding
    private var mListener: TableToolListener? = null
    private val mBinding: ViewStubLayoutBinding by lazy {
        ViewStubLayoutBinding.inflate(LayoutInflater.from(context), this, false)
    }

    init {
        removeAllViews()
        addView(mBinding.root)

        if (!mBinding.viewStubTest.isInflated) {
                mBinding.viewStubTest.viewStub?.inflate()?.let {
                    mViewStubBinding = DataBindingUtil.bind<ViewStubTestBinding>(it)!!
                }
            }
            if (::mViewStubBinding.isInitialized) {
                mViewStubBinding.xxx.setOnClickListener {
                    callback(XXX)
                }
            }
    }

    private fun callback(tableTool: Int) {
        mListener?.toolSelect(tableTool)
    }

    fun measureHeight() {
        layoutParams?.height = ResourcesUtil.getDimension(R.dimen.dp_48)
    }

    fun setListener(listener: TableToolListener) {
        mListener = listener
    }

    /**
     * 回调接口
     */
    interface XListener {
        /**
         * 工具类型选择
         */
        fun toolSelect(eventType: Int)
    }
}

```
