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

    /***********************************************************************************************
     *  Create variables to reference the Views
     **********************************************************************************************/
    private Button mButton_1_Player;
    private Button mButton_2_Player;
    private Button mButton_Settings;
    private RadioButton mRadioButton_Easy;
    private RadioButton mRadioButton_Medium;
    private RadioButton mRadioButton_Impossible;


    /***********************************************************************************************
     *  Create Object array of the cell class
     **********************************************************************************************/
    private Cell[] mArrayCells = new Cell[9];


    /***********************************************************************************************
     *  Create Object array of the SetCells class
     **********************************************************************************************/
    private SetCells[] mArraySets = new SetCells[8];


    /***********************************************************************************************
     *  Create Object array of the Player class
     **********************************************************************************************/
    private Player[] mArrayPlayer = new Player[3];


    /***********************************************************************************************
     *  Internal use variables
     **********************************************************************************************/
    private int Number_Of_Players;  // Number of players of the game in use
    private int Turn = 0;            // We start with turn "0" (nobody can play)
    private int vacant_Cells;        // Number of free cells, to know when the game is over


    /***********************************************************************************************
     *  We match the variables to the corresponding View
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton_1_Player = findViewById(R.id.id_butoon_1player);
        mButton_2_Player = findViewById(R.id.id_butoon_2players);
        mButton_Settings = findViewById(R.id.id_butoonS);
        mRadioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        mRadioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        mRadioButton_Impossible = findViewById(R.id.id_radio_button_impossible_difficulty);


/***************************************************************************************************
 *      We fill the elements of the mArrayCells with references to the cells
 *      We create the cells in the same line
 **************************************************************************************************/
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
 **************************************************************************************************/
        mArraySets[0] = new SetCells(mArrayCells[0], mArrayCells[1], mArrayCells[2]);
        mArraySets[1] = new SetCells(mArrayCells[3], mArrayCells[4], mArrayCells[5]);
        mArraySets[2] = new SetCells(mArrayCells[6], mArrayCells[7], mArrayCells[8]);


/***************************************************************************************************
 *      Sets for the columns  0, 1 y 2
 **************************************************************************************************/
        mArraySets[3] = new SetCells(mArrayCells[0], mArrayCells[3], mArrayCells[6]);
        mArraySets[4] = new SetCells(mArrayCells[1], mArrayCells[4], mArrayCells[7]);
        mArraySets[5] = new SetCells(mArrayCells[2], mArrayCells[5], mArrayCells[8]);


/***************************************************************************************************
 *      Sets for diagonals 0 y 1
 **************************************************************************************************/
        mArraySets[6] = new SetCells(mArrayCells[0], mArrayCells[4], mArrayCells[8]);
        mArraySets[7] = new SetCells(mArrayCells[2], mArrayCells[4], mArrayCells[6]);


/***************************************************************************************************
 *      We fill in the elements of the PlayersArray
 **************************************************************************************************/
        mArrayPlayer[0] = new Player(getString(R.string.name_Player0), getString(R.string.Color0));
        mArrayPlayer[1] = new Player(getString(R.string.name_Player1), getString(R.string.Color3));
        mArrayPlayer[2] = new Player(getString(R.string.name_Player2), getString(R.string.Color1));


