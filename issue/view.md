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
