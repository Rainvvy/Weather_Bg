package com.rainy.weahter_bg_plug.entity;

import android.content.Context;

import com.rainy.weahter_bg_plug.utils.Unit;

public class MeteorParam {
  public double translateX;
  public double translateY;
  public double radians;

  public int width, height;
  public double  widthRatio;

  /**
   * 初始化数据
   * @param width
   * @param height
   * @param widthRatio
   */
  public void init(int width, int height, double widthRatio) {
    this.width = width;
    this.height = height;
    this.widthRatio = widthRatio;
    reset();
  }

  /**
   *  重置数据
   */
  public void reset() {
    translateX = width + Math.random() * 20.0 * width;
    radians = - (Math.random() * 5 - 0.05);
    translateY = Math.random() * 0.5 * height * widthRatio;
  }

  /**
   *  移动
   */
  public void move(Context context) {
    translateX -=  Unit.dip2px(context,40);
    if (translateX <= -1.0 * width / widthRatio) {
      reset();
    }
  }
}
