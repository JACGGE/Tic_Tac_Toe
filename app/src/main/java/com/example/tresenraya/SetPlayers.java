/*
 * Copyright (C) 2019
 * All rights reserved
 *
 * SetPlayers Class
 * Exceptionally developed for training and sample purposes
 */
package com.example.tresenraya;

public class SetPlayers {
    private Player[] mPlayer = new Player[3];

    SetPlayers(Player player1, Player player2, Player player3){
        mPlayer[0] = player1;
        mPlayer[1] = player2;
        mPlayer[2] = player3;
    }

    void setName(String name, int index) {
        mPlayer[index].setName(name);
    }

    void setColor(String color, int index) {
        mPlayer[index].setColor(color);
    }

    String getName(int index){
        return mPlayer[index].getName();
    }

    String getColor(int index) {
        return mPlayer[index].getColor();
    }
}
