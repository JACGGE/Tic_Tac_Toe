package com.example.tresenraya;

import android.widget.ImageView;

public class Celda {
    private ImageView mImageView;
    private int mPropietario;
    private int mPeso;
    private int mValor;

    public Celda(ImageView celda, int peso) {
        mImageView = celda;
        mPeso = peso;
        mPropietario = 0;
        mValor = 0;
    }

    public int getValor() {
        return mValor;
    }

    public void setValor(int valor) {
        mValor = valor;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public int getPropietario() {
         return mPropietario;
    }

    public void setPropietario(int turno) {
        mPropietario = turno;
    }

    public int getPeso() {
        return mPeso;
    }

    public void setPeso(int peso) {
        mPeso = peso;
    }
}

