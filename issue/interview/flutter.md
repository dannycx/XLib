# Flutter

[Flutter项目搭建](https://github.com/dannycx/XTools/blob/main/notes/flutter/flutter_one.md)

[Flutter问题记录](https://github.com/dannycx/XTools/blob/main/notes/flutter/flutter_two.md)

## 跨组件通信


## 显示隐藏
1. Visibility
```
Widget _page() {
    return Visibility(
        // 显示隐藏
        visible: _visible,
        // 隐藏时是否保持占位
        maintainState: false,
        // 隐藏时是否保存动态状态
        maintainAnimation: false,
        // 隐藏时是否保存子组件所占空间大小，不会消耗过多的性能
        maintainSize: false,
        child: Positioned(
          width: 100,
          height: 36,
          left: _x,
          top: height - 84,
          child: XxxPage(
            onXxxProp: (xType) {
              eventBus.fire(XxxEvent(xType: XType));
            },
          ),
        ));
  }
```
2. Offstage
```
// 控制组件显示隐藏，隐藏时不占位
Widget _page() {
    return Offstage(
      offstage: true,
      child: Text('show'),
    );
}
```
3. Opacity
```
// 通过透明度控制组件显示隐藏，0：完全透明（隐藏），1：完全不透明（显示）
Widget _page() {
    return Opacity(
      opacity: 0,
      child: Text('hide'),
    );
}
```






