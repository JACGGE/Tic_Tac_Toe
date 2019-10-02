package com.example.tresenraya;

import java.util.ArrayList;

public class SetCells {
    private Cell[] mArrayCellsEnSet = new Cell[3];

    public SetCells(Cell Cell0, Cell Cell1, Cell Cell2) {
        mArrayCellsEnSet[0] = Cell0;
        mArrayCellsEnSet[1] = Cell1;
        mArrayCellsEnSet[2] = Cell2;}

    public Cell[] getArrayCellsEnSet(){
        return mArrayCellsEnSet;
        }

    public Cell getCellEnSet(int n){
        return mArrayCellsEnSet[n];
    }

    public int getSuma() {
        int suma = 0;
        for (int n = 0; n < mArrayCellsEnSet.length; n ++){
            suma = suma + mArrayCellsEnSet[n].getValue();}
        return suma;
    }
 }
