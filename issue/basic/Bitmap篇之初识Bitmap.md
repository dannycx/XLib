# Bitmap篇之初识Bitmap
## Bitmap
* final类，不可继承
* 图形处理类
* 可获取图像信息
* 可对图像剪切、旋转、缩放、压缩
* 可保存为指定格式图像文件png、jpeg、webp

## 创建Bitmap
* Bitmap#createBitmap()
[createBitmap]()
* BitmapFactory#decodeXxx()
[decodeXxx]()

## Bitmap颜色配置信息
**Config**
* ALPHA_8：颜色只有透明度组成，占8位
* ARGB_4444：颜色有ARGB组成，每部分占4位，共16位，图片不清晰
* ARGB_8888：颜色有ARGB组成，每部分占8位，共32位，默认颜色配置，最占空间
* RGB_565：颜色有R(5位)G(6位)B(5位)组成，共16位，优化选择方案

## Bitmap压缩方式
**CompressFormat**
* JPEG：以JPEG压缩算法进行图像压缩，格式为jpg或jpeg，有损压缩
* PNG：以PNG压缩算法进行图像压缩，格式为png，无损压缩
* WEBP：以WEBP压缩算法进行图像压缩，格式为webp，有损压缩
* 质量相同情况下，webp比jpeg小40%
* webp编码时间比jpeg长8倍

## Bitmap操作
### 裁剪
1. Bitmap#createBitmap(bitmap, x, y, w, h)
* 从源bitmap中裁剪
* x + w < bitmap.width && y + h < bitmap.height
2. Bitmap#createBitmap(bitmap, x, y, w, h, matrix, filter)
* 矩阵可进行缩放、旋转、移动
* filter为true表示bitmap会被过滤（仅当matrix包含移动+其他操作时适用）

### 缩放、旋转、移动、倾斜
1. Bitmap#createBitmap(bitmap, x, y, w, h, matrix, filter)
* 倾斜Matrix#postSkew不可在创建bitmap时使用
```
val matrix = Matrix()

// 缩放
matrix.postScale(0.8f, 0.9f)

// 左旋转45°
matrix.postRotate(-45)

// 移动
matrix.postTranslate(100, 80)
val bitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
```

### 保存和释放
* [BitmapTool#saveBitmap]()

## BitmapFactory#Options操作图像
[Options参数]()
* inBitmap：设置会将生成的图像内容加载至bitmap中
* inDensity：对bitmap设置密度，默认为true，若inDensity与inTargetDensity不匹配，bitmap返回前会将其缩放匹配inTargetDensity
* inTargetDensity：绘制目标bitmap上的密度
* inDither：是否做抖动处理，默认false
* inJustDecodeBounds：设为true表示获取bitmap信息，但不将其像素加载至内存
* inPreferredConfig：bitmap颜色配置信息，默认Bitmap.Config.ARGB_8888
* inSampleSize：压缩图像，值一般设为2的整数次幂，当设为2，宽高为原来1/2，图像大小为原来1/4
* inScaled：是否缩放
* outHeight：bitmap高度
* outWidth：bitmap宽度

## 优化
1. 压缩
[高效压缩]()

2. 缓存
* 内存缓存LruCache
* 磁盘缓存DiskLruCache
