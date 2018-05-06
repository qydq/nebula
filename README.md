Integrate
----

当前已经测试过的手机->

华为荣耀8 android7.0 API-24 KQN0116215000131

华为荣耀9 android7.0 API-24 8BN0117115000262

奇虎360 android5.1 API-22 8681-M02-0x56829d6a

takela

This is updata version from an-aw-base

# 使用软件必须登陆，在AdPageActivity 可以同步登陆信息。

an-aw-base系列框架在2017-12-09 00:20:05停止维护，但Integrate「nebula」正在对an-aw-base系列框架进行重构

这里记录一下快捷键
tip : 也可以统一整合mac 和windows快捷键为一样。

mac android studio 快捷键记录
------

快速格式化代码：command+option+L

快速倒包： option + return

快速移除不用包 ： option + control + o

android studio 选中『关键词』 command + shift + L 在浏览器中搜索



windows 快捷键记录
------

CTRL +G  快速定位到行数

切换繁体，简体：ctrl + shift +f （可能冲库android studio 查找快捷键）

Keymap 搜索 ，Editor Tabs下面的Close,设置它的快捷键为ctrl+w就好-关闭

在浏览器中，Ctrl + 数字切换到某某Tab

alt + insert 、设置get 和 set 方法

notepadd中选中代码 （Shift + ）Tab 可以向前向后缩进代码。


[sample.apk](https://github.com/qydq/Integrate/raw/chat/sample/sample-debug.apk)


**主页面动态是一个模板，卡片，符合观察者模式，想添加什么布局addLayout就添加任何布局。

Client ID:	YXA6gAJ00AveEeiJIWcOP36VyQ
Client Secret:	YXA6vxQon_tVi-qL3_DRzMze7u4VSL0


1.93 GB (2,076,716,311 字节)
1.99 GB (2,147,020,800 字节)
37,483 个文件，6,587 个文件夹
debug,APK size :69.7M
13号 51277
(a)
未签名：38628KB
签名：38744KB





**插件APK

（1）插件APK中加入如下代码，这样apk就以插件的形式存在。（或者droidplugin）

```
PackageManager packageManager = getPackageManager();
        /*隐藏应用图标*/
        packageManager.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.DONT_KILL_APP);
        /*显示应用图标*/
//        packageManager.setComponentEnabledSetting(getComponentName(),
//                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        /*
        * PackageManager.COMPONENT_ENABLED_STATE_ENABLED //显示应用图标
PackageManager.COMPONENT_ENABLED_STATE_DISABLED //隐藏应用图标
此方法在应用setting中也隐藏起来。
        * */
```

(2) 配置插件APK的包名。

如Manifest中配置，package="com.shiluohua.activity"
并且在需要启动的插件apk类中配置如下：

```
<activity android:name=".PluginActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <data
                    android:host="PluginActivity"
                    android:scheme="com.shiluohua.activity" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

最好在build.gradle中也配置好应用程序的包名。

```
defaultConfig {
        applicationId "com.shiluohua.activity"
```

(3) 调用插件apk。

调用方法。 。加关注知乎。(20180325该功能暂时没有完成，)

说明： 目前qy代码完成了打包混淆工作,DemoApplication在qy做的修改不提交
co代码完成了蓝牙模块，同时也集成了谷歌大数据分析。
sun代码目前开发retrofit工作


