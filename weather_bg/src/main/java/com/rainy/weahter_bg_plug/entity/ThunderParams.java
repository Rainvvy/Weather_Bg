package com.rainy.weahter_bg_plug.entity;

import android.graphics.Bitmap;

public class ThunderParams {
    /**
     * 配置闪电的图片资源
     */

    public Bitmap image;

    /**
     * 闪电的 alpha 属性
     */
    public float alpha;

    /**
     * 图片展示的 x 坐标
     */
    public double x;
    /**
     * 图片展示的 y 坐标
     */
    public double y;

    /**
     * 雷电图片的宽度
     */
    private int imgWidth;
    /**
     * 雷电图片的高度
     */
    private int imgHeight;
    private  int width, height;
    public double  widthRatio;

    public ThunderParams(Bitmap image, int width, int height, double widthRatio) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.widthRatio = widthRatio;
    }

  /**
   * 重置图片的位置信息
    */

    public ThunderParams reset() {
        x = Math.random() * 0.5d * widthRatio - 1 / 3 * image.getWidth();
        y = Math.random() * -0.05d * height;
        alpha = 0;
        return  this;
    }
}