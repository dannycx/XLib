# Bitmap篇之Mutable
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
