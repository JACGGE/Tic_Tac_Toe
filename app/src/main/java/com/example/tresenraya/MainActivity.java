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
    // TODO Sound
    // TODO Start-Up pantalla
    // TODO Inteligencia Artificial
    // TODO WIFI 2 Players
    // TODO Ingles comentarios

    /***********************************************************************************************
     *  Create variables to reference the Views
     **********************************************************************************************/
    private Button button_1_Player;
    private Button button_2_Player;
    private Button button_Settings;
    private RadioButton radioButton_Easy;
    private RadioButton radioButton_Medium;
    private RadioButton radioButton_Impossible;


    /***********************************************************************************************
     *  Create Object array of the cell class
     **********************************************************************************************/
    private Cell[] arrayCells = new Cell[9];


    /***********************************************************************************************
     *  Create Object array of the SetCells class
     **********************************************************************************************/
    private SetCells[] arraySets = new SetCells[8];


    /***********************************************************************************************
     *  Create Object array of the Player class
     **********************************************************************************************/
    private Player[] arrayPlayers = new Player[3];


    /***********************************************************************************************
     *  Internal use variables
     **********************************************************************************************/
    private int number_Of_Players;   // Number the players of the game in use
    private int turn = 0;            // We start with turn "0" (nobody can play)
    private int vacant_Cells;        // Number of free cells, to know when the game is over


    /***********************************************************************************************
     *  We match the variables to the corresponding View
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_1_Player = findViewById(R.id.id_butoon_1player);
        button_2_Player = findViewById(R.id.id_butoon_2players);
        button_Settings = findViewById(R.id.id_butoonS);
        radioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        radioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        radioButton_Impossible = findViewById(R.id.id_radio_button_impossible_difficulty);


        /***********************************************************************************************
         *      We fill the elements of the arrayCells with references to the cells
         *      We create the cells in the same line
         **********************************************************************************************/
        arrayCells[0] = new Cell((ImageView) findViewById(R.id.id_iv_a1), 3);
        arrayCells[1] = new Cell((ImageView) findViewById(R.id.id_iv_a2), 2);
        arrayCells[2] = new Cell((ImageView) findViewById(R.id.id_iv_a3), 3);

        arrayCells[3] = new Cell((ImageView) findViewById(R.id.id_iv_b1), 2);
        arrayCells[4] = new Cell((ImageView) findViewById(R.id.id_iv_b2), 4);
        arrayCells[5] = new Cell((ImageView) findViewById(R.id.id_iv_b3), 2);

        arrayCells[6] = new Cell((ImageView) findViewById(R.id.id_iv_c1), 3);
        arrayCells[7] = new Cell((ImageView) findViewById(R.id.id_iv_c2), 2);
        arrayCells[8] = new Cell((ImageView) findViewById(R.id.id_iv_c3), 3);


        /***********************************************************************************************
         *      We create the SetCells with references to the cells that make up the set
         *      Sets for the rows 0, 1 y 2
         **********************************************************************************************/
        arraySets[0] = new SetCells(arrayCells[0], arrayCells[1], arrayCells[2]);
        arraySets[1] = new SetCells(arrayCells[3], arrayCells[4], arrayCells[5]);
        arraySets[2] = new SetCells(arrayCells[6], arrayCells[7], arrayCells[8]);


        /***********************************************************************************************
         *      Sets for the columns  0, 1 y 2
         **********************************************************************************************/
        arraySets[3] = new SetCells(arrayCells[0], arrayCells[3], arrayCells[6]);
        arraySets[4] = new SetCells(arrayCells[1], arrayCells[4], arrayCells[7]);
        arraySets[5] = new SetCells(arrayCells[2], arrayCells[5], arrayCells[8]);


        /***********************************************************************************************
         *      Sets for diagonals 0 y 1
         **********************************************************************************************/
        arraySets[6] = new SetCells(arrayCells[0], arrayCells[4], arrayCells[8]);
        arraySets[7] = new SetCells(arrayCells[2], arrayCells[4], arrayCells[6]);


        /***********************************************************************************************
         *      We fill in the elements of the PlayersArray with name and color
         **********************************************************************************************/
        String namePayer[] = getResources().getStringArray(R.array.namePlayer);
        String colorPlayer[] = getResources().getStringArray(R.array.colorPlayer);
        for (int n = 0; n < arrayPlayers.length; n++)
            arrayPlayers[n] = new Player(namePayer[n], colorPlayer[n]);


        /***********************************************************************************************
         *      Recharge of variables and arrays when returning from a deletion due to UI change
         **********************************************************************************************/
        if (savedInstanceState != null) {
            onResume(); /** To recharge Preferences */
            int[] ArrayOwner = new int[9];
            ArrayOwner = savedInstanceState.getIntArray(getString(R.string.Owner));
            for (int n = 0; n < 9; n++) {
                arrayCells[n].setOwner(ArrayOwner[n]);
                putColorToCell(arrayCells[n]);
            }
            enabled_buttons(savedInstanceState.getBoolean
                    (getString(R.string.IsButtonsEnabled), true));
            number_Of_Players = savedInstanceState.getInt
                    (getString(R.string.NumberOfPlayers), 1);
            turn = savedInstanceState.getInt(getString(R.string.turn), 1);
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
            ArrayOwner[n] = arrayCells[n].getOwner();
        }
        outState.putIntArray(getString(R.string.Owner), ArrayOwner);
        outState.putBoolean(getString(R.string.IsButtonsEnabled), button_1_Player.isEnabled());
        outState.putInt(getString(R.string.NumberOfPlayers), number_Of_Players);
        outState.putInt(getString(R.string.turn), turn);
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
         * Recupera las preferencias sobre las matrices arrayPlayers[?]
         ******************************************************************************************/
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        arrayPlayers[1].setName(sharedPreferences.getString
                (getString(R.string.Pla1_name_key), "No leido"));
        arrayPlayers[2].setName(sharedPreferences.getString
                (getString(R.string.Pla2_name_key), "No leido"));
        String colorPlayer[] = getResources().getStringArray(R.array.colorPlayer);
        arrayPlayers[1].setColor(colorPlayer[Integer.valueOf
                (sharedPreferences.getString(getString(R.string.Pla1_color_key), "1"))]);
        arrayPlayers[2].setColor(colorPlayer[Integer.valueOf
                (sharedPreferences.getString(getString(R.string.Pla2_color_key), "1"))]);
    }


    /***********************************************************************************************
     * When the 1-player or 2-player button is pressed
     **********************************************************************************************/
    public void buttonClick(View view) {
        if (view.getId() == button_Settings.getId()) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return;
        }
        number_Of_Players = 1;
        if (view.getId() == button_2_Player.getId()) number_Of_Players = 2;

        /** Disable buttons and radio buttons */
        enabled_buttons(false);

        /** For all the Cells
         * set the owner to 0
         * set the value to 0
         * and  put the correct backgroundcolor to the cell  */
        for (Cell Cell : arrayCells) {
            Cell.setOwner(0);
            putColorToCell(Cell);
        }

        /** Assign the turn to Player 1 and initialize number of free Cells */
        turn = 1;
        vacant_Cells = 9;
    }


    /***********************************************************************************************
     * Get the color of the cell owner player
     * Apply this color to the BackgroundColor of the ImageView of the cell
     **********************************************************************************************/
    public void putColorToCell(Cell Cell) {
        String colJug = arrayPlayers[Cell.getOwner()].getColor();
        Cell.getImageView().setBackgroundColor(Color.parseColor(colJug));
        //Cell.getImageView().setBackgroundColor(Color.parseColor("#00FF00"));
    }


    /***********************************************************************************************
     * Set all Buttons and radio buttons Enabled or Disabled according to received parameter
     **********************************************************************************************/
    public void enabled_buttons(boolean enabled) {
        button_1_Player.setEnabled(enabled);
        button_2_Player.setEnabled(enabled);
        button_Settings.setEnabled(enabled);
        radioButton_Easy.setEnabled(enabled);
        radioButton_Easy.setEnabled(enabled);
        radioButton_Medium.setEnabled(enabled);
        radioButton_Impossible.setEnabled(enabled);
    }


    /***********************************************************************************************
     * When a player selects a cell,
     * 1 -> Check that the cell is not null
     * 2 -> Check that the cell has no owner
     * 3 -> Check that the game is started (turn! = 0)
     * 4 -> We award to the cell as owner the player who has the turn
     * 5 -> We put the corresponding color in the cell
     * 6 -> Check if the play makes the player a winner
     * 7 -> We calculate and update how many vacant cells are left
     * 8 -> We switch to the next turn
     * ********************************************************************************************/
    public void playerSelectCell(Cell Cell) {
        if (Cell == null || Cell.getOwner() != 0 || turn == 0) return;
        Cell.setOwner(turn);
        putColorToCell(Cell);
        chack_if_there_is_a_winner();
        vacantCells();
        shift_Change();
    }


    /***********************************************************************************************
     * When a player clicks on a cell
     * 1    -> Check that the game is started (turn! = 0)
     * 2    -> We get the index of the pressed cell
     * 3    -> With this index we call playerSelectCell
     **********************************************************************************************/
    public void playerClickACell(View view) {
        if (turn != 0) {
            int index = Integer.valueOf(view.getTag().toString());
            playerSelectCell(arrayCells[index]);
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
            ((Cell = arrayCells[new Random().nextInt(9)]).getOwner() != 0);
        }
        playerSelectCell(Cell);
    }


    /***********************************************************************************************
     * The vacant_Cells variable, counts the number of vacant cells
     * Each time a cell is occupied, this procedure is called, which remains 1
     * Once subtracted 1, if the number of cells without owner is less than 1
     * the game is terminated. turn is set to 0 and the buttons are Enabled
     **********************************************************************************************/
    public void vacantCells() {
        vacant_Cells--;
        if (vacant_Cells < 1) {
            vacant_Cells = 0;
            turn = 0;
            enabled_buttons(true);
        }
    }


    /***********************************************************************************************
     * Procedure to change the turn after each play
     **********************************************************************************************/
    public void shift_Change() {
        if (turn == 0) return;
        turn++;
        if (turn > 2) turn = 1;
        if (turn == 2 && number_Of_Players == 1) androidPlay();
    }


    /***********************************************************************************************
     * Procedure to determine if the last play resulted in a winner
     **********************************************************************************************/
    public void chack_if_there_is_a_winner() {
        for (int n = 0; n < arraySets.length; n++) {
            for (int Player = 1; Player < 3; Player++) {
                if (arraySets[n].getSuma() == (Player * Player) * 3) {
                    Toast.makeText(this, getString(R.string.winner)
                            + arrayPlayers[Player].getName(), Toast.LENGTH_LONG).show();
                    vacant_Cells = 0;
                    break;
                }
            }
        }
    }

    /***********************************************************************************************
     * Procedure to find the best possible cell for the android-playar
     * In easy mode
     * 1    -> Search if there are any combination(arraySets) with two android-owner.
     * 2    -> Search if there are any combination(arraySets) with two player1-owner.
     * 3    -> Search random vacant-cell
     * In medium mode
     * 1    -> Steps 1 & 2 for easy mode
     * 2    -> Search if there are any combination(arraySets) with only one android-owner.
     * 3    -> Search vacant cell with more weight
     * In hard mode
     * 1    -> Steps 1 & 2 for easy mode
     * 2    -> Search if there are two combinations with only one android-owner and the same vacant.
     * 3    -> Steps 3 for emedium mode
     **********************************************************************************************/
    public Cell androidSearchForBestCell() {


        /** For all modes **************************************************************************
         *  Search if there are any combination(arraySets) with two android-owner.
         *  The arraySets[n].getSuma() procedure, alwways return the sum on cells of (owner^2)
         ******************************************************************************************/
        for (SetCells arraySet : arraySets)
            if (arraySet.getSuma() == 8) /** = ((plaer2-owner)2^2) * (cells) 2; 8 = 2^2*2 *********/
                for (int m = 0; m < arraySet.getArrayCellsEnSet().length; m++)
                    if (arraySet.getCellEnSet(m).getOwner() == 0) return arraySet.getCellEnSet(m);


        /** For all modes **************************************************************************
         *  Search if there are any combination(arraySets) with two player1-owner.
         *  The arraySets[n].getSuma() procedure, alwways return the sum on cells of (owner^2)
         ******************************************************************************************/
        for (SetCells arraySet : arraySets)
            if (arraySet.getSuma() == 2) /** = ((plaer1-owner)1^2) * (cells) 2; 2 = 1^2*2 *********/
                for (int m = 0; m < arraySet.getArrayCellsEnSet().length; m++)
                    if (arraySet.getCellEnSet(m).getOwner() == 0) return arraySet.getCellEnSet(m);


        /** Only for easy mode *********************************************************************
         * Search random vacant-cell
         ******************************************************************************************/
        if (radioButton_Easy.isChecked()){
            int inicia = new Random().nextInt(8);
            int m;
            for (int n = inicia; n <= inicia + 8; n++) {
                if (n <= 8) m = n;
                else m = n % 8;
                if (arrayCells[m].getOwner() == 0) return arrayCells[m];
            }
        }


        /** Only for medium mode *******************************************************************
         * Search if there are any combination(arraySets) with only one android-owner.
         * The arraySets[n].getSuma() procedure, alwways return the sum on cells of (owner^2)
         ******************************************************************************************/
        for (SetCells arraySet : arraySets)
            if (arraySet.getSuma() == 4) /** = ((plaer1-owner)1^2) * (cells) 1; 2 = 2^2*1 *********/
                for (int m = 0; m < arraySet.getArrayCellsEnSet().length; m++)
                    if (arraySet.getCellEnSet(m).getOwner() == 0) return arraySet.getCellEnSet(m);


        /** Only for medium & hard modes ***********************************************************
         * -> Search vacant cell with more weight
         ******************************************************************************************/
        if (!radioButton_Easy.isChecked()) {
            int startIndex = new Random().nextInt(8);
            int m;
            int maxPeso = 0;
            Cell cell = null;
            for (int n = startIndex; n <= startIndex + 8; n++) {
                if (n <= 8) m = n;
                else m = n % 8;
                if (arrayCells[m].getOwner() == 0 && arrayCells[m].getWeigth() > maxPeso) {
                    maxPeso = arrayCells[m].getWeigth();
                    cell = arrayCells[m];
                }
            }
            return cell;
        }

           return null;
    }
}