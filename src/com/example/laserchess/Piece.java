package com.example.laserchess;

public class Piece {

  enum PieceType {
    PYRAMID, SCARAB, PHARAOH, SPHINX, ANUBIS
  }

  enum CornerDirection {
    LEFT_UP(-1, -1), RIGHT_UP(1, -1), RIGHT_DOWN(1, 1), LEFT_DOWN(-1, 1);

    int x, y;

    CornerDirection(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  enum Direction {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    int x, y;

    Direction(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  public static int directionToAngle(CornerDirection direction) {
    return direction.ordinal() * 90 - 45;
  }

  public static int directionToAngle(Direction direction) {
    return direction.ordinal() * 90;
  }

  private final PieceType mPieceType;
  private CornerDirection mDirection;
  private final int mPlayerNum;
  private int mXPos;
  private int mYPos;

  public Piece(int playerNum, int xPos, int yPos, CornerDirection direction, PieceType pieceType,
      boolean isFirstHalf) {
    mPieceType = pieceType;
    mDirection =
        isFirstHalf ? direction : CornerDirection.values()[clipDirection(direction.ordinal() + 2)];
    mPlayerNum = isFirstHalf ? playerNum : (playerNum - 1) * -1;
    mXPos = isFirstHalf ? xPos : 9 - xPos;
    mYPos = isFirstHalf ? yPos : 7 - yPos;
  }

  public int getXPos() {
    return mXPos;
  }

  public int getYPos() {
    return mYPos;
  }

  public PieceType getPieceType() {
    return mPieceType;
  }

  public CornerDirection getDirection() {
    return mDirection;
  }

  public int getPlayerNum() {
    return mPlayerNum;
  }

  public boolean rotate(boolean clockwise) {
    if (mPieceType == PieceType.PHARAOH) {
      return false;
    }
    if (mPieceType == PieceType.SPHINX) {
      // TODO: maybe prevent sphinx from being able to rotate to face away from board
    }
    int newIndex = mDirection.ordinal() + (clockwise ? 1 : -1);

    mDirection = CornerDirection.values()[clipDirection(newIndex)];
    return true;
  }

  public static int clipDirection(int directionIndex) {
    if (directionIndex < 0) {
      return directionIndex + 4;
    } else if (directionIndex >= 4) {
      return directionIndex - 4;
    }
    return directionIndex;
  }

  public boolean move(int xMove, int yMove) {
    if (mPieceType == PieceType.SPHINX) {
      return false;
    }
    int newX = mXPos + xMove;
    if (newX < 0 || newX >= 10) {
      return false;

    }
    int newY = mYPos + yMove;
    if (newY < 0 || newY >= 8) {
      return false;
    }
    mXPos = newX;
    mYPos = newY;
    return true;
  }

  public Direction checkCollision(Direction direction) {
    if (mPieceType == PieceType.SCARAB || mPieceType == PieceType.PYRAMID) {
      int laserX = direction.x;
      int laserY = direction.y;
      int pieceX = mDirection.x;
      int pieceY = mDirection.y;
      boolean isVertical = laserX == 0;
      if ((isVertical && laserY == pieceY || !isVertical && laserX == pieceX)) {
        if (mPieceType == PieceType.PYRAMID) {
          return null;
        }
        if (mPieceType == PieceType.SCARAB) {
          pieceX *= -1;
          pieceY *= -1;
        }
      }

      int newX, newY;
      newY = Math.abs(laserX) * pieceY;
      newX = Math.abs(laserY) * pieceX;
      if (newX == 0) {
        if (newY == -1) {
          return Direction.UP;
        }
        if (newY == 1) {
          return Direction.DOWN;
        }
      } else if (newY == 0) {
        if (newX == -1) {
          return Direction.LEFT;
        }
        if (newX == 1) {
          return Direction.RIGHT;
        }
      }
    }
    if (mPieceType == PieceType.ANUBIS) {
      Direction pieceDirection = Direction.values()[mDirection.ordinal()];
      int flippedX = pieceDirection.x * -1;
      int flippedY = pieceDirection.y * -1;
      if (flippedX == direction.x && flippedY == direction.y) {
        return direction;
      }
    }
    return null;
  }
}
