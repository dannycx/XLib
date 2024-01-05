# Bitmap篇之Mutable
* 使用immutable bitmap创建Canvas会抛出IllegalStateException
* Bitmap#createBitmap(w: Int, h: Int, config: Config)该方法返回Mutable Bitmap
* [immutable to mutable bitmap]()
```
val bitmap = Bitmap.createBitmap(srcBitmap)
val canvas = Canvas(bitmap)

# Canvas.class
constructor(bitmap: Bitmap) {
    if (!bitmap.isMutable()) {
        throw IllegalStateException("Immutable bitmap passed to Canvas constructor")
    }
    throwIfCannotDraw(bitmap)
    ...
}
```
