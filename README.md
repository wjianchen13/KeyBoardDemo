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


# 遇到问题
使用BaseActivity，适配android 15边到边之后，如果根布局添加了android:layout_gravity="center_vertical"，会导致调整
位置之后，会往上移动一段距离



# 参考文档
Android-软键盘一招搞定(实践篇)
https://code84.com/751589.html

Android中Activity的windowSoftInputMode属性详解
https://blog.csdn.net/qq_35381515/article/details/122078554




































