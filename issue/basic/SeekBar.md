# SeekBar

## 使用
```
1.layout
<SeekBar
    android:id="@+id/seek_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:max="100"
    android:progress="60"
    android:minHeight="4dp"
    android:maxHeight="4dp"
    android:layout_marginStart="-20dp"
    android:layout_marginEnd="-20dp"
    android:thumb="@drawable/thumb"
    android:progressDrawable="@drawable/progress"
    app:layout_constraintTop_toBottomOf="@id/xxx"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>

2.thumb权柄
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#FFFFFF"/>
    <size android:height="10dp"
        android:width="10dp"/>
</shape>

3.progress
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@android:id/background">
        <shape>
            <corners android:radius="3dp"/>
            <solid android:color="#5F5F5F"/>
        </shape>
    </item>

    <item
        android:id="@android:id/progress">
        <clip>
            <shape>
                <corners android:radius="3dp"/>
                <gradient
                    android:angle="0"
                    android:endColor="#77D5E5"
                    android:startColor="#77D5E5"/>
            </shape>
        </clip>
    </item>
</layer-list>
```

## 问题：比实际宽度短
* 设置margin负值
    * android:layout_marginEnd="-20dp"
