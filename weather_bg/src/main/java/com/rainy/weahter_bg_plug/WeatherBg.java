package com.rainy.weahter_bg_plug;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.rainy.weahter_bg_plug.entity.MeteorParam;
import com.rainy.weahter_bg_plug.entity.RainSnowParams;
import com.rainy.weahter_bg_plug.entity.StarParam;
import com.rainy.weahter_bg_plug.entity.ThunderParams;
import com.rainy.weahter_bg_plug.utils.Logger;
import com.rainy.weahter_bg_plug.utils.Unit;
import com.rainy.weahter_bg_plug.utils.WeatherUtil;
import com.rainy.weahter_bg_plug.utils.WeatherUtil.WeatherType;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ValueAnimator.REVERSE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * create by Rainy on 2020/10/17.
 * email: im.wyu@qq.com
 * github: Rainvy
 * describe:
 */
public class WeatherBg extends View {


    private static final String TAG = "WeatherBg";


    private int width;
    private int height;

    private Context context;

    /**
     * 资源
     */
    private Bitmap rainSnowBitmap;
    private Bitmap cloudBitmap;
    private Bitmap sunBitmap;
    private Bitmap starBitmap;

    /*
     * 天气背景缓存 Bitmap
     */
    private Bitmap weatherBgBitmap;

    private Bitmap[] thunderBitmaps;
    private ThunderParams thunderParam;

    private Paint cloudPaint;
    private Paint weatherPaint;
    private Paint sunnyPaint;

    private Paint thunderPaint;

    private Paint starPaint;

    private ValueAnimator thunderAnimator;
    private List<RainSnowParams> _rainSnows = new ArrayList<>();


    private List<StarParam> starParams = new ArrayList<>();

    private List<MeteorParam> meteorParams = new ArrayList<>();


    /// 流星参数信息
    final int meteorWidth = 200;
    /**
     * 流星rectF
     */
    private RectF starRectF;
    /// 流星的长度
    final float meteorHeight = 0.8f;

    /// 流星的高度
    final int radius = 10;

    /**
     * 雨天 云层 滤镜
     */
    private ColorMatrixColorFilter[] rainCloudFilters = new ColorMatrixColorFilter[]{
            new ColorMatrixColorFilter(new float[]{
                    0.45f, 0, 0, 0, 0,

                    0, 0.52f, 0, 0, 0,

                    0, 0, 0.6f, 0, 0,

                    0, 0, 0, 1, 0
            }),
            new ColorMatrixColorFilter(new float[]{
                    0.16f, 0, 0, 0, 0,

                    0, 0.22f, 0, 0, 0,

                    0, 0, 0.31f, 0, 0,

                    0, 0, 0, 1, 0
            }),
            new ColorMatrixColorFilter(new float[]{
                    0.19f, 0, 0, 0, 0,

                    0, 0.2f, 0, 0, 0,

                    0, 0, 0.22f, 0, 0,

                    0, 0, 0, 1, 0
            }),

    };


    /**
     * 雪 云层 滤镜
     */
    private ColorMatrixColorFilter[] snowCloudFilters = new ColorMatrixColorFilter[]{
            new ColorMatrixColorFilter(new float[]{
                    0.67f, 0, 0, 0, 0,

                    0, 0.75f, 0, 0, 0,

                    0, 0, 0.87f, 0, 0,

                    0, 0, 0, 1, 0
            }),
             new ColorMatrixColorFilter(new float[]{
                     0.7f, 0, 0, 0, 0,

                     0, 0.77f, 0, 0, 0,

                     0, 0, 0.87f, 0, 0,

                     0, 0, 0, 1, 0
            }),
             new ColorMatrixColorFilter(new float[]{
                     0.74f, 0, 0, 0, 0,

                     0, 0.74f, 0, 0, 0,

                     0, 0, 0.81f, 0, 0,

                     0, 0, 0, 1, 0
            }),

    };


    /**
     * 天气类型
     */
    private String weatherType ;



    /**
     * 雨雪颜色过滤器
     */
    private ColorMatrixColorFilter rainSnowIdentity;
    /**
     * 雷电效果颜色过滤器
     */
    private ColorMatrixColorFilter thunderIdentity;

    /**
     * 流星过滤器
     */
    private ColorMatrixColorFilter starIdentity;

    private ColorMatrixColorFilter rainyCloudIdentity;

    private ColorMatrixColorFilter snowCloudIdentity;
    private ColorMatrixColorFilter dustyIdentity;
    private ColorMatrixColorFilter foggyIdentity;
    private ColorMatrixColorFilter overcastIdentity;

