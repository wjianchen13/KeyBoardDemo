# 软键盘相关使用

# 环境
android-studio_2024.3.2.15
gradle插件：8.10.1
gradle版本：gradle-8.11.1-bin
jdk 21.0.6


# 功能
## 项目软件盘弹出隐藏，消息列表布局层级修改

# windowSoftInputMode 
adjustPan 
根布局是ConstraintLayout时，包裹EditText的控件高度是 android:layout_height="match_parent"时，弹出软键盘，
界面整体上移不压缩

adjustResize 
根布局是ConstraintLayout时，包裹EditText的控件高度是 android:layout_height="match_parent"时，弹出软键盘，
界面整体上移压缩。
但是，如果把android:layout_height="600dp"时，弹出软键盘，界面整体上移不压缩

adjustPan
根布局是RelativeLayout时，包裹EditText的控件高度是 android:layout_height="match_parent"时，弹出软键盘，
界面整体上移不压缩

adjustResize
根布局是RelativeLayout时，包裹EditText的控件高度是 android:layout_height="match_parent"时，弹出软键盘，
界面整体上移压缩。
但是，如果把android:layout_height="700dp"时，弹出软键盘，界面整体上移压缩

由此可以得出结论，设置windowSoftInputMode，根布局不一样，也会导致软键盘弹出布局的表现形式，最明显的就是使用
ConstraintLayout作为根布局，如果子布局是固定的尺寸，弹出软键盘就不会压缩，使用RelativeLayout，即使子布局是
固定尺寸，弹出软键盘也会导致布局压缩。
也就是说，在ConstraintLayout里面，只要弹出软键盘上移的布局是一个固定的尺寸，而不是铺满父布局这种match_parent,
那弹出软键盘的时候，上移的布局并不会压缩，或者修改尺寸。

* H5页面软键盘弹出H5上移
* 如果不设置initEdgeToEdge和initEdgeToEdgePadding，android:windowSoftInputMode="adjustPan" ，点击H5的输入框
* 界面是可以整体上移的不压缩的，但是这个时候并没有边到边的效果
* 如果设置了initEdgeToEdge和initEdgeToEdgePadding，android:windowSoftInputMode="adjustPan"，点击H5输入框
* 界面上移并且会压缩
* BaseWindowInsetConstraintLayout 其实就是设置了上下边距，和initEdgeToEdgePadding()要做的事情差不多，都是
* 根据状态栏和导航栏的尺寸动态设置实际布局的边距
* 下面的例子实际上BaseWindowInsetConstraintLayout已经生效了，当设置initEdgeToEdge()时，已经是设置了边到边
* BaseWindowInsetHelper里面的defaultApplySystemWindowInsets21()方法设置了状态栏和导航栏的缩进。所以看到的
* 效果就是状态栏和导航栏都不会遮挡布局上的内容，然后点击H5上的输入框，界面也可以上移不压缩。
* 如果不使用BaseWindowInsetConstraintLayout，那么状态栏和导航栏都会遮挡布局内容，然后点击H5的输入框，整体
* 界面上移，并且不会压缩

BaseWindowInsetRelativeLayout的作用
20260109 
当Activity开启了边到边enableEdgeToEdge()，android:windowSoftInputMode="adjustResize"，并且没有设置下面的
代码：ViewCompat.setOnApplyWindowInsetsListener()，这时候界面上EditText点击弹出软键盘时，界面并不会压缩，
EditText也不会往上移动，也就是说当开启边到边时，adjustResize会失去作用。这个时候如果根布局套一个BaseWindowInsetRelativeLayout
界面就会压缩。
但是当设置了ViewCompat.setOnApplyWindowInsetsListener()时，BaseWindowInsetRelativeLayout会失去作用，他会以
ViewCompat.setOnApplyWindowInsetsListener()设置的监听器为准。



# 遇到问题
使用BaseActivity，适配android 15边到边之后，如果根布局添加了android:layout_gravity="center_vertical"，会导致调整
位置之后，会往上移动一段距离



# 参考文档
Android-软键盘一招搞定(实践篇)
https://code84.com/751589.html

Android中Activity的windowSoftInputMode属性详解
https://blog.csdn.net/qq_35381515/article/details/122078554




































