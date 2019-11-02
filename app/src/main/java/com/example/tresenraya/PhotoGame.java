/*
 * Copyright (C) 2019 J.A.Cucurull
 * All rights reserved
 *
 * PhotoGame Class
 * Exceptionally developed for training and sample purposes
 */

package com.example.tresenraya;

public class PhotoGame {
    private int mCellsState;
    private int mPlayer;
    private int mWinner;


    public PhotoGame(int cellsState, int player) {
        mCellsState = cellsState;
        mPlayer = player;
    }

    public int getCellsState() {return mCellsState;}

    public int getPlayer() {return mPlayer;}

    public int getWinner() {return mWinner;}

    public void setWinner(int winner) {mWinner = winner;}
}