    private ColorMatrixColorFilter hazyIdentity;
    private ColorMatrixColorFilter cloudyNightIdentity;
    private ColorMatrixColorFilter cloudyIdentity;


    /**
     * 透明filter
     */
    private ColorMatrixColorFilter[] alphaFilters = new ColorMatrixColorFilter[100];

    /**
     * 流星渐变
     */
    private LinearGradient mStarShader;
    /**
     * 背景渐变
     */
    private LinearGradient mBackgroundShader;

    private Rect backgroundRect ;

    public WeatherBg(Context context, String weatherType) {
        super(context);
        this.context = context;
        this.weatherType = weatherType;
        init();
    }

    public WeatherBg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.WeatherBg);
        weatherType = WeatherUtil.xmlType(typedArray.getInt(R.styleable.WeatherBg_type,0));
        typedArray.recycle();
        this.context = context;
        init();
    }

    private void initAlphaFilter(){
        for (int i = 0; i < 100; i++) {

            alphaFilters[i] =  new ColorMatrixColorFilter(new float[]{

                    1, 0, 0, 0, 0,

                    0, 1, 0, 0, 0,

                    0, 0, 1, 0, 0,

                    0, 0, 0, 0.01f * i, 0});
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        initParasm();
        initThunderParams();
        initStarMeteorParams();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        initParasm();
        initThunderParams();
        initStarMeteorParams();
    }


    public void setWidth(int value , int unit){
        switch (unit){
            case COMPLEX_UNIT_PX:
               getLayoutParams().width = value;
                break;
            case COMPLEX_UNIT_DIP:
                getLayoutParams().width = Unit.dip2px(context,value);
                break;
            default: break;
        }
        requestLayout();
    }

      public void setHeight(int value , int unit){
        switch (unit){
            case COMPLEX_UNIT_PX:
                getLayoutParams().height = value;
                break;
            case COMPLEX_UNIT_DIP:
                getLayoutParams().height = Unit.dip2px(context,value);
                break;
            default:break;
        }

        requestLayout();
    }





    @Override
    protected void onDraw(Canvas canvas) {

        drawWeather(canvas);


        if (WeatherUtil.isSnowRain(weatherType)
                || weatherType.equals(WeatherType.thunder)
                || weatherType.equals(WeatherType.sunnyNight)) {

            invalidate();

        }

    }

    private  void  drawWeather(Canvas canvas){
        if(weatherBgBitmap == null){
            weatherBgBitmap =  Bitmap.createBitmap(width, height,   Bitmap.Config.RGB_565);
            Canvas weatherBgCanvas = new Canvas(weatherBgBitmap);
            //绘制天气背景
            drawWeatherBg(weatherBgCanvas);
            //绘制云层
            drawCloudBg(weatherBgCanvas);
        }
        canvas.drawBitmap(weatherBgBitmap,0,0,null);
        drawWeatherBg(canvas);
        //绘制云层
        drawCloudBg(canvas);
        //绘制雨雪层
        drawRainSnow(canvas);

        //绘制雷电
        drawThunder(canvas);

        //绘制星星  流星
        drawStarMeteor(canvas);

    }

    /**
     * 绘制天气背景
     */
    private void drawWeatherBg(Canvas canvas) {
        int[] color = WeatherUtil.getColor(context, weatherType);
        mBackgroundShader = new LinearGradient(0, 0, 0, height, color[0], color[1], Shader.TileMode.MIRROR);
        weatherPaint.setShader(mBackgroundShader);
        weatherPaint.setShadowLayer(15, 10, 10, Color.GRAY);
        backgroundRect =  new Rect(0,0,width,height);
        canvas.drawRect(backgroundRect, weatherPaint);
    }


    /**
     * 绘制雨天
     *
     * @param canvas
     */
    private void drawRainSnow(Canvas canvas) {

        if (!_rainSnows.isEmpty()) {
            for (RainSnowParams param : _rainSnows) {
                move(param);
                canvas.save();
                canvas.scale((float) param.scale, (float) param.scale);

                int index = (int) (param.alpha * 100);

                if (index < 0) {
                    index = 0;
                }
                if (index > 99) {
                    index = 99;
                }
                rainSnowIdentity = alphaFilters[index];

                cloudPaint.setColorFilter(rainSnowIdentity);
                canvas.drawBitmap(rainSnowBitmap, (float) param.x, (float) param.y, cloudPaint);
                canvas.restore();
            }
        }

    }


    /**
     * 绘制云层背景
     *
     * @param canvas
     */
    private void drawCloudBg(Canvas canvas) {
        switch (weatherType) {
            case WeatherType.sunny:
                drawSunny(canvas);
                break;
            case WeatherType.cloudy:
                drawCloudy(canvas);
                break;
            case WeatherType.cloudyNight:
                drawCloudyNight(canvas);
                break;
            case WeatherType.overcast:
                drawOvercast(canvas);
                break;

            case WeatherType.lightRainy:
            case WeatherType.middleRainy:
            case WeatherType.heavyRainy:
            case WeatherType.thunder:
                drawRainCloudBg(canvas);
                break;

            case WeatherType.hazy:
                drawHazy(canvas);
                break;
            case WeatherType.foggy:
                drawFoggy(canvas);
                break;

            case WeatherType.lightSnow:
            case WeatherType.middleSnow:
            case WeatherType.heavySnow:
                drawSnowCloudBg(canvas);
                break;

            case WeatherType.dusty:
                drawDusty(canvas);
                break;
            default:
                break;
        }
    }

    /**
     * 绘制雨天云层背景  小雨 中雨  大雨
     *
     * @param canvas
     */
    private void drawRainCloudBg(Canvas canvas) {
        canvas.save();


        if (weatherType.equals(WeatherType.lightRainy)) rainyCloudIdentity = rainCloudFilters[0];
        else if (weatherType.equals(WeatherType.middleRainy)) rainyCloudIdentity = rainCloudFilters[1];
        else rainyCloudIdentity = rainCloudFilters[2];


        cloudPaint.setColorFilter(rainyCloudIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 0.8f;
        canvas.scale(scale, scale);
        canvas.drawBitmap(cloudBitmap, (float) -380, (float) -150, cloudPaint);
        canvas.drawBitmap(cloudBitmap, (float) 0, (float) -60, cloudPaint);
        canvas.drawBitmap(cloudBitmap, (float) 0, (float) 60, cloudPaint);

        canvas.restore();
    }

    /**
     * 绘制雪云层背景  小雪 中雪  大雪
     *
     * @param canvas
     */
    private void drawSnowCloudBg(Canvas canvas) {
        canvas.save();

        if (weatherType.equals(WeatherType.lightSnow)) snowCloudIdentity = snowCloudFilters[0];
        else if (weatherType.equals(WeatherType.middleSnow)) snowCloudIdentity = snowCloudFilters[1];
        else snowCloudIdentity = snowCloudFilters[2];

        cloudPaint.setColorFilter(snowCloudIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 0.8f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, (float) -380, (float) -150, cloudPaint);
        canvas.drawBitmap(cloudBitmap, (float) 0, (float) -170, cloudPaint);


        canvas.restore();
    }


    /**
     * 绘制云层 浮沉
     *
     * @param canvas
     */
    private void drawDusty(Canvas canvas) {
        canvas.save();
        if(dustyIdentity == null){
            dustyIdentity = new ColorMatrixColorFilter(new float[]{

                    0.62f, 0, 0, 0, 0,

                    0, 0.55f, 0, 0, 0,

                    0, 0, 0.45f, 0, 0,

                    0, 0, 0, 1, 0});
        }

        cloudPaint.setColorFilter(dustyIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 2.0f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() * 0.5f, -200, cloudPaint);

        canvas.restore();
    }

    /**
     * 绘制云层 雾
     *
     * @param canvas
     */
    private void drawFoggy(Canvas canvas) {
        canvas.save();
        if(foggyIdentity == null){
            foggyIdentity = new ColorMatrixColorFilter(new float[]{

                    0.75f, 0, 0, 0, 0,

                    0, 0.77f, 0, 0, 0,

                    0, 0, 0.82f, 0, 0,

                    0, 0, 0, 1, 0});
        }
        cloudPaint.setColorFilter(foggyIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 2.0f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() * 0.5f, -200, cloudPaint);

        canvas.restore();
    }


    /**
     * 绘制云层 阴天
     *
     * @param canvas
     */
    private void drawOvercast(Canvas canvas) {
        canvas.save();
        if(overcastIdentity == null){
            overcastIdentity = new ColorMatrixColorFilter(new float[]{

                    1, 0, 0, 0, 0,

                    0, 1, 0, 0, 0,

                    0, 0, 1, 0, 0,

                    0, 0, 0, 0.7f, 0});
        }


        cloudPaint.setColorFilter(overcastIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 0.8f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, 0, -200, cloudPaint);
        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() / 2, -130, cloudPaint);
        canvas.drawBitmap(cloudBitmap, 100, 0, cloudPaint);

        canvas.restore();
    }


    /**
     * 绘制云层 霾
     *
     * @param canvas
     */
    private void drawHazy(Canvas canvas) {
        canvas.save();
        if(hazyIdentity == null){
            hazyIdentity = new ColorMatrixColorFilter(new float[]{

                    0.67f, 0, 0, 0, 0,

                    0, 0.67f, 0, 0, 0,

                    0, 0, 0.67f, 0, 0,

                    0, 0, 0, 1, 0});
        }
        cloudPaint.setColorFilter(hazyIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 2.0f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() * 0.5f, -200, cloudPaint);

        canvas.restore();
    }


    /**
     * 绘制云层 多云的夜晚效果
     *
     * @param canvas
     */
    private void drawCloudyNight(Canvas canvas) {
        canvas.save();
        if(cloudyNightIdentity == null){
            cloudyNightIdentity = new ColorMatrixColorFilter(new float[]{

                    0.32f, 0, 0, 0, 0,

                    0, 0.39f, 0, 0, 0,

                    0, 0, 0.52f, 0, 0,

                    0, 0, 0, 0.9f, 0});
        }
        cloudPaint.setColorFilter(cloudyNightIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 0.8f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, 0, -200, cloudPaint);
        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() / 2, -130, cloudPaint);
        canvas.drawBitmap(cloudBitmap, 100, 0, cloudPaint);
        canvas.restore();
    }


    int index = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        changeWeather();

        return super.onTouchEvent(event);
    }

    /**
     * 改变天气效果
     */
    public void changeWeather(String type) {
        if(weatherType.equals(type)){
            return;
        }
        weatherType = type;


        if (weatherType == null){
            weatherType = WeatherUtil.weathers[index] ;
            index++;
            if (index >= WeatherUtil.weathers.length) {
                index = 0;
            }
        }

        init();
        initParasm();
        initThunderParams();
        initStarMeteorParams();

        invalidate();
    }



    /**
     * 绘制云层 多云的夜晚效果
     *
     * @param canvas
     */
    private void drawCloudy(Canvas canvas) {
        canvas.save();
        if(cloudyIdentity == null){
            cloudyIdentity = new ColorMatrixColorFilter(new float[]{

                    1, 0, 0, 0, 0,

                    0, 1, 0, 0, 0,

                    0, 0, 1, 0, 0,

                    0, 0, 0, 0.9f, 0});
        }
        cloudPaint.setColorFilter(cloudyIdentity);
        float widthRatio = width / 392.0f;
        float scale = widthRatio * 0.8f;
        canvas.scale(scale, scale);

        canvas.drawBitmap(cloudBitmap, 0, -200, cloudPaint);
        canvas.drawBitmap(cloudBitmap, -cloudBitmap.getWidth() / 2, -130, cloudPaint);
        canvas.drawBitmap(cloudBitmap, 100, 0, cloudPaint);
        canvas.restore();
    }

    /**
     * 绘制云层 多云的夜晚效果
     *
     * @param canvas
     */
    private void drawSunny(Canvas canvas) {
        canvas.save();


        float widthRatio = Unit.px2dip(context, width) / 392.0f;
        float sunScale = widthRatio * 1.2f;
        canvas.scale(sunScale, sunScale);
        canvas.drawBitmap(sunBitmap, width - sunBitmap.getWidth() * sunScale, -sunBitmap.getWidth() / 2, sunnyPaint);
        canvas.restore();
        Logger.d(TAG, "Left : " + (width - sunBitmap.getWidth() * sunScale) + " , Top : " + -sunBitmap.getWidth() / 2 + ". sunScale : " + sunScale);
        canvas.save();
        float scale = width / 392.0f * 0.6f;
        canvas.scale(scale, scale);
        canvas.drawBitmap(cloudBitmap, -100, -100, sunnyPaint);
        canvas.restore();
    }


    /**
     * 绘制 雷电效果
     */
    private void drawThunder(Canvas canvas) {
        if (!(weatherType.equals(WeatherType.thunder) || weatherType.equals(WeatherType.heavyRainy))) {
            if (thunderAnimator != null) {
                thunderAnimator.cancel();
            }
            return;
        }
        if (thunderParam == null || thunderParam.image == null) {
            return;
        }

        canvas.save();
        int index = (int) (thunderParam.alpha * 100);

        if (index < 0) {
            index = 0;
        }
        if (index > 99) {
            index = 99;
        }
        thunderIdentity = alphaFilters[index];
        thunderPaint.setColorFilter(thunderIdentity);
        float scale = (float) (thunderParam.widthRatio * 1.2);
        canvas.scale(scale, scale);
        canvas.drawBitmap(thunderParam.image, (float) thunderParam.x, (float) thunderParam.y, thunderPaint);
        canvas.restore();
    }


    /**
     * 雷电 效果 动画
     */
    private void thunderAnimation() {
        thunderAnimator = ValueAnimator.ofFloat(0, 1);
        thunderAnimator.setDuration(500);
        thunderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                thunderParam.alpha = (float) animation.getAnimatedValue();
                if (!(weatherType.equals(WeatherType.thunder) || weatherType.equals(WeatherType.heavyRainy))) {
                    thunderAnimator.cancel();
                    return;
                }
                Logger.d(TAG, "thunderValue : " + animation.getAnimatedValue().toString());
            }
        });
        thunderAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Logger.d(TAG, "onAnimationEnd ----- ");
                if (!(weatherType.equals(WeatherType.thunder) || weatherType.equals(WeatherType.heavyRainy))) {
                    return;
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (thunderBitmaps != null) {
                            thunderParam.image = thunderBitmaps[(int) (Math.random() * thunderBitmaps.length)];
                            thunderAnimator.start();
                        }
                    }
                }, 500);


            }
        });
        thunderAnimator.setRepeatCount(1);
        thunderAnimator.setRepeatMode(REVERSE);
        thunderAnimator.start();
    }


    /**
     * 绘制星星 和流星
     */
    private void drawStarMeteor(Canvas canvas) {

        if (!weatherType.equals(WeatherType.sunnyNight)) {
            return;
        }

        if (starParams.size() > 0) {
            for (StarParam param : starParams) {
                drawStar(param, canvas);
            }
        }
        if (meteorParams.size() > 0) {
            for (MeteorParam param : meteorParams) {
                drawMeteor(param, canvas);
            }
        }
    }

    /**
     * 绘制星星
     *
     * @param star
     * @param canvas
     */
    private void drawStar(StarParam star, Canvas canvas) {
        if (star == null) {
            return;
        }
        canvas.save();
        int index = (int) (star.alpha * 100);

        if (index < 0) {
            index = 0;
        }
        if (index > 99) {
            index = 99;
        }
        starIdentity = alphaFilters[index];
        starPaint.setColorFilter(starIdentity);
        canvas.scale((float) star.scale, (float) star.scale);
        canvas.drawBitmap(starBitmap, (float) star.x, (float) star.y, starPaint);
        canvas.restore();
        star.move();

    }

    /**
     * 绘制流星
     */
    private void drawMeteor(MeteorParam meteor, Canvas canvas) {
        if (meteor == null) {
            return;
        }

        canvas.save();
        if(mStarShader == null){
            mStarShader = new LinearGradient(0, 0, 0, width, Color.parseColor("#FFFFFFFF"), Color.parseColor("#00FFFFFF"), Shader.TileMode.MIRROR);
        }

        starPaint.setShader(mStarShader);
        starPaint.setColorFilter(null);
        starPaint.setAntiAlias(true);
        canvas.rotate((float) (Math.PI * meteor.radians));
        float scale = Unit.px2dip(context,width) / 392.0f;
        canvas.scale(scale, scale);
        canvas.translate(
                (float) meteor.translateX, (float) (Math.tan(Math.PI * 0.1) * Unit.dip2px(context,meteorWidth) + meteor.translateY));
        if(starRectF == null){
            starRectF = new RectF(0, 0, Unit.dip2px(context,meteorWidth), Unit.dip2px(context,meteorHeight));
        }
        float starRadius = Unit.dip2px(context,radius);
        canvas.drawRoundRect( starRectF, starRadius, starRadius , starPaint);
        meteor.move(context);
        canvas.restore();
    }

    private void move(RainSnowParams params) {
        params.y = params.y + params.speed;
        if (WeatherUtil.isSnow(weatherType)) {
            double offsetX = Math.sin(params.y / (300 + 50 * params.alpha)) *
                    (1 + 0.5 * params.alpha) *
                    params.widthRatio;
            params.x += offsetX;
        }

        if (params.y > params.height / params.scale) {
            params.y = -params.height * params.scale;
            if (WeatherUtil.isRainy(weatherType) &&
                    rainSnowBitmap != null) {
                params.y = -rainSnowBitmap.getHeight();
            }
            params.reset();
        }
    }

    private void initParasm() {
        if (width == 0 || height == 0) {
            return;
        }
        _rainSnows.clear();
        // 界面上元素的 数量
        int count = 0;

        Logger.d(TAG, " 天气数据初始化----------");
        if (WeatherUtil.isSnowRain(weatherType)) {
            // 下雨
            if (weatherType.equals(WeatherType.lightRainy)) {
                count = 70;
            } else if (weatherType.equals( WeatherType.middleRainy)) {
                count = 100;
            } else if (weatherType.equals(WeatherType.heavyRainy) ||
                    weatherType.equals(WeatherType.thunder)) {
                count = 200;
            } else if (weatherType.equals(WeatherType.lightSnow)) {
                count = 30;
            } else if (weatherType.equals(WeatherType.middleSnow)) {
                count = 100;
            } else if (weatherType.equals(WeatherType.heavySnow)) {
                count = 200;
            }
        }
        double widthRatio = Unit.px2dip(context, width) / 392.0d;
        double heightRatio = Unit.px2dip(context, height) / 817d;

        for (int i = 0; i < count; i++) {
            RainSnowParams params = new RainSnowParams(width, height, weatherType);
            params.init(context, widthRatio, heightRatio);
            _rainSnows.add(params);
        }

        Logger.d(TAG, " 天气数据初始化成功");

    }

    private void init() {


        cloudBitmap = zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.cloud), 1080, 631);

        rainSnowBitmap = BitmapFactory.decodeResource(getResources(), WeatherUtil.isRainy(weatherType) ? R.drawable.rain : R.drawable.snow);
        sunBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sun);

        starBitmap = zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.star), 20, 20);
        thunderBitmaps = weatherType.equals(WeatherType.thunder) || weatherType.equals(WeatherType.heavyRainy) ? new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.lightning0),
                BitmapFactory.decodeResource(getResources(), R.drawable.lightning1),
                BitmapFactory.decodeResource(getResources(), R.drawable.lightning2),
                BitmapFactory.decodeResource(getResources(), R.drawable.lightning3),
                BitmapFactory.decodeResource(getResources(), R.drawable.lightning4)
        } : null;


        //初始化 画笔
        cloudPaint = new Paint();
        weatherPaint = new Paint();

        weatherPaint.setAntiAlias(true);
        weatherPaint.setStyle(Paint.Style.FILL);


        /**
         * CLAMP:以最后一个像素进行拉伸
         * REPEAT:重复进行同样的渐变
         * MIRROR:镜面翻转，等价于反向REPEAT
         */

        sunnyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunnyPaint.setMaskFilter(new BlurMaskFilter(40, BlurMaskFilter.Blur.NORMAL));

        thunderPaint = new Paint();

        starPaint = new Paint();
        starPaint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.NORMAL));
        starPaint.setColor(Color.WHITE);
        starPaint.setStyle(Paint.Style.FILL);

        initAlphaFilter();
    }

    /**
     * 初始化雷暴参数
     */

    private void initThunderParams() {
        if (thunderBitmaps == null || thunderBitmaps.length == 0) {
            return;
        }

        if (!(weatherType.equals(WeatherType.thunder) || weatherType.equals(WeatherType.heavyRainy))) {
            if (thunderAnimator != null) {
                thunderAnimator.cancel();
            }
            return;
        }

        double widthRatio = Unit.px2dip(context, width) / 392.0d;

        thunderParam = new ThunderParams(
                thunderBitmaps[(int) (Math.random() * 5)], width, height, widthRatio).reset();


        thunderAnimation();


    }

    /**
     * 初始化 星星  流星 参数
     */
    private void initStarMeteorParams() {
        meteorParams.clear();
        starParams.clear();

        if (!weatherType.equals(WeatherType.sunnyNight)) {
            return;
        }

        if (width == 0 || height == 0){
            return;
        }

        float widthRatio = Unit.px2dip(context, width)  / 392.0f;
        for (int i = 0; i < 50; i++) {
            int index = (int) (Math.random() * 2);
            StarParam starParam = new StarParam(index);
            starParam.init(width, height, widthRatio);
            starParams.add(starParam);
        }

        for (int i = 0; i < 4; i++) {
            MeteorParam param = new MeteorParam();
            param.init(width, height, widthRatio);
            meteorParams.add(param);
        }


    }

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public String getWeatherType() {
        return weatherType;
    }
}
