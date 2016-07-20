package com.example.laserchess;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;


public class PieceAdapter extends ArrayAdapter<Piece> {

  Context mContext;

  public PieceAdapter(Context context, int textViewResourceId, List<Piece> objects) {
    super(context, textViewResourceId, objects);
    mContext = context;
  }

  // @Override
  // public View getView(int position, View convertView, ViewGroup parent) {
  // PieceView pieceView;
  // if (convertView == null) { // if it's not recycled, initialize some attributes
  // pieceView = new PieceView(mContext, getItem(position));
  // pieceView.setLayoutParams(new GridView.LayoutParams(50, 50));
  // } else {
  // pieceView = (PieceView) convertView;
  // }
  // pieceView.invalidate();
  // return pieceView;
  // }

}
