package com.rainy.weahter_bg_plug.utils;

import android.content.Context;
import android.graphics.Color;

import com.rainy.weahter_bg_plug.R;

import java.util.HashMap;


/**
 * create by Rainy on 2020/10/17.
 * email: im.wyu@qq.com
 * github: Rainvy
 * describe:
 */
public class WeatherUtil {


    private static HashMap<String, int[]> weatherColors = new HashMap<>();

    private static HashMap<Integer, String> xmlWeatherTypes;


    public static String[] weathers = {
            WeatherType.sunny,
            WeatherType.cloudy,
            WeatherType.cloudyNight,
            WeatherType.dusty,
            WeatherType.foggy,
            WeatherType.hazy,
            WeatherType.lightRainy,
            WeatherType.middleRainy,
            WeatherType.heavyRainy,
            WeatherType.lightSnow,
            WeatherType.middleSnow,
            WeatherType.heavySnow,
            WeatherType.overcast,
            WeatherType.sunnyNight,
            WeatherType.thunder
    };

    public static boolean isSnowRain(String weatherType) {
        return isRainy(weatherType) || isSnow(weatherType);
    }

    /**
     * 判断是否下雨，小中大包括雷暴，都是属于雨的类型
     *
     * @param weatherType
     * @return
     */
    public static boolean isRainy(String weatherType) {
        return weatherType.equals( WeatherType.lightRainy) ||
                weatherType.equals(WeatherType.middleRainy) ||
                weatherType.equals(WeatherType.heavyRainy) ||
                weatherType.equals(WeatherType.thunder);
    }

    /**
     * 判断是否下雪
     */

    public static boolean isSnow(String weatherType) {
        return weatherType.equals( WeatherType.lightSnow) ||
                weatherType.equals(WeatherType.middleSnow) ||
                weatherType.equals(WeatherType.heavySnow);
    }

    /**
     * 根据天气类型获取背景的颜色值
     */

    public static int[] getColor(Context context, String weatherType) {

        if (weatherColors.size() == 0) {
            initWeatherHashMaps(context);
        }

        int[] re = weatherColors.get(weatherType);

        if (re != null) {

            return re;

        }

        return weatherColors.get(WeatherType.sunny);

    }

    private static void initWeatherHashMaps(Context context) {

        weatherColors.put(WeatherType.sunny, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_sunny)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_sunny)[1])});
        weatherColors.put(WeatherType.sunnyNight, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_sunny_light)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_sunny_light)[1])});
        weatherColors.put(WeatherType.cloudyNight, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_cloudy_light)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_cloudy)[1])});
        weatherColors.put(WeatherType.overcast, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_overcast)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_overcast)[1])});
        weatherColors.put(WeatherType.lightRainy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_lightRainy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_lightRainy)[1])});
        weatherColors.put(WeatherType.middleRainy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_middleRainy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_middleRainy)[1])});
        weatherColors.put(WeatherType.thunder, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_thunder)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_thunder)[1])});
        weatherColors.put(WeatherType.hazy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_hazy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_hazy)[1])});
        weatherColors.put(WeatherType.foggy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_foggy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_foggy)[1])});
        weatherColors.put(WeatherType.lightSnow, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_lightSnow)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_lightSnow)[1])});
        weatherColors.put(WeatherType.middleSnow, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_middleSnow)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_middleSnow)[1])});
        weatherColors.put(WeatherType.heavySnow, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_heavySnow)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_heavySnow)[1])});
        weatherColors.put(WeatherType.dusty, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.dusty)[0]),Color.parseColor(context.getResources().getStringArray(R.array.dusty)[1])});
        weatherColors.put(WeatherType.cloudy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_cloudy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_cloudy)[1])});
        weatherColors.put(WeatherType.heavyRainy, new int[]{Color.parseColor(context.getResources().getStringArray(R.array.weather_middleRainy)[0]),Color.parseColor(context.getResources().getStringArray(R.array.weather_middleRainy)[1])});


    }

    public static String xmlType(int xmlType) {
        if (xmlWeatherTypes == null) {
            xmlWeatherTypes = new HashMap<>();
            xmlWeatherTypes.put(0, WeatherType.sunny);
            xmlWeatherTypes.put(1, WeatherType.sunnyNight);
            xmlWeatherTypes.put(2, WeatherType.cloudy);
            xmlWeatherTypes.put(3, WeatherType.cloudyNight);
            xmlWeatherTypes.put(4, WeatherType.overcast);
            xmlWeatherTypes.put(5, WeatherType.lightRainy);
            xmlWeatherTypes.put(6, WeatherType.middleRainy);
            xmlWeatherTypes.put(7, WeatherType.heavyRainy);
            xmlWeatherTypes.put(8, WeatherType.thunder);
            xmlWeatherTypes.put(9, WeatherType.hazy);
            xmlWeatherTypes.put(10, WeatherType.foggy);
            xmlWeatherTypes.put(11, WeatherType.lightSnow);
            xmlWeatherTypes.put(12, WeatherType.middleSnow);
            xmlWeatherTypes.put(13, WeatherType.heavySnow);
            xmlWeatherTypes.put(14, WeatherType.dusty);
        }

        return xmlWeatherTypes.get(xmlType);
    }


    /**
     * 根据天气类型获取天气的描述信息
     */

    public static String getWeatherDesc(String weatherType) {

        switch (weatherType) {
            case WeatherType.sunny:
            case WeatherType.sunnyNight:
                return "晴";
            case WeatherType.cloudy:
            case WeatherType.cloudyNight:
                return "多云";
            case WeatherType.overcast:
                return "阴";
            case WeatherType.lightRainy:
                return "小雨";
            case WeatherType.middleRainy:
                return "中雨";
            case WeatherType.heavyRainy:
                return "大雨";
            case WeatherType.thunder:
                return "雷阵雨";
            case WeatherType.hazy:
                return "雾";
            case WeatherType.foggy:
                return "霾";
            case WeatherType.lightSnow:
                return "小雪";
            case WeatherType.middleSnow:
                return "中雪";
            case WeatherType.heavySnow:
                return "大雪";
            case WeatherType.dusty:
                return "浮尘";
            default:
                return "晴";
        }
    }


    public static class WeatherType {

        //大雨
        public static final String heavyRainy = "heavyRainy";
        public static final String heavySnow = "heavySnow";
        public static final String middleSnow = "middleSnow";
        public static final String thunder = "thunder";
        public static final String lightRainy = "lightRainy";
        public static final String lightSnow = "lightSnow";
        public static final String sunnyNight = "sunnyNight";
        public static final String sunny = "sunny";
        public static final String cloudy = "cloudy";
        public static final String cloudyNight = "cloudyNight";
        public static final String middleRainy = "middleRainy";
        public static final String hazy = "hazy";
        public static final String foggy = "foggy";
        public static final String overcast = "overcast";
        public static final String dusty = "dusty";


    }


}
