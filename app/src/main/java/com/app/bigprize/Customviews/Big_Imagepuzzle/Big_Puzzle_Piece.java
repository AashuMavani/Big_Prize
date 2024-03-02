package com.app.bigprize.Customviews.Big_Imagepuzzle;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

public class Big_Puzzle_Piece extends AppCompatImageView {
    public int xCoord;
    public int yCoord;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;

    public Big_Puzzle_Piece(Context context) {
        super(context);
    }
}