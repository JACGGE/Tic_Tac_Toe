package com.example.tresenraya;


public class SetCells {
    private Cell[] arrayCellsEnSet = new Cell[3];

    public SetCells(Cell Cell0, Cell Cell1, Cell Cell2) {
        arrayCellsEnSet[0] = Cell0;
        arrayCellsEnSet[1] = Cell1;
        arrayCellsEnSet[2] = Cell2;}

    public Cell[] getArrayCellsEnSet(){
        return arrayCellsEnSet;
        }

    public Cell getCellEnSet(int n){
        return arrayCellsEnSet[n];
    }

    public int getSuma() {
        int suma = 0;
        for (int n = 0; n < arrayCellsEnSet.length; n ++){
            suma = suma + (arrayCellsEnSet[n].getOwner() * arrayCellsEnSet[n].getOwner());}
        return suma;
    }
 }
