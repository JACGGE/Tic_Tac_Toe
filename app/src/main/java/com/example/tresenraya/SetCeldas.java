package com.example.tresenraya;

import java.util.ArrayList;

public class SetCeldas {
    private Celda[] mArrayCeldasEnSet = new Celda[3];

    public SetCeldas(Celda celda0, Celda celda1, Celda celda2) {
        mArrayCeldasEnSet[0] = celda0;
        mArrayCeldasEnSet[1] = celda1;
        mArrayCeldasEnSet[2] = celda2;}

    public Celda[] getArrayCeldasEnSet(){
        return mArrayCeldasEnSet;
        }

    public Celda getCeldaEnSet(int n){
        return mArrayCeldasEnSet[n];
    }

    public int getSuma() {
        int suma = 0;
        for (int n = 0; n < mArrayCeldasEnSet.length; n ++){
            suma = suma + mArrayCeldasEnSet[n].getValor();}
        return suma;
    }
 }
