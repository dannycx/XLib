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
