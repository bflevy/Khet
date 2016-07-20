package com.example.laserchess;

import android.graphics.Point;

public class LaserPoint extends Point {
  Point endPoint;

  public LaserPoint(int x1, int y1, int x2, int y2) {
    super(x1, y1);
    endPoint = new Point(x2, y2);
  }
}
