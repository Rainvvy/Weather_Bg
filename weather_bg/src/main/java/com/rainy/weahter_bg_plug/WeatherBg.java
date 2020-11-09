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
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainy.weahter_bg_plug.entity.MeteorParam;
import com.rainy.weahter_bg_plug.entity.RainSnowParams;
import com.rainy.weahter_bg_plug.entity.StarParam;
import com.rainy.weahter_bg_plug.entity.ThunderParams;
import com.rainy.weahter_bg_plug.utils.Logger;
import com.rainy.weahter_bg_plug.utils.Unit;
import com.rainy.weahter_bg_plug.utils.WeatherUtil;
import com.rainy.weahter_bg_plug.utils.WeatherUtil.WeatherType;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import static android.animation.ValueAnimator.REVERSE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static com.rainy.weahter_bg_plug.utils.WeatherUtil.WeatherType.sunny;
import static com.rainy.weahter_bg_plug.utils.WeatherUtil.WeatherType.sunnyNight;

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

    ///圆角背景图片
    private Bitmap mMaskBitmap;

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

    /// 流星的长度
    final float meteorHeight = 0.8f;

    /// 流星的高度
    final int radius = 10;

    /**
     * 雨天 云层 滤镜
     */
    private float[][] rainCloudIdentitys = {
            {
                    0.45f, 0, 0, 0, 0,

                    0, 0.52f, 0, 0, 0,

                    0, 0, 0.6f, 0, 0,

                    0, 0, 0, 1, 0
            },
            {
                    0.16f, 0, 0, 0, 0,

                    0, 0.22f, 0, 0, 0,

                    0, 0, 0.31f, 0, 0,

                    0, 0, 0, 1, 0
            },
            {
                    0.19f, 0, 0, 0, 0,

                    0, 0.2f, 0, 0, 0,

                    0, 0, 0.22f, 0, 0,

                    0, 0, 0, 1, 0
            }

    };


    /**
     * 雪 云层 滤镜
     */
    private float[][] snowCloudIdentitys = {
            {
                    0.67f, 0, 0, 0, 0,

                    0, 0.75f, 0, 0, 0,

                    0, 0, 0.87f, 0, 0,

                    0, 0, 0, 1, 0
            },
            {
                    0.7f, 0, 0, 0, 0,

                    0, 0.77f, 0, 0, 0,

                    0, 0, 0.87f, 0, 0,

                    0, 0, 0, 1, 0
            },
            {
                    0.74f, 0, 0, 0, 0,

                    0, 0.74f, 0, 0, 0,

                    0, 0, 0.81f, 0, 0,

                    0, 0, 0, 1, 0
            }

    };


    /**
     * 天气类型
     */
    private String weatherType ;

    public WeatherBg(Context context,String weatherType) {
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

        //绘制天气背景
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
        LinearGradient mShader = new LinearGradient(0, 0, 0, height, color[0], color[1], Shader.TileMode.MIRROR);

        weatherPaint.setShader(mShader);
        weatherPaint.setShadowLayer(15, 10, 10, Color.GRAY);
        canvas.drawRect(new Rect(0,0,width,height), weatherPaint);
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
                ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                        1, 0, 0, 0, 0,

                        0, 1, 0, 0, 0,

                        0, 0, 1, 0, 0,

                        0, 0, 0, (float) param.alpha, 0});

                cloudPaint.setColorFilter(identity);
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
        float[] rainIdentity;

        if (weatherType.equals(WeatherType.lightRainy)) rainIdentity = rainCloudIdentitys[0];
        else if (weatherType.equals(WeatherType.middleRainy)) rainIdentity = rainCloudIdentitys[1];
        else rainIdentity = rainCloudIdentitys[2];

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(rainIdentity);

        cloudPaint.setColorFilter(identity);
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
        float[] snowIdentity;

        if (weatherType.equals(WeatherType.lightSnow)) snowIdentity = snowCloudIdentitys[0];
        else if (weatherType.equals(WeatherType.middleSnow)) snowIdentity = snowCloudIdentitys[1];
        else snowIdentity = snowCloudIdentitys[2];

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(snowIdentity);

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                0.62f, 0, 0, 0, 0,

                0, 0.55f, 0, 0, 0,

                0, 0, 0.45f, 0, 0,

                0, 0, 0, 1, 0});

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                0.75f, 0, 0, 0, 0,

                0, 0.77f, 0, 0, 0,

                0, 0, 0.82f, 0, 0,

                0, 0, 0, 1, 0});

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                1, 0, 0, 0, 0,

                0, 1, 0, 0, 0,

                0, 0, 1, 0, 0,

                0, 0, 0, 0.7f, 0});

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                0.67f, 0, 0, 0, 0,

                0, 0.67f, 0, 0, 0,

                0, 0, 0.67f, 0, 0,

                0, 0, 0, 1, 0});

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                0.32f, 0, 0, 0, 0,

                0, 0.39f, 0, 0, 0,

                0, 0, 0.52f, 0, 0,

                0, 0, 0, 0.9f, 0});

        cloudPaint.setColorFilter(identity);
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

        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                1, 0, 0, 0, 0,

                0, 1, 0, 0, 0,

                0, 0, 1, 0, 0,

                0, 0, 0, 0.9f, 0});

        cloudPaint.setColorFilter(identity);
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
        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                1, 0, 0, 0, 0,

                0, 1, 0, 0, 0,

                0, 0, 1, 0, 0,

                0, 0, 0, thunderParam.alpha, 0});
        thunderPaint.setColorFilter(identity);
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
        ColorMatrixColorFilter identity = new ColorMatrixColorFilter(new float[]{

                1, 0, 0, 0, 0,

                0, 1, 0, 0, 0,

                0, 0, 1, 0, 0,

                0, 0, 0, (float) star.alpha, 0});

        starPaint.setColorFilter(identity);
        canvas.scale((float) star.scale, (float) star.scale);
        canvas.drawBitmap(zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.star), 20, 20), (float) star.x, (float) star.y, starPaint);
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
        LinearGradient mShader = new LinearGradient(0, 0, 0, width, Color.parseColor("#FFFFFFFF"), Color.parseColor("#00FFFFFF"), Shader.TileMode.MIRROR);

        starPaint.setShader(mShader);
        starPaint.setColorFilter(null);
        starPaint.setAntiAlias(true);
        canvas.rotate((float) (Math.PI * meteor.radians));
        float scale = Unit.px2dip(context,width) / 392.0f;
        canvas.scale(scale, scale);
        canvas.translate(
                (float) meteor.translateX, (float) (Math.tan(Math.PI * 0.1) * Unit.dip2px(context,meteorWidth) + meteor.translateY));
//        canvas.translate(
//                200, 300);
        canvas.drawRoundRect(new RectF(0, 0, Unit.dip2px(context,meteorWidth), Unit.dip2px(context,meteorHeight)), Unit.dip2px(context,radius), Unit.dip2px(context,radius), starPaint);
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
            if (weatherType == WeatherType.lightRainy) {
                count = 70;
            } else if (weatherType == WeatherType.middleRainy) {
                count = 100;
            } else if (weatherType == WeatherType.heavyRainy ||
                    weatherType == WeatherType.thunder) {
                count = 200;
            } else if (weatherType == WeatherType.lightSnow) {
                count = 30;
            } else if (weatherType == WeatherType.middleSnow) {
                count = 100;
            } else if (weatherType == WeatherType.heavySnow) {
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
