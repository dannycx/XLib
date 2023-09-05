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
