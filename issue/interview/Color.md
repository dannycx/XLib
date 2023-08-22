# Color处理

## int color转换为字符串
* 适配于js中使用
```
/**
 * int Color 转String Color
 *
 * 例：0XCE042C 转换为 #CE042C
 */
fun colorIntToHex(color: Int): String {
    // val color: Int = 0XCE042C
    val sb = StringBuilder()
    sb.append("#")
    sb.append(Integer.toHexString(Color.red(color)))
    sb.append(Integer.toHexString(Color.green(color)))
    sb.append(Integer.toHexString(Color.blue(color)))
    return sb.toString()
}
```
