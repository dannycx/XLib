# WebView篇之问题记录

## AndroidRuntime: Caused by: java.lang.UnsupportedOperationException: For security reasons, WebView is not allowed in privileged processes
* 原因
1. android:sharedUserId="android.uid.system"申请app为系统应用
2. Android 8.0新增安全机制
3. 使用Hook技术对sProviderInstance赋值，就不会抛异常
```
# 源码
...
static WebViewFactoryProvider getProvider() {
    synchronized (sProviderLock) {
        if (sProviderInstance != null) return sProviderInstance;

        final int uid = android.os.Process.myUid();
        if (...) {
            throw new UnsupportedOperationException("For security reasons, WebView is not allowed in privileged processes")
        }

        ...
    }
}
...
```
* 解决方案
```
fun hookWebView() {
    val sdkInt = Build.VERSION.SDK_INT
    try {
        val factoryClass = Class.forName("android.webkit.WebViewFactory")
        val field = factoryClass.getDeclaredField("sProviderInstance")
        field.isAccessible = true
        var sProviderInstance = field[null]
        if (sProviderInstance != null) {
            i("sProviderInstance isn't null")
            return
        }
        val getProviderClassMethod: Method =
            if (sdkInt > 22) {
                factoryClass.getDeclaredMethod("getProviderClass")
            } else if (sdkInt == 22) {
                factoryClass.getDeclaredMethod("getFactoryClass")
            } else {
                i("Don't need to Hook WebView")
                return
            }
        getProviderClassMethod.isAccessible = true
        val factoryProviderClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
        val delegateClass = Class.forName("android.webkit.WebViewDelegate")
        val delegateConstructor = delegateClass.getDeclaredConstructor()
        delegateConstructor.isAccessible = true
        if (sdkInt < 26) {
            val providerConstructor = factoryProviderClass.getConstructor(delegateClass)
            if (providerConstructor != null) {
                providerConstructor.isAccessible = true
                sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance())
            }
        } else {
            val chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD")
            chromiumMethodName.isAccessible = true
            var chromiumMethodNameStr = chromiumMethodName[null] as String
            if (chromiumMethodNameStr == null) {
                chromiumMethodNameStr = "create"
            }
            val staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass)
            if (staticFactory != null) {
                sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance())
            }
        }
        if (sProviderInstance != null) {
            field["sProviderInstance"] = sProviderInstance
            i("Hook success!")
        } else {
            i("Hook failed!")
        }
    } catch (e: Throwable) {
        w("throwable = " + e.message)
    }
}
```
