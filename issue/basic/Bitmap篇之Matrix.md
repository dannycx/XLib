# Bitmap篇之Matrix
## Matrix
* set：直接设置矩阵中数值
* pre：矩阵左乘
* post：矩阵右乘
* bitmap计算后，会重新生成一个bitmap
```
MSCALE_X   MSKEW_X    MTRANS_X
MSKEW_Y    MSCALE_Y   MTRANS_Y
MPERSP_0   MPERSP_1   MPERSP_2

缩放X 偏移X 平移X
偏移Y 缩放Y 平移Y
透视0 透视1 透视2
```
