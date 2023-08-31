# SwitchCompat

## 使用
```
1.layout
<androidx.appcompat.widget.SwitchCompat
    android:id="@+id/switch_bar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:checked="true"
    android:thumb="@drawable/selector_thumb"
    app:track="@drawable/selector_track"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintTop_toTopOf="@id/xxx"
    app:layout_constraintBottom_toBottomOf="@id/xxx"/>

2.track
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <size android:width="24dp"
                android:height="8dp"/>

            <solid android:color="#F9F9F9"/>

            <corners android:radius="8dp"/>
        </shape>
    </item>
</selector>

3.thumb权柄
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="oval">
            <size android:width="12dp"
                android:height="12dp"/>

            <solid android:color="#77D5E5"/>
        </shape>
    </item>
</selector>
```

## 问题：无法设置宽高
* 通过thumb和track解决
