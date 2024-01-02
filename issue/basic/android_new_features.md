# Android新特性

## Android P
### Android P及以上非公开api访问
1. 将自己类的classLoader转换为系统的classLoader去调用系统非公开api
2. 借助系统类方法去调用系统非公开api，即双反射机制
3. 三方库解除系统api访问限制(implementation 'com.github.tiann:FreeReflection:3.1.0')

4. 
