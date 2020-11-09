package com.rainy.weahter_bg_plug.entity;

public class StarParam {
  /// x 坐标
  public double x;

  /// y 坐标
  public double y;

  /// 透明度值，默认为 0
 public double alpha = 0.0;

  /// 缩放
  public double scale;

  /// 是否反向动画
  boolean reverse = false;

  /// 当前下标值
  int index;

  int width;

  int height;

    public float widthRatio;

  public  StarParam(int index){
     this.index = index;
  }

    public void reset() {
    alpha = 0;
    double baseScale = index == 0 ? 0.7 : 0.5;
    scale = Math.random() * 0.1 + baseScale * widthRatio;
    x = Math.random() * 1 * width / scale;
    y = Math.random() * Math.max(0.3 * height, 150);
    reverse = false;
  }

  /// 用于初始参数
  public void init(int width, int height,float widthRatio) {
    this.width = width;
    this.height = height;
    this.widthRatio = widthRatio;
    alpha = Math.random();
    double baseScale = index == 0 ? 0.7 : 0.5;
    scale = (Math.random() * 0.1 + baseScale) * widthRatio;
    x = Math.random() * 1 * width / scale;
    y = Math.random() * Math.max(0.3 * height, 150);
    reverse = false;
  }

  /// 每次绘制完会触发此方法，开始移动
  public void move() {
    if (reverse == true) {
      alpha -= 0.01;
      if (alpha < 0) {
        reset();
      }
    } else {
      alpha += 0.01;
      if (alpha > 1.2) {
        reverse = true;
      }
    }
  }
}