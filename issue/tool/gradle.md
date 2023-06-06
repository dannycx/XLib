# gradle问题

## java.lang.ClassNotFoundException: Didn't find class "androidx.databinding.DataBinderMapperImpl"
* 依赖lib使用dataBinding，主module未使用，修改如下：
```
buildFeatures {
    dataBinding true
}
```
