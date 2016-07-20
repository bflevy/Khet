package com.example.laserchess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.laserchess.Piece.CornerDirection;
import com.example.laserchess.Piece.Direction;
import com.example.laserchess.Piece.PieceType;


public class PieceView extends ImageView {

  public PieceView(Context context) {
    super(context);
    init();
  }

  public PieceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PieceView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private List<Piece> mPieces;
  private Paint mShapePaint;
  private Path mTrianglePath;
  private List<Point> mDrawPoints;
  int mCellSize, mCellMargin;
  private List<Point> mShapePoints;
  private List<LaserPoint> mLaserPoints;
  private List<Marking> mMarkings;

  private void init() {

    mCellSize = (int) getResources().getDimension(R.dimen.cell_size);

    mPieces = new ArrayList<Piece>();
    mLaserPoints = new ArrayList<LaserPoint>();

    mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    mShapePaint.setStyle(Paint.Style.STROKE);
    mShapePaint.setStrokeWidth(3);

    mTrianglePath = new Path();

    mDrawPoints = new ArrayList<Point>();

    mCellMargin = (int) getResources().getDimension(R.dimen.cell_margin);
    int startPos = mCellMargin;
    int endPos = mCellSize - mCellMargin;
    mShapePoints = new ArrayList<Point>();
    mShapePoints.add(new Point(startPos, startPos));
    mShapePoints.add(new Point(endPos, startPos));
    mShapePoints.add(new Point(endPos, endPos));
    mShapePoints.add(new Point(startPos, endPos));

  }

  public void setPieces(List<Piece> pieces) {
    mPieces = pieces;
  }

  public void setMarkings(List<Marking> markings) {
    mMarkings = markings;
  }

  public void setLaserPoints(List<LaserPoint> laserPoints) {
    mLaserPoints = laserPoints;
  }

  @Override
  protected void onDraw(Canvas canvas) {

    for (Piece piece : mPieces) {

      mShapePaint.setColor(getResources().getColor(colors.get(piece.getPlayerNum())));

      mDrawPoints.clear();

      int xOffset = piece.getXPos() * mCellSize;
      int yOffset = piece.getYPos() * mCellSize;

      mTrianglePath.reset();

      mTrianglePath.setFillType(Path.FillType.EVEN_ODD);
      if (piece.getPieceType() == PieceType.PYRAMID) {
        for (int i = 0; i < mShapePoints.size(); i++) {
          Point shapePoint = mShapePoints.get(i);
          if (i != piece.getDirection().ordinal()) {
            mDrawPoints.add(shapePoint);
          }
        }
        mTrianglePath.moveTo(xOffset + mDrawPoints.get(0).x, yOffset + mDrawPoints.get(0).y);
        mTrianglePath.lineTo(xOffset + mDrawPoints.get(1).x, yOffset + mDrawPoints.get(1).y);
        mTrianglePath.lineTo(xOffset + mDrawPoints.get(2).x, yOffset + mDrawPoints.get(2).y);
        mTrianglePath.lineTo(xOffset + mDrawPoints.get(0).x, yOffset + mDrawPoints.get(0).y);
        mTrianglePath.close();
        canvas.drawPath(mTrianglePath, mShapePaint);
      } else if (piece.getPieceType() == PieceType.SCARAB) {
        for (int i = 0; i < mShapePoints.size(); i++) {
          Point shapePoint = mShapePoints.get(i);
          if (i != piece.getDirection().ordinal() && i != piece.getDirection().ordinal() + 2
              && i != piece.getDirection().ordinal() - 2) {
            mDrawPoints.add(shapePoint);
          }
        }
        canvas.drawLine(xOffset + mDrawPoints.get(0).x, yOffset + mDrawPoints.get(0).y, xOffset
            + mDrawPoints.get(1).x, yOffset + mDrawPoints.get(1).y, mShapePaint);
      } else if (piece.getPieceType() == PieceType.SPHINX) {
        int xStart, yStart, xEnd, yEnd;
        xStart = xEnd = xOffset + mCellSize / 2;
        yStart = yEnd = yOffset + mCellSize / 2;
        yEnd +=
            (mCellSize / 2 - mCellMargin) * Direction.values()[piece.getDirection().ordinal()].y;
        xEnd +=
            (mCellSize / 2 - mCellMargin) * Direction.values()[piece.getDirection().ordinal()].x;
        canvas.drawLine(xStart, yStart, xEnd, yEnd, mShapePaint);
        canvas.drawRect(xOffset + mCellMargin, yOffset + mCellMargin, xOffset + mCellSize
            - mCellMargin, yOffset + mCellSize - mCellMargin, mShapePaint);
      } else if (piece.getPieceType() == PieceType.PHARAOH) {
        mShapePaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(xOffset + mCellMargin, yOffset + mCellMargin, xOffset + mCellSize
            - mCellMargin, yOffset + mCellSize - mCellMargin, mShapePaint);
        mShapePaint.setStyle(Paint.Style.STROKE);
        mShapePaint.setStrokeWidth(3);
      } else if (piece.getPieceType() == PieceType.ANUBIS) {
        int xStart, yStart, xEnd1, yEnd1, xEnd2, yEnd2;
        xStart = xEnd1 = xEnd2 = xOffset + mCellSize / 2;
        yStart = yEnd1 = yEnd2 = yOffset + mCellSize / 2;
        CornerDirection forward =
            CornerDirection.values()[Piece.clipDirection(piece.getDirection().ordinal() + 1)];
        yEnd1 += (mCellSize / 2 - mCellMargin) * piece.getDirection().y;
        xEnd1 += (mCellSize / 2 - mCellMargin) * piece.getDirection().x;
        yEnd2 += (mCellSize / 2 - mCellMargin) * forward.y;
        xEnd2 += (mCellSize / 2 - mCellMargin) * forward.x;
        canvas.drawLine(xStart, yStart, xEnd1, yEnd1, mShapePaint);
        canvas.drawLine(xStart, yStart, xEnd2, yEnd2, mShapePaint);
        canvas.drawRect(xOffset + mCellMargin, yOffset + mCellMargin, xOffset + mCellSize
            - mCellMargin, yOffset + mCellSize - mCellMargin, mShapePaint);
      }
    }


    // draw laser points
    mShapePaint.setColor(getResources().getColor(R.color.red));
    for (LaserPoint laserPoint : mLaserPoints) {
      int centerX1 = laserPoint.x * mCellSize + mCellSize / 2;
      int centerY1 = laserPoint.y * mCellSize + mCellSize / 2;
      int centerX2 = laserPoint.endPoint.x * mCellSize + mCellSize / 2;
      int centerY2 = laserPoint.endPoint.y * mCellSize + mCellSize / 2;

      canvas.drawLine(centerX1, centerY1, centerX2, centerY2, mShapePaint);
    }

    for (Marking marking : mMarkings) {
      mShapePaint.setColor(getResources().getColor(colors.get(marking.getPlayerNum())));
      int xOffset = marking.getXPos() * mCellSize;
      int yOffset = marking.getYPos() * mCellSize;
      int markingMargin = 5;
      canvas.drawRect(xOffset + markingMargin, yOffset + markingMargin, xOffset + mCellSize
          - markingMargin, yOffset + mCellSize - markingMargin, mShapePaint);
    }
  }

  private static final List<Integer> colors;
  static {
    List<Integer> list = new ArrayList<Integer>();
    list.add(R.color.red);
    list.add(R.color.blue);
    colors = Collections.unmodifiableList(list);
  }

}
