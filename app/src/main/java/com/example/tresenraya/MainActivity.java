package com.example.tresenraya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Crear variables encapsuladas para refenciar las View
    private Button mButton_1_Jugador;
    private Button mButton_2_Jugadores;
    private RadioButton mRadioButton_Easy;
    private RadioButton mRadioButton_Medium;
    private RadioButton mRadioButton_Impossible;

    // Crear Array de objetos de la clase Celda
    private Celda[] mArrayCeldas = new Celda[9];

    // Crear ArrayList de objetos de la clase SetCeldas
    private SetCeldas[] mArraySets = new SetCeldas[8];

    // Crear Array de objetos de la clase Jugador
    private Jugador[] mArrayJugador = new Jugador[3];

    // Variables de uso interno
    private int mNumero_De_Jugadores;   // Numero de jugadores de la partida en uso
    private int mDificulty;             // Grado de dificultad de la partida en uso
    private int mTurno = 0;             // Empezamos con turno "0" (nadie puede jugar)
    private int mCeldas_Libres;         // Número de celdas libres, para saber cuando se acaba

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referenciar las variables a las View correspondienres
        mButton_1_Jugador = findViewById(R.id.id_butoon_1player);
        mButton_2_Jugadores = findViewById(R.id.id_butoon_2players);
        mRadioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        mRadioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        mRadioButton_Impossible = findViewById(R.id.id_radio_button_impossible_difficulty);

        // Rellenamos los elementos del ArrayList con referencias a las celdas
        // Creamos las celdas en la misma linea
        mArrayCeldas[0] = new Celda((ImageView) findViewById(R.id.id_iv_a1), 3);
        mArrayCeldas[1] = new Celda((ImageView) findViewById(R.id.id_iv_a2), 2);
        mArrayCeldas[2] = new Celda((ImageView) findViewById(R.id.id_iv_a3), 3);

        mArrayCeldas[3] = new Celda((ImageView) findViewById(R.id.id_iv_b1), 2);
        mArrayCeldas[4] = new Celda((ImageView) findViewById(R.id.id_iv_b2), 4);
        mArrayCeldas[5] = new Celda((ImageView) findViewById(R.id.id_iv_b3), 2);

        mArrayCeldas[6] = new Celda((ImageView) findViewById(R.id.id_iv_c1), 3);
        mArrayCeldas[7] = new Celda((ImageView) findViewById(R.id.id_iv_c2), 2);
        mArrayCeldas[8] = new Celda((ImageView) findViewById(R.id.id_iv_c3), 3);

        // Creamos los set de celdas con referencias a las celdas

        // Sets para las filas 0, 1 y 2
        mArraySets[0] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[1], mArrayCeldas[2]);
        mArraySets[1] = new SetCeldas(mArrayCeldas[3], mArrayCeldas[4], mArrayCeldas[5]);
        mArraySets[2] = new SetCeldas(mArrayCeldas[6], mArrayCeldas[7], mArrayCeldas[8]);

        // Sets para las columnas  0, 1 y 2
        mArraySets[3] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[3], mArrayCeldas[6]);
        mArraySets[4] = new SetCeldas(mArrayCeldas[1], mArrayCeldas[4], mArrayCeldas[7]);
        mArraySets[5] = new SetCeldas(mArrayCeldas[2], mArrayCeldas[5], mArrayCeldas[8]);

        // Sets para las diagonales 0 y 1
        mArraySets[6] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[4], mArrayCeldas[8]);
        mArraySets[7] = new SetCeldas(mArrayCeldas[2], mArrayCeldas[4], mArrayCeldas[6]);

        // Rellenamos los elementos del ArrayList con referencias a los Jugadores
        mArrayJugador[0] = new Jugador(getString(R.string.name_Player0), getColor(R.color.colorJugador0), 0);
        mArrayJugador[1]= new Jugador(getString(R.string.name_Player1), getColor(R.color.colorJugador1), 1);
        mArrayJugador[2] = new Jugador(getString(R.string.name_Player2), getColor(R.color.colorJugador2), 4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.SettingsMenu){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View view) {
        // Asignar el numero de jugadores "mNumero_De_Jugadores segun boton pulsado"
        mNumero_De_Jugadores = 1;
        if (view.getId() == mButton_2_Jugadores.getId()) mNumero_De_Jugadores = 2;
        // Inabilitar los botones y los radio_botones
        enabled_buttons(false);
        // Poner el propietario ="0"  y el valor = 0 en todas la celdas
        for (Celda celda : mArrayCeldas) {
            celda.setPropietario(0);
            celda.setValor(0);
            ponerColorEnCelda(celda);}
        // Asignar el turno al jugador 1 e inicializar nemero de celdas libres
        mTurno = 1;
        mCeldas_Libres = 9;
    }

    public void ponerColorEnCelda(Celda celda) {
        celda.getImageView().setBackgroundColor(mArrayJugador[celda.getPropietario()].getMcolor());
    }

    public void enabled_buttons(boolean enabled) {
        mButton_1_Jugador.setEnabled(enabled);
        mButton_2_Jugadores.setEnabled(enabled);
        mRadioButton_Easy.setClickable(enabled);
        mRadioButton_Medium.setClickable(enabled);
        mRadioButton_Impossible.setClickable(enabled);
    }

    public void juegaEnCelda(Celda celda) {
        if (celda == null || celda.getPropietario() != 0 || mTurno == 0) return;
        celda.setPropietario(mTurno);
        celda.setValor(mArrayJugador[mTurno].getMvalor());
        ponerColorEnCelda(celda);
        comprueba_si_ganador();
        celdas_libres();
        cambio_de_turno();
    }

    public void casilla_pulsada(View view) {
        if (mTurno != 0) {
            int index = Integer.valueOf(view.getTag().toString());
            juegaEnCelda(mArrayCeldas[index]);}
    }

    public void maquina_juega() {
        Celda celda = busca_mejor_celda_para_maquina();
          if (celda == null) {
            do {}
            while
            ((celda = mArrayCeldas[new Random().nextInt(9)]).getPropietario() != 0);}
        juegaEnCelda(celda);
    }

    public void celdas_libres() {
        mCeldas_Libres--;
        if (mCeldas_Libres < 1){
            mCeldas_Libres = 0;
            mTurno = 0;
            enabled_buttons(true);}
    }

    public void cambio_de_turno() {
        mTurno++;
        if (mTurno > 2) mTurno = 1;
        if (mTurno == 2 && mNumero_De_Jugadores == 1) {
            maquina_juega();}
    }

    public void comprueba_si_ganador() {
        for (int n = 0; n < mArraySets.length; n++) {
            for (int jugador = 1; jugador < 3; jugador ++) {
                if (mArraySets[n].getSuma() == mArrayJugador[jugador].mValor * 3) {
                    Toast.makeText(this, getString(R.string.winner)
                            + mArrayJugador[jugador].getName(), Toast.LENGTH_LONG).show();
                    mCeldas_Libres = 0;
                    break;}}}
    }

    public Celda busca_mejor_celda_para_maquina(){
        int inicio = 1;
        if (!mRadioButton_Easy.isChecked())inicio = 2;
        for (int jugador = inicio; jugador > 0; jugador--) {
            for (int n = 0; n < mArraySets.length; n++) {
                if (mArraySets[n].getSuma() == mArrayJugador[jugador].mValor * 2) {
                    for (int m = 0; m < mArraySets[n].getArrayCeldasEnSet().length; m++) {
                        if (mArraySets[n].getCeldaEnSet(m).getPropietario() == 0) {
                            return mArraySets[n].getCeldaEnSet(m);}}}}}

        // TODO SI NINGUNA DE LAS ANTERIORES, BUSCA LA CELDA VACIA CON MAS PESO Y LA OCUPA
        Celda celda = null;
        if (!mRadioButton_Impossible.isChecked()) return celda;
        int maxPeso = 0;
        int inicia = new Random().nextInt(8);
        int m ;
        for (int n = inicia; n < inicia +9; n++) {
            if ( n < 9){m = n;}else{m = n % 8;}
            if (mArrayCeldas[m].getPropietario() == 0 && mArrayCeldas[m].getPeso() > maxPeso){
                maxPeso = mArrayCeldas[m].getPeso();
                celda = mArrayCeldas[m];}}
        return celda;
    }
}
