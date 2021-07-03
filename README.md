## 前言

最近自己想做一个天气动画的小demo，在网上找了很多雷电效果动画都不是很理想。忽然看到网上有人用flutter实现了一个，效果还不错，想着用android原生给它复刻出来。看看效果。

## 自定义天气背景雷电，流星，雨，雪等效果

鉴于这个背景，参考了[@下位子](https://github.com/xiaweizi)的flutter版本天气动画插件[flutter_weather_bg](https://github.com/xiaweizi/flutter_weather_bg)
实现了同等的天气雷电，流星，雨，雪等效果

先看一下整体的效果：

![xtmwg-xgqu1.gif](https://upload-images.jianshu.io/upload_images/19328990-c390adb622287580.gif?imageMogr2/auto-orient/strip)

如果想直接使用，在根`build.gradle`配置：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

在`app`下的`build.gradle`添加依赖

	dependencies {
		 implementation 'com.github.Rainvvy:Weather_Bg:v1.1'
	}

可以在`XML`直接使用：

       <com.rainy.weahter_bg_plug.WeatherBg
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:type="heavyRainy"
            />

也可以通过代码使用
  ```javascript
  setCotentView(new WeatherBg(this,WeatherUtil.WeatherType.heavyRainy));
  ```
具体包括的类型：
- heavyRainy
- heavySnow
- middleSnow
- thunder
- lightRainy
- lightSnow
- sunnyNight
- sunny
- cloudy
- cloudyNight
- middleRainy
- hazy
- foggy
- overcast
- dusty
## 关于作者
- `qq:549813516`
- `email:im.wyu@qq.com`
- `github:https://github.com/Rainvvy`

## 鸣谢

感谢[@下位子](https://github.com/xiaweizi)提供的支持和参考。

[flutter_weather_bg](https://github.com/xiaweizi/flutter_weather_bg)

## License

MIT

