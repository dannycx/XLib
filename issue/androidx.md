# Androidx适配

## androidx.lifecycle:lifecycle-extensions:2.2.0已被弃用。
1.弃用前初始化ViewModel的方法：
  // implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
  myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
2. 弃用后初始化ViewModel的方法：
  // implementation "androidx.lifecycle:lifecycle-viewmodel:2.5.1"
  ViewModelProvider(this)[MainViewModel::class.java]
