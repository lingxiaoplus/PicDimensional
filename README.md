# PicDimensional
picture dimensional

主要用到的框架是现在流行的retrofit+rxjava+rxandroid

### 项目分包

![](http://oqz3bypff.bkt.clouddn.com/picdimensional_package.png)

项目分为四个模块：

1. 第一个common包里对网络请求进行了封装，还有一些base类和工具类
2. 第二个包对语言进行封装，做国际化
3. 第三个sjinlibrary是皮肤包，对换肤框架的实现
4. 第四个是主项目，在里面实现相应的功能

#### V1.0

1.图片查看和下载

2.抓包安卓壁纸的图片进行开发

3.首页轮播图

#### V1.1

1.启动页面增加开关，如果不想显示可以关闭

2.下载的图片可以直接在应用内打开啦

3.更新了一下源，有些图片更好看啦

4.图片详情界面可以分享了

#### V1.2

1.修复图片下载失败的几率

2.修复查看部分图片卡顿问题

3.更新了一下源，有些图片更好看啦

4.增加图片搜索功能

5.可以对下载好的图片做出一些py操作

6.修改应用图标

#### V1.3
1.首页图片添加悬浮按钮,一键回到顶部

2.增加了menu侧滑菜单,内容比较少,以后会添加的

3.图片滑动添加动画,更加好看啦

4.添加评论,可以看见图片的评论了

5.添加了手机壁纸界面,里面的一些福利需要大家自己发现

6.优化图片展示页面的布局

7.添加百度识图

8.修复部分bug

#### V2.0.2

1. 使用rxjava2+retrofit2+mvp重构应用
2. 加入换肤支持
3. 搜索接口优化
4. 增加阿里云热修复
5. 组件化开发

## 用到的开源库
强大的图片加载框架   [fresco](https://www.fresco-cn.org/docs/getting-started.html)

android轮播图控件    [banner](https://github.com/youth5201314/banner)

图片放大缩小手势控件  [photoview](https://github.com/chrisbanes/PhotoView)

强大的注解框架       [butterknife](https://github.com/JakeWharton/butterknife)

网络请求框架          [retrofit](https://github.com/square/retrofit)
