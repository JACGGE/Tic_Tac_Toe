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
    // ECHO Prefrencias
    // TODO Start-Up pantalla
    // TODO Inteligencia Artificial
    // TODO WIFI 2 Players
    // TODO Ingles comentarios

    /***************************************************************************************************
     *  Create variables to reference the Views
     ***************************************************************************************************/
    private Button mButton_1_Player;
    private Button mButton_2_Player;
    private Button mButton_Settings;
    private RadioButton mRadioButton_Easy;
    private RadioButton mRadioButton_Medium;
    private RadioButton mRadioButton_Impossible;


    /***************************************************************************************************
     *  Create Object array of the cell class
     ***************************************************************************************************/
    private Cell[] mArrayCells = new Cell[9];


    /***************************************************************************************************
     *  Create Object array of the SetCells class
     ***************************************************************************************************/
    private SetCells[] mArraySets = new SetCells[8];


    /***************************************************************************************************
     *  Create Object array of the Player class
     ***************************************************************************************************/
    private Player[] mArrayPlayer = new Player[3];


    /***************************************************************************************************
     *  Internal use variables
     ***************************************************************************************************/
    private int Number_Of_Playeres;  // Number of players of the game in use
    private int Turn = 0;            // We start with turn "0" (nobody can play)
    private int vacant_Cells;        // Number of free cells, to know when the game is over


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/***************************************************************************************************
 *      We match the variables to the corresponding View
 ***************************************************************************************************/
        mButton_1_Player = findViewById(R.id.id_butoon_1player);
        mButton_2_Player = findViewById(R.id.id_butoon_2players);
        mButton_Settings = findViewById(R.id.id_butoonS);
        mRadioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        mRadioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        mRadioButton_Impossible = findViewById(R.id.id_radio_button_impossible_difficulty);


/***************************************************************************************************
 *      We fill the elements of the mArrayCells with references to the cells
 *      We create the cells in the same line
 ***************************************************************************************************/
        mArrayCells[0] = new Cell((ImageView) findViewById(R.id.id_iv_a1), 3);
        mArrayCells[1] = new Cell((ImageView) findViewById(R.id.id_iv_a2), 2);
        mArrayCells[2] = new Cell((ImageView) findViewById(R.id.id_iv_a3), 3);

        mArrayCells[3] = new Cell((ImageView) findViewById(R.id.id_iv_b1), 2);
        mArrayCells[4] = new Cell((ImageView) findViewById(R.id.id_iv_b2), 4);
        mArrayCells[5] = new Cell((ImageView) findViewById(R.id.id_iv_b3), 2);

        mArrayCells[6] = new Cell((ImageView) findViewById(R.id.id_iv_c1), 3);
        mArrayCells[7] = new Cell((ImageView) findViewById(R.id.id_iv_c2), 2);
        mArrayCells[8] = new Cell((ImageView) findViewById(R.id.id_iv_c3), 3);


/***************************************************************************************************
 *      We create the SetCells with references to the cells that make up the set
 *      Sets for the rows 0, 1 y 2
 ***************************************************************************************************/
        mArraySets[0] = new SetCells(mArrayCells[0], mArrayCells[1], mArrayCells[2]);
        mArraySets[1] = new SetCells(mArrayCells[3], mArrayCells[4], mArrayCells[5]);
        mArraySets[2] = new SetCells(mArrayCells[6], mArrayCells[7], mArrayCells[8]);


/***************************************************************************************************
 *      Sets for the columns  0, 1 y 2
 ***************************************************************************************************/
        mArraySets[3] = new SetCells(mArrayCells[0], mArrayCells[3], mArrayCells[6]);
        mArraySets[4] = new SetCells(mArrayCells[1], mArrayCells[4], mArrayCells[7]);
        mArraySets[5] = new SetCells(mArrayCells[2], mArrayCells[5], mArrayCells[8]);


/***************************************************************************************************
 *      Sets for diagonals 0 y 1
 ***************************************************************************************************/
        mArraySets[6] = new SetCells(mArrayCells[0], mArrayCells[4], mArrayCells[8]);
        mArraySets[7] = new SetCells(mArrayCells[2], mArrayCells[4], mArrayCells[6]);


