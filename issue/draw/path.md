# Path

## 两条Path是否有交集
```
public boolean checkInterEraser(Path path1, Path path2) {
    Region clip = new Region(0, 0, Util.INSTANCE.getWindowWH()[0], Util.INSTANCE.getWindowWH()[1]);
    Region eRegion = new Region();
    eRegion.setPath(path1, clip);
    Region sRegion = new Region();
    sRegion.setPath(path, clip);
    if (!eRegion.quickReject(sRegion) && eRegion.op(sRegion, Region.Op.INTERSECT)) {
        return true;
    }
    return false;
}

public Path getPath() {
    if (MainUtils.INSTANCE.isListEmpty(points)) {
        return null;
    }
    final int size = points.size();
    Path tempPath = new Path();
    tempPath.moveTo(points.get(0).x, points.get(0).y);

    PointF preP;
    PointF curP;
    for (int index = 0; index < size - 1; index++) {
        preP = points.get(index);
        curP = points.get(index + 1);
        tempPath.quadTo(preP.x, preP.y, (preP.x + curP.x) / 2, (preP.y + curP.y) / 2);
    }
    return tempPath;
}
```

**PathMeasure实现路径动画**
* PathMeasure可计算出指定路径信息：路径总长度、指定长度对应坐标
```
// 方式一
val pathMeasure = PathMeasure()
val path = Path()
// forceClosed：true，强制闭合计算终点到起点的距离，forceClosed仅对测量有影响，不会影响关联的path
pathMeasure.setPath(path, true)

// 方式二
val path = Path()
val pathMeasure = PathMeasure(path, true)
```
* 常用方法
```
// 获取路径长度：forceClosed：true强制闭合，true > false
fun getLength(): Float

// 是否闭合
fun isClosed(): Boolean

// path由多条曲线构成（addXxx()），用于跳转下一个线段，true：跳转成功
fun nextContour(): Boolean

// 线段截取：[startD, stopD] in [0, pathLength]，截取线段添加至dst
// startWithMoveTo：true新线段移动至dst起点，false新线段移动至dst终点(上一线段终点)
// 注意：使用该方法需禁用硬件加速setLayerType(View.LAYER_TYPE_SOFTWARE, null)
fun getSegment(startD: Float, stopD: Float, dst: Path, startWithMoveTo: Boolean)
```
* 路径实现加载动画

