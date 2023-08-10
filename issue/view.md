# View

## Fragment刷新
* 回退任务栈，页面刷新，需延迟刷新
```
view.postDelayed({
    val curFragment = supportFragmentManager.findFragmentById(R.id.contains_fragment)
    if (curFragment is XxxFragment) {
        curFragment.refresh()
    }
}, Constants.DELAY_100)
```

## ImageView背景设置-tint属性
```
1. drawable设置
 Drawable stroke = ContextCompat.getDrawable(context, R.drawable.tint_drawable)
DrawableCompat.setTint(stroke, ContextCompat.getColor(context, R.color.tint_color))
image.setImageDrawable(stroke)

2. tint设置
image.setImageResource(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_tint)))
```

## Path是否有交集
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
```