/***************************************************************************************************
 *      Recharge of variables and arrays when returning from a deletion due to UI change
 **************************************************************************************************/
        if (savedInstanceState != null) {
            onResume(); /** To recharge Preferences */
            int[] ArrayOwner = new int[9];
            ArrayOwner = savedInstanceState.getIntArray(getString(R.string.Owner));
            for (int n = 0; n < 9; n++) {
                mArrayCells[n].setOwner(ArrayOwner[n]);
                putColorToCell(mArrayCells[n]);
            }
            enabled_buttons(savedInstanceState.getBoolean
                    (getString(R.string.IsButtonsEnabled), true));
            Number_Of_Players = savedInstanceState.getInt
                    (getString(R.string.NumberOfPlayers), 1);
            Turn = savedInstanceState.getInt(getString(R.string.Turn), 1);
            vacant_Cells = savedInstanceState.getInt(getString(R.string.VacantCells), 9);
        }
    }


    /***********************************************************************************************
     *  Storage of variables and arrays when a deletion due to UI change is about
     **********************************************************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] ArrayOwner = new int[9];
        for (int n = 0; n < 9; n++) {
            ArrayOwner[n] = mArrayCells[n].getOwner();
        }
        outState.putIntArray(getString(R.string.Owner), ArrayOwner);
        outState.putBoolean(getString(R.string.IsButtonsEnabled), mButton_1_Player.isEnabled());
        outState.putInt(getString(R.string.NumberOfPlayers), Number_Of_Players);
        outState.putInt(getString(R.string.Turn), Turn);
        outState.putInt(getString(R.string.VacantCells), vacant_Cells);
    }


     @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                /******************************************************************
                 *  Set the content to appear under the system bars so that the
                 *  content doesn't resize when the system bars hide and show.
                 ******************************************************************/
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 /*****************************************************************
                  *  Hide the nav bar and status bar
                  *****************************************************************/
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        /*******************************************************************************************
         * Recupera las preferencias sobre las matrices mArrayPlayer[?]
         ******************************************************************************************/
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        mArrayPlayer[1].setName(sharedPreferences.getString(getString(R.string.Pla1_name_key), "No leido"));
        mArrayPlayer[2].setName(sharedPreferences.getString(getString(R.string.Pla2_name_key), "No leido"));

        switch (Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla1_color_key), "1"))){
            case 1:
                mArrayPlayer[1].setColor(getString(R.string.Color1));
                break;
            case 2:
                mArrayPlayer[1].setColor(getString(R.string.Color2));
                break;
             case 3:
                 mArrayPlayer[1].setColor(getString(R.string.Color3));
                 break;
             case 4:
                 mArrayPlayer[1].setColor(getString(R.string.Color4));
                 break;
            default: break;
         }

         switch (Integer.valueOf(sharedPreferences.getString(getString(R.string.Pla2_color_key), "2"))){
             case 1:
                 mArrayPlayer[2].setColor(getString(R.string.Color1));
                 break;
             case 2:
                 mArrayPlayer[2].setColor(getString(R.string.Color2));
                 break;
             case 3:
                 mArrayPlayer[2].setColor(getString(R.string.Color3));
                 break;
             case 4:
                 mArrayPlayer[2].setColor(getString(R.string.Color4));
                 break;
             default: break;
         }
     }


    /***********************************************************************************************
     * When the 1-player or 2-player button is pressed
     **********************************************************************************************/
    public void buttonClick(View view) {
        if (view.getId() == mButton_Settings.getId()) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return;
        }
        Number_Of_Players = 1;
        if (view.getId() == mButton_2_Player.getId()) Number_Of_Players = 2;

        /** Disable buttons and radio buttons */
        enabled_buttons(false);

        /** For all the Cells
         * set the owner to 0
         * set the value to 0
         * and  put the correct backgroundcolor to the cell  */
        for (Cell Cell : mArrayCells) {
            Cell.setOwner(0);
            putColorToCell(Cell);
        }

        /** Assign the turn to Player 1 and initialize number of free Cells */
        Turn = 1;
        vacant_Cells = 9;
    }


    /***********************************************************************************************
     * Get the color of the cell owner player
     * Apply this color to the BackgroundColor of the ImageView of the cell
     **********************************************************************************************/
    public void putColorToCell(Cell Cell) {
        String colJug = mArrayPlayer[Cell.getOwner()].getColor();
        Cell.getImageView().setBackgroundColor(Color.parseColor(colJug));
        //Cell.getImageView().setBackgroundColor(Color.parseColor("#00FF00"));
    }


    /***********************************************************************************************
     * Set all Buttons and radio buttons Enabled or Disabled according to received parameter
     **********************************************************************************************/
    public void enabled_buttons(boolean enabled) {
        mButton_1_Player.setEnabled(enabled);
        mButton_2_Player.setEnabled(enabled);
        mButton_Settings.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Easy.setEnabled(enabled);
        mRadioButton_Medium.setEnabled(enabled);
        mRadioButton_Impossible.setEnabled(enabled);
    }


    /***********************************************************************************************
     * When a player selects a cell,
     * 1 -> Check that the cell is not null
     * 2 -> Check that the cell has no owner
     * 3 -> Check that the game is started (Turn! = 0)
     * 4 -> We award to the cell as owner the player who has the turn
     * 5 -> We put the corresponding color in the cell
     * 6 -> Check if the play makes the player a winner
     * 7 -> We calculate and update how many vacant cells are left
     * 8 -> We switch to the next turn
     * ********************************************************************************************/
    public void playerSelectCell(Cell Cell) {
        if (Cell == null || Cell.getOwner() != 0 || Turn == 0) return;
        Cell.setOwner(Turn);
        putColorToCell(Cell);
        chack_if_there_is_a_winner();
        vacantCells();
        shift_Change();
    }


    /***********************************************************************************************
     * When a player clicks on a cell
     * 1    -> Check that the game is started (Turn! = 0)
     * 2    -> We get the index of the pressed cell
     * 3    -> With this index we call playerSelectCell
     **********************************************************************************************/
    public void playerClickACell(View view) {
        if (Turn != 0) {
            int index = Integer.valueOf(view.getTag().toString());
            playerSelectCell(mArrayCells[index]);
        }
    }


    /***********************************************************************************************
     * When the Android gat the turn
     * 1    -> Search for the best possible cell
     * 2    -> If no cell is found
     * 3    -> Randomly search for a cell without an owner (vacant cell)
     **********************************************************************************************/
    public void androidPlay() {
        Cell Cell = androidSearchForBestCell();
        if (Cell == null) {
            do {
            }
            while
            ((Cell = mArrayCells[new Random().nextInt(9)]).getOwner() != 0);
        }
        playerSelectCell(Cell);
    }


    /***********************************************************************************************
     * The vacant_Cells variable, counts the number of vacant cells
     * Each time a cell is occupied, this procedure is called, which remains 1
     * Once subtracted 1, if the number of cells without owner is less than 1
     * the game is terminated. Turn is set to 0 and the buttons are Enabled
     **********************************************************************************************/
    public void vacantCells() {
        vacant_Cells --;
        if (vacant_Cells < 1) {
            vacant_Cells = 0;
            Turn = 0;
            enabled_buttons(true);
        }
    }


    /***********************************************************************************************
     * Procedimiento para cambiar el turno tras cada jugada
     **********************************************************************************************/
    public void shift_Change() {
        if (Turn == 0)  return;
        Turn ++;
        if (Turn > 2) Turn = 1;
        if (Turn == 2 && Number_Of_Players == 1) androidPlay();
    }


    /***********************************************************************************************
     * Procedimiento para determinar si la última jugada, dio lugar a un ganador
     **********************************************************************************************/
    public void chack_if_there_is_a_winner() {
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
    /***********************************************************************************************
     * Procedimiento para encontar la mejor celda posible patra el jugador android
     **********************************************************************************************/
    public Cell androidSearchForBestCell() {
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

        /** IF IT IS NOT THE ABOVE, LOOK FOR THE EMPTY CELL WITH MORE WEIGHT AND IT OCCUPIES IT */
        Cell Cell = null;
        if (!mRadioButton_Impossible.isChecked()) return Cell;
        int maxPeso = 0;
        int inicia = new Random().nextInt(8);
        int m;
        for (int n = inicia; n < inicia + 9; n++) {
            if (n < 9) m = n; else m = n % 8;
            if (mArrayCells[m].getOwner() == 0 && mArrayCells[m].getWeigth() > maxPeso) {
                maxPeso = mArrayCells[m].getWeigth();
                Cell = mArrayCells[m];
            }
        }
        return Cell;
    }
}
