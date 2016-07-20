package com.example.laserchess;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laserchess.Piece.CornerDirection;
import com.example.laserchess.Piece.Direction;
import com.example.laserchess.Piece.PieceType;

public class MainActivity extends Activity {

  private GridView mGameGrid;
  private List<Piece> mPieces;
  private final Piece[] mSpinxs = new Piece[2];
  private GameBoardAdapter mBoardAdapter;
  private PieceView mPieceView;
  private Piece mSelectedPiece;
  private List<Marking> mMarkings;

  private int mPlayerTurn;

  private ImageButton mLeftButton, mUpButton, mDownButton, mRightButton, mRotateCButton,
      mRotateCCButton, mUpRightButton, mUpLeftButton, mDownRightButton, mDownLeftButton;
  private TextView mPlayerTurnText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mGameGrid = (GridView) findViewById(R.id.game_grid);
    mPlayerTurnText = (TextView) findViewById(R.id.turn_text);
    mLeftButton = (ImageButton) findViewById(R.id.left_button);
    mRightButton = (ImageButton) findViewById(R.id.right_button);
    mUpButton = (ImageButton) findViewById(R.id.up_button);
    mDownButton = (ImageButton) findViewById(R.id.down_button);
    mDownLeftButton = (ImageButton) findViewById(R.id.down_left_button);
    mDownRightButton = (ImageButton) findViewById(R.id.down_right_button);
    mUpLeftButton = (ImageButton) findViewById(R.id.up_left_button);
    mUpRightButton = (ImageButton) findViewById(R.id.up_right_button);
    mRotateCButton = (ImageButton) findViewById(R.id.rotate_clockwise);
    mRotateCCButton = (ImageButton) findViewById(R.id.rotate_counterclockwise);
    mLeftButton.setOnClickListener(onButtonPressed);
    mUpButton.setOnClickListener(onButtonPressed);
    mDownButton.setOnClickListener(onButtonPressed);
    mRightButton.setOnClickListener(onButtonPressed);
    mUpLeftButton.setOnClickListener(onButtonPressed);
    mUpRightButton.setOnClickListener(onButtonPressed);
    mDownLeftButton.setOnClickListener(onButtonPressed);
    mDownRightButton.setOnClickListener(onButtonPressed);
    mRotateCButton.setOnClickListener(onButtonPressed);
    mRotateCCButton.setOnClickListener(onButtonPressed);
    mPieceView = (PieceView) findViewById(R.id.game_canvas);

    List<Point> gameBoard = new ArrayList<Point>();
    for (int y = 0; y < 8; y++) {
      for (int x = 0; x < 10; x++) {
        gameBoard.add(new Point(x, y));
      }
    }

    mBoardAdapter = new GameBoardAdapter(this, gameBoard);
    mGameGrid.setAdapter(mBoardAdapter);

    mMarkings = new ArrayList<Marking>();
    for (int i = 0; i < 2; i++) {
      boolean isFirstHalf = i == 0;
      mMarkings.add(new Marking(0, 0, 0, isFirstHalf));
      mMarkings.add(new Marking(0, 0, 1, isFirstHalf));
      mMarkings.add(new Marking(0, 0, 2, isFirstHalf));
      mMarkings.add(new Marking(0, 0, 3, isFirstHalf));

      mMarkings.add(new Marking(1, 1, 0, isFirstHalf));
      mMarkings.add(new Marking(0, 8, 0, isFirstHalf));

      mMarkings.add(new Marking(1, 9, 0, isFirstHalf));
      mMarkings.add(new Marking(1, 9, 1, isFirstHalf));
      mMarkings.add(new Marking(1, 9, 2, isFirstHalf));
      mMarkings.add(new Marking(1, 9, 3, isFirstHalf));
    }
    mPieceView.setMarkings(mMarkings);

