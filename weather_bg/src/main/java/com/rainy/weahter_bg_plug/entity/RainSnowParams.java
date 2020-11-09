package com.rainy.weahter_bg_plug.entity;

import android.content.Context;

import com.rainy.weahter_bg_plug.utils.Unit;
import com.rainy.weahter_bg_plug.utils.WeatherUtil.WeatherType;
import com.rainy.weahter_bg_plug.utils.WeatherUtil;
/**
 * create by Rainy on 2020/10/17.
 * email: im.wyu@qq.com
 * github: Rainvy
 * describe:
 */
public class RainSnowParams {

    /**
     *  x 坐标
     */
   public double x;

    /**
     *  // y 坐标
     */

    public double y;

    /**
     * // 下落速度
     */

    public double speed;
    /**
     * // 绘制的缩放
     */

    public double scale;
    /**
     *  // 宽度
     */

    public double width;
    /**
     *  // 高度
     */

    public double height;

    /**
     *  透明度
      */

    public double alpha;

    /**
     * 天气类型
      */

    public String weatherType;

    public double widthRatio;
     double heightRatio;

    public RainSnowParams(double width, double height,  String weatherType){
        this.width = width;
        this.height = height;
        this.weatherType = weatherType;
    }

   public void init(Context context,double widthRatio, double heightRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = Math.max(heightRatio, 0.65d);
        /// 雨 0.1 雪 0.5
        reset();
        y = Math.random() *(int)(Unit.dip2px(context,800) / scale);
    }

    /**
     * /// 当雪花移出屏幕时，需要重置参数
     */
    public void reset() {
        double ratio = 1.0d;

        if (weatherType == WeatherType.lightRainy) {
            ratio = 0.5d;
        } else if (weatherType == WeatherType.middleRainy) {
            ratio = 0.75d;
        } else if (weatherType == WeatherType.heavyRainy ||
                weatherType == WeatherType.thunder) {
            ratio = 1d;
        } else if (weatherType == WeatherType.lightSnow) {
            ratio = 0.75d;
        } else if (weatherType == WeatherType.middleSnow) {
            ratio = 1d;
        } else if (weatherType == WeatherType.heavySnow) {
            ratio = 1.25d;
        }
        if (WeatherUtil.isRainy(weatherType)) {
            double random = 0.4d + 0.12d * Math.random() * 5d;
            this.scale = random * 1.2d;
            this.speed = 30d * random * ratio * heightRatio;
            this.alpha = random * 0.6d;
            x = Math.random() * width * (int)(1.2d / scale) -
                   width *  (int)(0.1d / scale);
        } else {
            double random = 0.4d + 0.12d * Math.random() * 5d;
            this.scale = random * 0.8d * heightRatio;
            this.speed = 8d * random * ratio * heightRatio;
            this.alpha = random;
            x = Math.random() * width * (int)(1.2d / scale) -
                    width *  (int)(0.1d / scale);
        }
    }
}
