package com.example.tresenraya;

public class SetOfCells {
    private Cell[] cellsEnSet = new Cell[3];

    SetOfCells(Cell Cell0, Cell Cell1, Cell Cell2) {
        cellsEnSet[0] = Cell0;
        cellsEnSet[1] = Cell1;
        cellsEnSet[2] = Cell2;}

    Cell getCell(int n){
        return cellsEnSet[n];
    }

    Cell[] getCells(){
        return cellsEnSet;
    }

    int getSum() {
        int sum = 0;
        for (Cell cell : cellsEnSet) sum += (cell.getOwner() * cell.getOwner());
        return sum;
    }
 }