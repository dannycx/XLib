# 三方框架使用

## Glide使用（GlideApp）
1.build.gradle配置
```
plugins {
    ...
    id 'kotlin-kapt'
}

dependencies {
    ...

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.13.0'
    kapt 'com.github.bumptech.glide:compiler:4.13.0'
}
```

2.继承AppGlideModule
```
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MyAppGlideModule: AppGlideModule() {
}
```

3.rebuild生成GlideApp类

4.使用
```
val options = RequestOptions().centerCrop()
    .placeholder(R.color.color_ffffff)
    .error(R.color.color_ffffff)
    .transform(object : Transformation<Bitmap>{
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            TODO("Not yet implemented")
        }

        override fun transform(
            context: Context,
            resource: Resource<Bitmap>,
            outWidth: Int,
            outHeight: Int): Resource<Bitmap> {
            TODO("Not yet implemented")
        }
    }).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
GlideApp.with(context).load(url).apply(options).into(iv)
    
或
    
val target: FutureTarget<Bitmap>? = GlideApp.with(context)
    .asBitmap()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .downsample(DownsampleStrategy.CENTER_INSIDE)
    .load(uri)
    .submit(mBitmapWidth, mBitmapHeight)
val bitmap = mTarget?.get()
iv.setImageBitmap(bitmap)
```

