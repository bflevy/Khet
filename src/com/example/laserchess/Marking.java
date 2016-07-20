package com.example.laserchess;

public class Marking {

  private final int mXPos, mYPos, mPlayerNum;

  public Marking(int playerNum, int xPos, int yPos, boolean isFirstHalf) {
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

  public int getPlayerNum() {
    return mPlayerNum;
  }
}
