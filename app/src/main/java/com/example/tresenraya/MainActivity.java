package com.example.tresenraya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // ECHO Icono hacerlo y ponerlo
    // ECHO Landscape pantalla
    // CASI Prefrencias
    // TODO Start-Up pantalla
    // TODO Inteligencia Artificial
    // TODO WIFI 2 Jugadores
    // TODO Ingles comentarios

    // Create encapsulated variables to reference the Views
    private Button mButton_1_Jugador;
    private Button mButton_2_Jugadores;
    private Button mButton_Settings;
    private RadioButton mRadioButton_Easy;
    private RadioButton mRadioButton_Medium;
    private RadioButton mRadioButton_Impossible;

    // Create Object array of the Celda class
    private Celda[] mArrayCeldas = new Celda[9];

    // Create Object array of the SetCeldas class
    private SetCeldas[] mArraySets = new SetCeldas[8];

    // Create Object array of the Jugador class
    private Jugador[] mArrayJugador = new Jugador[3];

    // Internal use variables
    private int mNumero_De_Jugadores;   // Number of players of the game in use
    private int mTurno = 0;             // We start with turn "0" (nobody can play)
    private int mCeldas_Libres;         // Number of free cells, to know when the game is over

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We refer to the encapsulated variables to the corresponding View
        mButton_1_Jugador = findViewById(R.id.id_butoon_1player);
        mButton_2_Jugadores = findViewById(R.id.id_butoon_2players);
        mButton_Settings = findViewById(R.id.id_butoonS);
        mRadioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        mRadioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        mRadioButton_Impossible = findViewById(R.id.id_radio_button_impossible_difficulty);

        // We fill the elements of the cell array with references to the cells
        // We create the cells in the same line
        mArrayCeldas[0] = new Celda((ImageView) findViewById(R.id.id_iv_a1), 3);
        mArrayCeldas[1] = new Celda((ImageView) findViewById(R.id.id_iv_a2), 2);
        mArrayCeldas[2] = new Celda((ImageView) findViewById(R.id.id_iv_a3), 3);

        mArrayCeldas[3] = new Celda((ImageView) findViewById(R.id.id_iv_b1), 2);
        mArrayCeldas[4] = new Celda((ImageView) findViewById(R.id.id_iv_b2), 4);
        mArrayCeldas[5] = new Celda((ImageView) findViewById(R.id.id_iv_b3), 2);

        mArrayCeldas[6] = new Celda((ImageView) findViewById(R.id.id_iv_c1), 3);
        mArrayCeldas[7] = new Celda((ImageView) findViewById(R.id.id_iv_c2), 2);
        mArrayCeldas[8] = new Celda((ImageView) findViewById(R.id.id_iv_c3), 3);

        // We create the cell sets with references to the cells

        // Sets for the rows 0, 1 y 2
        mArraySets[0] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[1], mArrayCeldas[2]);
        mArraySets[1] = new SetCeldas(mArrayCeldas[3], mArrayCeldas[4], mArrayCeldas[5]);
        mArraySets[2] = new SetCeldas(mArrayCeldas[6], mArrayCeldas[7], mArrayCeldas[8]);

        // Sets for the columns  0, 1 y 2
        mArraySets[3] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[3], mArrayCeldas[6]);
        mArraySets[4] = new SetCeldas(mArrayCeldas[1], mArrayCeldas[4], mArrayCeldas[7]);
        mArraySets[5] = new SetCeldas(mArrayCeldas[2], mArrayCeldas[5], mArrayCeldas[8]);

        // Sets for diagonals 0 y 1
        mArraySets[6] = new SetCeldas(mArrayCeldas[0], mArrayCeldas[4], mArrayCeldas[8]);
        mArraySets[7] = new SetCeldas(mArrayCeldas[2], mArrayCeldas[4], mArrayCeldas[6]);

        // We fill in the elements of the PlayersArray
        String hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador0)));
        mArrayJugador[0] = new Jugador(getString(R.string.name_Player0),hexColor, 0);

        hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador1)));
        mArrayJugador[1] = new Jugador(getString(R.string.name_Player1),  hexColor, 1);

        hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador2)));
        mArrayJugador[2] = new Jugador(getString(R.string.name_Player2),hexColor, 4);

        restaurarCampos(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] ArrayProp = new int[9];
        int[] ArrayValor = new int[9];
        for (int n = 0; n < 9; n++) {
            ArrayProp[n] = mArrayCeldas[n].getPropietario();
            ArrayValor[n] = mArrayCeldas[n].getValor();
        }
        outState.putIntArray("PROPS", ArrayProp);
        outState.putIntArray("VALOR", ArrayValor);
        outState.putBoolean("ENB1", mButton_1_Jugador.isEnabled());
        outState.putInt("NUMJUG", mNumero_De_Jugadores);
        outState.putInt("TURNO", mTurno);
        outState.putInt("CELFREE", mCeldas_Libres);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mArrayJugador[1].setName(sharedPreferences.getString(getString(R.string.Pla1_name_key), "No leido"));
        mArrayJugador[2].setName(sharedPreferences.getString(getString(R.string.Pla2_name_key), "No leido"));
        int col = Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla1_color_key),"1"));
        String hexColor1 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador1)));
        String hexColor3 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador3)));
        if (col == 1 ){
            mArrayJugador[1].setMcolor(hexColor1);
        }else if (col == 2){
            mArrayJugador[1].setMcolor(hexColor3);
        }
        col = Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla2_color_key),"1"));
        String hexColor2 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador2)));
        String hexColor4 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorJugador4)));
        if (col == 1 ){
            mArrayJugador[2].setMcolor(hexColor2);
        }else if (col == 2){
            mArrayJugador[2].setMcolor(hexColor4);
        }
    }

    private void restaurarCampos(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int[] ArrayProp = new int[9];
            ArrayProp = savedInstanceState.getIntArray("PROPS");
            int[] ArrayValor = new int[9];
            ArrayValor = savedInstanceState.getIntArray("VALOR");
            for (int n = 0; n < 9; n++) {
                mArrayCeldas[n].setPropietario(ArrayProp[n]);
                mArrayCeldas[n].setValor(ArrayValor[n]);
                ponerColorEnCelda(mArrayCeldas[n]);
            }
            enabled_buttons(savedInstanceState.getBoolean("ENB1", true));
            mNumero_De_Jugadores = savedInstanceState.getInt("NUMJUG", 1);
            mTurno = savedInstanceState.getInt("TURNO", 1);
            mCeldas_Libres = savedInstanceState.getInt("CELFREE", 9);
        }
    }

    public void buttonClick(View view) {
        if (view.getId() == mButton_Settings.getId()) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return;
        }
        mNumero_De_Jugadores = 1;
        if (view.getId() == mButton_2_Jugadores.getId()) mNumero_De_Jugadores = 2;
        // Inabilitar los botones y los radio_botones
        enabled_buttons(false);
        // Poner el propietario ="0"  y el valor = 0 en todas la celdas
        for (Celda celda : mArrayCeldas) {
            celda.setPropietario(0);
            celda.setValor(0);
            ponerColorEnCelda(celda);
        }
        // Asignar el turno al jugador 1 e inicializar nemero de celdas libres
        mTurno = 1;
        mCeldas_Libres = 9;
    }

    public void ponerColorEnCelda(Celda celda) {
        String colJug = mArrayJugador[celda.getPropietario()].getMcolor();
        celda.getImageView().setBackgroundColor(Color.parseColor(colJug));
    }

    public void enabled_buttons(boolean enabled) {
        mButton_1_Jugador.setEnabled(enabled);
        mButton_2_Jugadores.setEnabled(enabled);
        mButton_Settings.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Medium.setEnabled(enabled);
        mRadioButton_Impossible.setEnabled(enabled);
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
            juegaEnCelda(mArrayCeldas[index]);
        }
    }

    public void maquina_juega() {
        Celda celda = busca_mejor_celda_para_maquina();
        if (celda == null) {
            do {
            }
            while
            ((celda = mArrayCeldas[new Random().nextInt(9)]).getPropietario() != 0);
        }
        juegaEnCelda(celda);
    }

    public void celdas_libres() {
        mCeldas_Libres--;
        if (mCeldas_Libres < 1) {
            mCeldas_Libres = 0;
            mTurno = 0;
            enabled_buttons(true);
        }
    }

    public void cambio_de_turno() {
        mTurno++;
        if (mTurno > 2) mTurno = 1;
        if (mTurno == 2 && mNumero_De_Jugadores == 1) {
            maquina_juega();
        }
    }

    public void comprueba_si_ganador() {
        for (int n = 0; n < mArraySets.length; n++) {
            for (int jugador = 1; jugador < 3; jugador++) {
                if (mArraySets[n].getSuma() == mArrayJugador[jugador].getMvalor() * 3) {
                    Toast.makeText(this, getString(R.string.winner)
                            + mArrayJugador[jugador].getName(), Toast.LENGTH_LONG).show();
                    mCeldas_Libres = 0;
                    break;
                }
            }
        }
    }

    public Celda busca_mejor_celda_para_maquina() {
        int inicio = 1;
        if (!mRadioButton_Easy.isChecked()) inicio = 2;
        for (int jugador = inicio; jugador > 0; jugador--) {
            for (int n = 0; n < mArraySets.length; n++) {
                if (mArraySets[n].getSuma() == mArrayJugador[jugador].getMvalor() * 2) {
                    for (int m = 0; m < mArraySets[n].getArrayCeldasEnSet().length; m++) {
                        if (mArraySets[n].getCeldaEnSet(m).getPropietario() == 0) {
                            return mArraySets[n].getCeldaEnSet(m);
                        }
                    }
                }
            }
        }

        // SI NINGUNA DE LAS ANTERIORES, BUSCA LA CELDA VACIA CON MAS PESO Y LA OCUPA
        Celda celda = null;
        if (!mRadioButton_Impossible.isChecked()) return celda;
        int maxPeso = 0;
        int inicia = new Random().nextInt(8);
        int m;
        for (int n = inicia; n < inicia + 9; n++) {
            if (n < 9) {
                m = n;
            } else {
                m = n % 8;
            }
            if (mArrayCeldas[m].getPropietario() == 0 && mArrayCeldas[m].getPeso() > maxPeso) {
                maxPeso = mArrayCeldas[m].getPeso();
                celda = mArrayCeldas[m];
            }
        }
        return celda;
    }
}
