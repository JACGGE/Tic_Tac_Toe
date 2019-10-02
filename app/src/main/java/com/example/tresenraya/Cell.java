package com.example.tresenraya;

import android.widget.ImageView;

public class Cell {
    private ImageView mImageView;
    private int mOwner;
    private int mWeight;
    private int mValue;

    public Cell(ImageView Cell, int weigth) {
        mImageView = Cell;
        mWeight = weigth;
        mOwner = 0;
        mValue = 0;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {mValue = value;}

    public ImageView getImageView() {return mImageView;}

    public int getOwner() {return mOwner;}

    public void setOwner(int turn) {mOwner = turn;}

    public int getWeigth() {return mWeight;}

    public void setWeigth(int weigth) {
        mWeight = weigth;
    }
}

