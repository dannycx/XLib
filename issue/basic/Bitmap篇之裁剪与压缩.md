# Bitmap篇之裁剪与压缩
## 裁剪
**ImageView的ScaleType**
* matrix：不缩放，图片与控件左上角对齐，超出裁剪
* center：不缩放，图片与控件中心点对齐，超出裁剪
* centerInside：完整显示图片，不裁剪，显示不下缩放，显示的下不缩放
* centerCrop：填满控件显示，等比缩放，超出裁剪
* fitCenter(默认)：自适应，不裁剪，不超出控件，等比缩放至最大，居中显示
* fitStart：自适应，不裁剪，不超出控件，等比缩放至最大，左上显示
* fitEnd：自适应，不裁剪，不超出控件，等比缩放至最大，右下显示
* fitXY：填满控件显示，不按比例缩放(会变形)，不裁剪

## 压缩
**图片质量**
* ARGB_8888：32位位图，带透明度，每个像素占4字节
* ARGB_4444：16位位图，带透明度，每个像素占2字节
* RGB_565：16位位图，不带透明度，每个像素占2字节
* ALPHA_8：32位位图，只有透明度，无颜色，每个像素占4字节

**Bitmap压缩**
1. 质量压缩
* Bitmap#compress()在保持像素前提下改变图片位深及透明度
* bitmap大小不变
* bytes.length随quality变小而变小

2. 采样率压缩
* 改变inSampleSize值降低内存占用

3.  缩放法压缩
* Matrix对图像进行缩放、旋转、平移、斜切等变换
```
val matrix = Matrix()
matrix.setScale(.25f, .25f)
val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
```