    gameSetup();
  }

  private void gameSetup() {
    mPlayerTurn = 0;
    mSelectedPiece = null;

    mPieces = new ArrayList<Piece>();
    for (int i = 0; i < 2; i++) {
      boolean isFirstHalf = i == 0;

      mSpinxs[i] = new Piece(0, 0, 0, CornerDirection.RIGHT_DOWN, PieceType.SPHINX, isFirstHalf);
      mPieces.add(mSpinxs[i]);

      mPieces.add(new Piece(0, 4, 0, CornerDirection.RIGHT_DOWN, PieceType.ANUBIS, isFirstHalf));
      mPieces.add(new Piece(0, 5, 0, CornerDirection.RIGHT_DOWN, PieceType.PHARAOH, isFirstHalf));
      mPieces.add(new Piece(0, 6, 0, CornerDirection.RIGHT_DOWN, PieceType.ANUBIS, isFirstHalf));
      mPieces.add(new Piece(0, 7, 0, CornerDirection.RIGHT_DOWN, PieceType.PYRAMID, isFirstHalf));

      mPieces.add(new Piece(0, 2, 1, CornerDirection.LEFT_DOWN, PieceType.PYRAMID, isFirstHalf));

      mPieces.add(new Piece(1, 3, 2, CornerDirection.LEFT_UP, PieceType.PYRAMID, isFirstHalf));

      mPieces.add(new Piece(0, 0, 3, CornerDirection.RIGHT_UP, PieceType.PYRAMID, isFirstHalf));
      mPieces.add(new Piece(1, 2, 3, CornerDirection.LEFT_DOWN, PieceType.PYRAMID, isFirstHalf));
      mPieces.add(new Piece(0, 4, 3, CornerDirection.LEFT_DOWN, PieceType.SCARAB, isFirstHalf));
      mPieces.add(new Piece(0, 5, 3, CornerDirection.RIGHT_DOWN, PieceType.SCARAB, isFirstHalf));
      mPieces.add(new Piece(0, 7, 3, CornerDirection.RIGHT_DOWN, PieceType.PYRAMID, isFirstHalf));
      mPieces.add(new Piece(1, 9, 3, CornerDirection.LEFT_UP, PieceType.PYRAMID, isFirstHalf));
    }

    mPieceView.setPieces(mPieces);
    mPieceView.invalidate();
  }

  private final OnClickListener onButtonPressed = new OnClickListener() {
    @Override
    public void onClick(View v) {
      boolean successfulMove = false;
      if (mSelectedPiece != null) {
        switch (v.getId()) {
          case R.id.left_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() - 1, mSelectedPiece.getYPos()))
                && mSelectedPiece.move(-1, 0)) {
              successfulMove = true;
            }
            break;
          case R.id.right_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() + 1, mSelectedPiece.getYPos()))
                && mSelectedPiece.move(1, 0)) {
              successfulMove = true;
            }
            break;
          case R.id.down_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos(), mSelectedPiece.getYPos() + 1))
                && mSelectedPiece.move(0, 1)) {
              successfulMove = true;
            }
            break;
          case R.id.up_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos(), mSelectedPiece.getYPos() - 1))
                && mSelectedPiece.move(0, -1)) {
              successfulMove = true;
            }
            break;
          case R.id.up_left_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() - 1, mSelectedPiece.getYPos() - 1))
                && mSelectedPiece.move(-1, -1)) {
              successfulMove = true;
            }
            break;
          case R.id.up_right_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() + 1, mSelectedPiece.getYPos() - 1))
                && mSelectedPiece.move(1, -1)) {
              successfulMove = true;
            }
            break;
          case R.id.down_left_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() - 1, mSelectedPiece.getYPos() + 1))
                && mSelectedPiece.move(-1, 1)) {
              successfulMove = true;
            }
            break;
          case R.id.down_right_button:
            if (!containsPiece(new Point(mSelectedPiece.getXPos() + 1, mSelectedPiece.getYPos() + 1))
                && mSelectedPiece.move(1, 1)) {
              successfulMove = true;
            }
            break;
          case R.id.rotate_clockwise:
            if (mSelectedPiece.rotate(true)) {
              successfulMove = true;
            }
            break;
          case R.id.rotate_counterclockwise:
            if (mSelectedPiece.rotate(false)) {
              successfulMove = true;
            }
            break;
        }
        if (successfulMove) {
          fireLaser();
          deselectSelectedPiece();
          mPlayerTurn = mPlayerTurn == 1 ? 0 : 1;
          mPlayerTurnText.setText((mPlayerTurn == 0 ? "Red" : "Blue") + " Players Turn");
          mPieceView.invalidate();
          final Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              hideLaser();
              mPieceView.invalidate();
            }
          }, 500);
        } else {
          Toast.makeText(MainActivity.this, "You can't do that", Toast.LENGTH_SHORT).show();
        }
      }
    }
  };

  private void fireLaser() {
    Piece sphinx = mSpinxs[mPlayerTurn];
    List<LaserPoint> laserPoints = new ArrayList<LaserPoint>();

    Direction direction = Direction.values()[sphinx.getDirection().ordinal()];
    int curX = sphinx.getXPos();
    int curY = sphinx.getYPos();
    boolean collision = false;

    while (!collision) {
      int prevX = curX;
      int prevY = curY;

      curX += direction.x;
      curY += direction.y;
      laserPoints.add(new LaserPoint(prevX, prevY, curX, curY));
      if (curX >= 10 || curX < 0 || curY >= 8 || curY < 0) {
        collision = true;
        break;
      }
      for (Piece piece : mPieces) {
        if (piece.getXPos() == curX && piece.getYPos() == curY) {
          Direction newDir = piece.checkCollision(direction);
          if (newDir == null) {
            switch (piece.getPieceType()) {
              case PYRAMID:
              case ANUBIS:
                mPieces.remove(piece);
                break;
              case PHARAOH:
                mPieces.remove(piece);
                // TODO: show dialog with restart
                Toast.makeText(this, "Player " + ((piece.getPlayerNum() - 1) * -1 + 1) + " wins!",
                    Toast.LENGTH_LONG).show();
                gameSetup();
                break;
              case SPHINX:
              case SCARAB:
                break;
            }
            collision = true;
            break;
          } else {
            if (piece.getPieceType() == PieceType.ANUBIS) {
              collision = true;
              break;
            }
            direction = newDir;
          }
        }
      }
    }
    mPieceView.setLaserPoints(laserPoints);

  }

  private void hideLaser() {
    mPieceView.setLaserPoints(new ArrayList<LaserPoint>());
  }

  private boolean containsPiece(Point point) {
    for (Piece piece : mPieces) {
      if (point.x == piece.getXPos() && point.y == piece.getYPos()) {
        return true;
      }
    }
    for (Marking marking : mMarkings) {
      if (marking.getPlayerNum() != mPlayerTurn && point.x == marking.getXPos()
          && point.y == marking.getYPos()) {
        return true;
      }
    }
    return false;
  }

  private void updateNewSelectedPiece() {
    for (int i = 0; i < mGameGrid.getChildCount(); i++) {
      View view = mGameGrid.getChildAt(i);
      Point viewPoint = (Point) view.getTag();
      if (viewPoint.x == mSelectedPiece.getXPos() && viewPoint.y == mSelectedPiece.getYPos()) {
        view.setSelected(true);
      } else {
        view.setSelected(false);
      }
    }
  }

  private void deselectSelectedPiece() {
    for (int i = 0; i < mGameGrid.getChildCount(); i++) {
      View view = mGameGrid.getChildAt(i);
      view.setSelected(false);
    }
  }

  private class GameBoardAdapter extends ArrayAdapter<Point> {

    public GameBoardAdapter(Context context, List<Point> objects) {
      super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View boardButton;
      if (convertView == null) {
        boardButton = getLayoutInflater().inflate(R.layout.board_square, null);
        int cellSize = (int) getResources().getDimension(R.dimen.cell_size);
        boardButton.setLayoutParams(new GridView.LayoutParams(cellSize, cellSize));
      } else {
        boardButton = convertView;
      }
      boardButton.setTag(getItem(position));
      boardButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
          Point point = (Point) view.getTag();
          for (Piece piece : mPieces) {
            if (point.x == piece.getXPos() && point.y == piece.getYPos()
                && piece.getPlayerNum() == mPlayerTurn) {
              if (!view.isSelected()) {
                deselectSelectedPiece();
                view.setSelected(true);
                mSelectedPiece = piece;
              } else {
                view.setSelected(false);
                mSelectedPiece = null;
              }
            }
          }
        }
      });
      return boardButton;
    }
  }
}
