# Color处理

### color资源获取
```
val color = ContextCompat.getColor(this,R.color.color_id)

val colorId = resources.getIdentifier("color_id", "color", context.packageName)
```

### color定义
```
使用上color1等价于color2，建议使用int值，占用空间小效率高

val color1: Int = 0xFE031D
val color2: String = "#FE031D"
val color3 = Color.argb(10, 23, 67, 8)
```

### #FE031D -> int
```
val color = Color.parseColor("#FE031D");
```

### color转ARGB十进制
```
方式一：
val color = 0xFE031D;
val sb = StringBuilder();
sb.append(Color.alpha(color) + ",");
sb.append(Color.red(color) + ",");
sb.append(Color.green(color) + ",");
sb.append(Color.blue(color) + ",");
Log.e("ARGB: ", sb.toString());
// 输出结果是：0，254,3,29

方式二：
val color = 0xFE031D;
val a = ((color >> 24) and 0xff);
val r = ((color >> 16) and 0xff);
val g = ((color >> 8) and 0xff);
val b = ((color) and 0xff);
// 输出结果是：0，254,3,29
```

### int -> #FE031D
* 适配于js中使用
```
/**
 * int Color 转String Color
 *
 * 例：0XFE031D 转换为 #FE031D
 * 注意：0x003443 需特殊处理，补0
 */
fun colorIntToHex(color: Int): String {
    val sb = StringBuilder()
    sb.append("#")
    var red = Integer.toHexString(Color.red(color))
    if (red.length == 1) {
        red = "0$red"
    }
    sb.append(red)
    var green = Integer.toHexString(Color.green(color))
    if (green.length == 1) {
        green = "0$green"
    }
    sb.append(green)
    var blue = Integer.toHexString(Color.blue(color))
    if (blue.length == 1) {
        blue = "0$blue"
    }
    sb.append(blue)
    return sb.toString()
}
```