/***************************************************************************************************
 *      We fill in the elements of the PlayersArray
 ***************************************************************************************************/
        String hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer0)));
        mArrayPlayer[0] = new Player(getString(R.string.name_Player0), hexColor, 0);

        hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer1)));
        mArrayPlayer[1] = new Player(getString(R.string.name_Player1), hexColor, 1);

        hexColor = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer2)));
        mArrayPlayer[2] = new Player(getString(R.string.name_Player2), hexColor, 4);


/***************************************************************************************************
 *      Recharge of variables and arrays when returning from a deletion due to UI change
 ***************************************************************************************************/
        if (savedInstanceState != null) {
            int[] ArrayOwner = new int[9];
            ArrayOwner = savedInstanceState.getIntArray(getString(R.string.Owner));
            for (int n = 0; n < 9; n++) {
                mArrayCells[n].setOwner(ArrayOwner[n]);
                putColorToCell(mArrayCells[n]);
            }
            enabled_buttons(savedInstanceState.getBoolean(getString(R.string.IsButtonsEnabled), true));
            Number_Of_Playeres = savedInstanceState.getInt(getString(R.string.NumberOfPlayers), 1);
            Turn = savedInstanceState.getInt(getString(R.string.Turn), 1);
            vacant_Cells = savedInstanceState.getInt(getString(R.string.VacantCells), 9);
        }
    }


    /***************************************************************************************************
     *  Storage of variables and arrays when a deletion due to UI change is about
     ***************************************************************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] ArrayOwner = new int[9];
        for (int n = 0; n < 9; n++) {
            ArrayOwner[n] = mArrayCells[n].getOwner();
        }
        outState.putIntArray(getString(R.string.Owner), ArrayOwner);
        outState.putBoolean(getString(R.string.IsButtonsEnabled), mButton_1_Player.isEnabled());
        outState.putInt(getString(R.string.NumberOfPlayers), Number_Of_Playeres);
        outState.putInt(getString(R.string.Turn), Turn);
        outState.putInt(getString(R.string.VacantCells), vacant_Cells);
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
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
        mArrayPlayer[1].setName(sharedPreferences.getString(getString(R.string.Pla1_name_key), "No leido"));
        mArrayPlayer[2].setName(sharedPreferences.getString(getString(R.string.Pla2_name_key), "No leido"));
        int col = Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla1_color_key), "1"));
        String hexColor1 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer1)));
        String hexColor3 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer3)));
        if (col == 1) {
            mArrayPlayer[1].setColor(hexColor1);
        } else if (col == 2) {
            mArrayPlayer[1].setColor(hexColor3);
        }
        col = Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla2_color_key), "1"));
        String hexColor2 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer2)));
        String hexColor4 = String.format("#%06X", (0xFFFFFF & getColor(R.color.colorPlayer4)));
        if (col == 1) {
            mArrayPlayer[2].setColor(hexColor2);
        } else if (col == 2) {
            mArrayPlayer[2].setColor(hexColor4);
        }
    }


    /***************************************************************************************************
     * When the 1-player or 2-player button is pressed
     ***************************************************************************************************/
    public void buttonClick(View view) {
        if (view.getId() == mButton_Settings.getId()) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return;
        }
        Number_Of_Playeres = 1;
        if (view.getId() == mButton_2_Player.getId()) Number_Of_Playeres = 2;
        // Inabilitar los botones y los radio_botones
        enabled_buttons(false);
        // Poner el propietario ="0"  y el valor = 0 en todas la Cells
        for (Cell Cell : mArrayCells) {
            Cell.setOwner(0);
            putColorToCell(Cell);
        }
        // Asignar el turno al Player 1 e inicializar nemero de Cells libres
        Turn = 1;
        vacant_Cells = 9;
    }


    /***************************************************************************************************
     * Obtiene el color del jugador propietario de la celda
     * Aplica este color al BackgroundColor de la ImageView de la celda
     ***************************************************************************************************/
    public void putColorToCell(Cell Cell) {
        String colJug = mArrayPlayer[Cell.getOwner()].getColor();
        Cell.getImageView().setBackgroundColor(Color.parseColor(colJug));
    }


    /***************************************************************************************************
     * Pone todos los Botones y radio botones Enabled o Disabled segun parametro recibido
     ***************************************************************************************************/
    public void enabled_buttons(boolean enabled) {
        mButton_1_Player.setEnabled(enabled);
        mButton_2_Player.setEnabled(enabled);
        mButton_Settings.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Medium.setEnabled(enabled);
        mRadioButton_Impossible.setEnabled(enabled);
    }


    /***************************************************************************************************
     * Cuando un jugador selecciona una celda,
     * 1    -> Comprobamos que la celda no sea null
     * 2    -> Comprobamos que la celda no tenga propietario
     * 3    -> Comprobamos que la partida este comenzada ( Turn !=0)
     * 4    -> Adjudicamos a la celda como propietario al jugador que tiene el turno
     * 5    -> Ponemos el color correspondiente en la celda
     * 6    -> Comprobamos si la jugada convierte al jugador en ganador
     * 7    -> Calculamos y actualizamos cuantas celdes libres quedan
     * 8    -> Cambiamos al siguiente turno
     ***************************************************************************************************/
    public void juegaEnCell(Cell Cell) {
        if (Cell == null || Cell.getOwner() != 0 || Turn == 0) return;
        Cell.setOwner(Turn);
        putColorToCell(Cell);
        comprueba_si_ganador();
        Cells_libres();
        cambio_de_turno();
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public void casilla_pulsada(View view) {
        if (Turn != 0) {
            int index = Integer.valueOf(view.getTag().toString());
            juegaEnCell(mArrayCells[index]);
        }
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public void maquina_juega() {
        Cell Cell = busca_mejor_Cell_para_maquina();
        if (Cell == null) {
            do {
            }
            while
            ((Cell = mArrayCells[new Random().nextInt(9)]).getOwner() != 0);
        }
        juegaEnCell(Cell);
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public void Cells_libres() {
        vacant_Cells--;
        if (vacant_Cells < 1) {
            vacant_Cells = 0;
            Turn = 0;
            enabled_buttons(true);
        }
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public void cambio_de_turno() {
        Turn++;
        if (Turn > 2) Turn = 1;
        if (Turn == 2 && Number_Of_Playeres == 1) {
            maquina_juega();
        }
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public void comprueba_si_ganador() {
        for (int n = 0; n < mArraySets.length; n++) {
            for (int Player = 1; Player < 3; Player++) {
                if (mArraySets[n].getSuma() == (Player * Player) * 3) {
                Toast.makeText(this, getString(R.string.winner)
                            + mArrayPlayer[Player].getName(), Toast.LENGTH_LONG).show();
                    vacant_Cells = 0;
                    break;
                }
            }
        }
    }


    /***************************************************************************************************
     *
     ***************************************************************************************************/
    public Cell busca_mejor_Cell_para_maquina() {
        int inicio = 1;
        if (!mRadioButton_Easy.isChecked()) inicio = 2;
        for (int Player = inicio; Player > 0; Player--) {
            for (int n = 0; n < mArraySets.length; n++) {
                 if (mArraySets[n].getSuma() == (Player * Player) * 2) {
                    for (int m = 0; m < mArraySets[n].getArrayCellsEnSet().length; m++) {
                        if (mArraySets[n].getCellEnSet(m).getOwner() == 0) {
                            return mArraySets[n].getCellEnSet(m);
                        }
                    }
                }
            }
        }

        // IF IT IS NOT THE ABOVE, LOOK FOR THE EMPTY CELL WITH MORE WEIGHT AND IT OCCUPIES IT
        Cell Cell = null;
        if (!mRadioButton_Impossible.isChecked()) return Cell;
        int maxPeso = 0;
        int inicia = new Random().nextInt(8);
        int m;
        for (int n = inicia; n < inicia + 9; n++) {
            if (n < 9) {
                m = n;
            } else {
                m = n % 8;
            }
            if (mArrayCells[m].getOwner() == 0 && mArrayCells[m].getWeigth() > maxPeso) {
                maxPeso = mArrayCells[m].getWeigth();
                Cell = mArrayCells[m];
            }
        }
        return Cell;
    }
}
