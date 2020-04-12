/*
 * Copyright (C) 2019
 * All rights reserved
 *
 * MainActivity Class
 * Exceptionally developed for training and sample purposes
 */

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   DatabaseReference firebase;
   //======== (((((( ==== ((((
   // Create variables to reference the Views

    private Button button_1_Player,
            button_2_Player,
            button_Settings;
    private RadioButton radioButton_Easy,
            radioButton_Medium,
            radioButton_Hard;

    // Create Object array of the cell class
    private Cell[] gameBoard = new Cell[9];

    // Create Object array of the PhotoFame class
    PhotoGame[] photoGame = new PhotoGame[9];

    // Create Object array of the SetOfCells class
    private SetOfCells[] winnerSets = new SetOfCells[8];

    // Create Object array of the Player class
    private SetPlayers setPlayers;

    // Internal use variables
    private int number_Of_Players;      // Number the human players (no android) of the game in use
    private int turn = 0;               // We start with turn "0" (nobody can play) (0-1-2)
    private boolean evenPlays = false;  // When evenPlays, start Player 2 or android-player
    private int vacant_Cells;           // Number of vacant cells, to know when the game is over

    /**
     * @param savedInstanceState bundle width saved variables
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = FirebaseDatabase.getInstance().getReference("Jugadas");

        /*
         We match the variables to the corresponding View
        */
        button_1_Player = findViewById(R.id.id_button_1player);
        button_2_Player = findViewById(R.id.id_button_2players);
        button_Settings = findViewById(R.id.id_buttonS);
        radioButton_Easy = findViewById(R.id.id_radio_button_easy_difficulty);
        radioButton_Medium = findViewById(R.id.id_radio_button_medium_difficulty);
        radioButton_Hard = findViewById(R.id.id_radio_button_impossible_difficulty);

        /*
          We fill the elements of the gameBoard with references to the cells
          We create the cells in the same line
         */
        gameBoard[0] = new Cell((ImageView) findViewById(R.id.id_iv_a1), 3);
        gameBoard[1] = new Cell((ImageView) findViewById(R.id.id_iv_a2), 2);
        gameBoard[2] = new Cell((ImageView) findViewById(R.id.id_iv_a3), 3);

        gameBoard[3] = new Cell((ImageView) findViewById(R.id.id_iv_b1), 2);
        gameBoard[4] = new Cell((ImageView) findViewById(R.id.id_iv_b2), 4);
        gameBoard[5] = new Cell((ImageView) findViewById(R.id.id_iv_b3), 2);

        gameBoard[6] = new Cell((ImageView) findViewById(R.id.id_iv_c1), 3);
        gameBoard[7] = new Cell((ImageView) findViewById(R.id.id_iv_c2), 2);
        gameBoard[8] = new Cell((ImageView) findViewById(R.id.id_iv_c3), 3);

        /*
          We create the SetOfCells with references to the cells that make up the set
          Sets for the rows 0, 1 y 2
         */
        winnerSets[0] = new SetOfCells(gameBoard[0], gameBoard[1], gameBoard[2]);
        winnerSets[1] = new SetOfCells(gameBoard[3], gameBoard[4], gameBoard[5]);
        winnerSets[2] = new SetOfCells(gameBoard[6], gameBoard[7], gameBoard[8]);

        /*
          Sets for the columns  0, 1 y 2
         */
        winnerSets[3] = new SetOfCells(gameBoard[0], gameBoard[3], gameBoard[6]);
        winnerSets[4] = new SetOfCells(gameBoard[1], gameBoard[4], gameBoard[7]);
        winnerSets[5] = new SetOfCells(gameBoard[2], gameBoard[5], gameBoard[8]);

        /*
          Sets for diagonals 0 y 1
         */
        winnerSets[6] = new SetOfCells(gameBoard[0], gameBoard[4], gameBoard[8]);
        winnerSets[7] = new SetOfCells(gameBoard[2], gameBoard[4], gameBoard[6]);

        /*
          We fill in the elements of the PlayersArray with name and color
         */
        String[] nameOfPayer = getResources().getStringArray(R.array.namePlayer);
        String[] colorOfPlayer = getResources().getStringArray(R.array.colorOfPlayer);
        Player player1 = new Player(nameOfPayer[0], colorOfPlayer[0]);
        Player player2 = new Player(nameOfPayer[1], colorOfPlayer[1]);
        Player player3 = new Player(nameOfPayer[2], colorOfPlayer[2]);
        setPlayers = new SetPlayers(player1, player2, player3);

        /*
          call restore variables and arrays when returning from a deletion due to UI change
         */
        restoreInstanceState(savedInstanceState);
    }

    /**
     * Storage of variables and arrays when a deletion due to UI change is about
     *
     * @param outState bundle tu put variables to save
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int[] ArrayOwner = new int[9];
        for (int n = 0; n < 9; n++) {
            ArrayOwner[n] = gameBoard[n].getOwner();
        }
        outState.putIntArray(getString(R.string.Owner), ArrayOwner);
        outState.putBoolean(getString(R.string.IsButtonsEnabled), button_1_Player.isEnabled());
        outState.putInt(getString(R.string.NumberOfPlayers), number_Of_Players);
        outState.putInt(getString(R.string.turn), turn);
        outState.putBoolean(getString(R.string.evenPlays), evenPlays);
        outState.putInt(getString(R.string.VacantCells), vacant_Cells);
    }

    protected void restoreInstanceState(Bundle savedInstanceState) {
        /*
          Restore variables and arrays when returning from a deletion due to UI change
         */
        if (savedInstanceState != null) {
            onResume();                                               // To recharge Preferences */
            int[] ArrayOwner = savedInstanceState.getIntArray(getString(R.string.Owner));
            for (int n = 0; n < 9; n++) {
                gameBoard[n].setOwner(ArrayOwner != null ? ArrayOwner[n] : 0);
                setColorToCell(gameBoard[n]);
            }
            enabled_buttons(savedInstanceState.getBoolean
                    (getString(R.string.IsButtonsEnabled), true));
            number_Of_Players = savedInstanceState.getInt
                    (getString(R.string.NumberOfPlayers), 1);
            turn = savedInstanceState.getInt(getString(R.string.turn), 1);
            evenPlays = savedInstanceState.getBoolean(getString(R.string.evenPlays), true);
            vacant_Cells = savedInstanceState.getInt(getString(R.string.VacantCells), 9);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        /*
                           Set the content to appear under the system bars so that the
                           content doesn't resize when the system bars hide and show.
                         */
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        /*
                         Hide the nav bar and status bar
                         */
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Retrieve preferences on setOfPlayers arrays [?]
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        setPlayers.setName(sharedPreferences.getString
                (getString(R.string.Pla1_name_key), "No leído"), 1);
        setPlayers.setName(sharedPreferences.getString
                (getString(R.string.Pla2_name_key), "No leído"), 2);
        String[] colorOfPlayer = getResources().getStringArray(R.array.colorOfPlayer);
        setPlayers.setColor(colorOfPlayer[Integer.parseInt(Objects.requireNonNull
                (sharedPreferences.getString(getString(R.string.Pla1_color_key), "1")))], 1);
        setPlayers.setColor(colorOfPlayer[Integer.parseInt(Objects.requireNonNull
                (sharedPreferences.getString(getString(R.string.Pla2_color_key), "2")))], 2);
    }

    /**
     * When the 1-player or 2-player button is pressed
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == button_1_Player.getId() || view.getId() == button_2_Player.getId()) {
            number_Of_Players = 1;
            if (view.getId() == button_2_Player.getId()) number_Of_Players = 2;

            /* Disable buttons and radio buttons */
            enabled_buttons(false);

            /* For all the Cells
             * set the owner to 0
             * set the value to 0
             * and  put the correct back to the cell  */
            for (Cell Cell : gameBoard) {
                Cell.setOwner(0);
                setColorToCell(Cell);
            }

            /* Assign the turn to Player 1 and initialize number of free Cells */
            turn = 1;
            vacant_Cells = 9;
            if (evenPlays) turnChange();
        } else if (view.getId() == button_Settings.getId()) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        } else playerClickACell(view);
    }

    /**
     * Set color to the BackgroundColor of the ImageView of the cell
     * Get the color of the cell owner player
     *
     * @param cell es la celda de tablero que queremos colorear
     */
    public void setColorToCell(Cell cell) {
        int colJug = Color.parseColor(setPlayers.getColor(cell.getOwner()));
        cell.getImageView().setBackgroundColor(colJug);
    }

    /**
     * Set all Buttons and radio buttons Enabled or Disabled according to received parameter
     */
    public void enabled_buttons(boolean enabled) {
        button_1_Player.setEnabled(enabled);
        button_2_Player.setEnabled(enabled);
        button_Settings.setEnabled(enabled);
        radioButton_Easy.setEnabled(enabled);
        radioButton_Easy.setEnabled(enabled);
        radioButton_Medium.setEnabled(enabled);
        radioButton_Hard.setEnabled(enabled);
    }

    /**
     * When a player selects a cell,
     * 1 -> Check that the cell is not null
     * 2 -> Check that the cell has no owner
     * 3 -> Check that the game is started (turn! = 0)
     * 4 -> We award to the cell as owner the player who has the turn
     * 5 -> We put the corresponding color in the cell
     * 6 -> Check if the play makes the player a winner
     * 7 -> We calculate and update how many vacant cells are left
     * 8 -> We switch to the next turn
     */
    public void playerSelectCell(Cell Cell) {
        if (Cell == null || Cell.getOwner() != 0 || turn == 0) return;
        Cell.setOwner(turn);
        setColorToCell(Cell);
        checkIfThereIsAWinner();
        updateVacantCells();
        turnChange();
    }

    /**
     * When a player clicks on a cell
     * 1    -> Check that the game is started (turn! = 0)
     * 2    -> We get the index of the pressed cell
     * 3    -> With this index we call playerSelectCell
     */
    public void playerClickACell(View view) {
        if (turn != 0) {
            int index = Integer.parseInt(view.getTag().toString());
            playerSelectCell(gameBoard[index]);
        }
    }

    /**
     * When the Android get the turn
     * 1    -> Search for the best possible cell
     * 2    -> If no cell is found
     * 3    -> Randomly search for a cell without an owner (vacant cell)
     */
    public void androidPlay() {
        Cell cell = androidSearchForBestCell();
        if (cell == null)
            do {
                cell = gameBoard[new Random().nextInt(9)];
            }
            while (cell.getOwner() != 0);
        playerSelectCell(cell);
    }

    /**
     * The vacant_Cells variable, counts the number of vacant cells
     * Each time a cell is occupied, this procedure is called, which remains 1
     * Once subtracted 1, if the number of cells without owner is less than 1
     * the game is terminated. turn is set to 0 and the buttons are Enabled
     */
    public void updateVacantCells() {
        if (vacant_Cells > 0) photoGameToArray();
        if (vacant_Cells == 1) photoGameArrayToFireBase(0);
        vacant_Cells--;
        if (vacant_Cells < 1) {
            vacant_Cells = 0;
            turn = 0;
            evenPlays = !evenPlays;
            enabled_buttons(true);
        }
    }

    /**
     * Procedure to change the turn after each play
     */
    public void turnChange() {
        if (turn != 0) {
            turn++;
            if (turn > 2) turn = 1;
            if (turn == 2 && number_Of_Players == 1) androidPlay();
        }
    }

    public void photoGameArrayToFireBase(int winner) {
        for (int n = 0; n < (10 - vacant_Cells); n++) {
            photoGame[n].setWinner(winner);
            String id = firebase.push().getKey();
            assert id != null;
            firebase.child(id).setValue(photoGame[n]);

        }
        Arrays.fill(photoGame, null);
    }

    public void photoGameToArray() {
        //if (vacant_Cells > 8)return;
        int gameBoardInt = getGameBoardInt();
        int playNumber = 9 - vacant_Cells;
        photoGame[playNumber] = new PhotoGame(gameBoardInt, turn);
    }

    public int getGameBoardInt() {
        String gameBoardBinLow = "";
        String gameBoardBinHigh = "";
        for (Cell cell : gameBoard) {
            switch (cell.getOwner()) {
                case 1:
                    gameBoardBinLow = "1".concat(gameBoardBinLow);
                    gameBoardBinHigh = "0".concat(gameBoardBinHigh);
                    break;
                case 2:
                    gameBoardBinLow = "0".concat(gameBoardBinLow);
                    gameBoardBinHigh = "1".concat(gameBoardBinHigh);
                    break;
                default:
                    gameBoardBinLow = "0".concat(gameBoardBinLow);
                    gameBoardBinHigh = "0".concat(gameBoardBinHigh);
                    break;
            }
        }
        return Integer.parseInt(gameBoardBinHigh + gameBoardBinLow, 2);
    }

    /**
     * Procedure to determine if the last play resulted in a winner
     */
    public void checkIfThereIsAWinner() {
        for (SetOfCells set : winnerSets)
            for (int player = 1; player < 3; player++)
                if (set.getSum() == (player * player) * 3) {
                    Toast.makeText(this, getString(R.string.winner) +
                            setPlayers.getName(player), Toast.LENGTH_LONG).show();
                    photoGameToArray();
                    photoGameArrayToFireBase(turn);
                    vacant_Cells = -1;
                }
    }

    /**
     * Method to return the more interesting cell for the android-player
     */
    public Cell androidSearchForBestCell() {
        Cell cell = searchForAndroidWinner();
        if (cell == null) cell = searchForPlayer1Winner();
        if (cell == null && radioButton_Easy.isChecked()) cell = searchRandomVacantCell();
        if (cell == null && radioButton_Medium.isChecked()) cell = searchCombOneAndroidOwner();
        if (cell == null && radioButton_Hard.isChecked()) cell = searchStrategicCell();
        if (cell == null && !radioButton_Easy.isChecked()) cell = searchMoreWeightCell();
        return cell;
    }

    /**
     * Method to return the cell that makes android winner
     */
    // For all modes
    public Cell searchForAndroidWinner() {
        Cell cell = null;
        for (SetOfCells arraySet : winnerSets)
            if (arraySet.getSum() == 8) /*  = ((player2-owner)2^2) * (cells) 2; 8 = 2^2*2 *********/
                for (int m = 0; m < arraySet.getCells().length; m++)
                    if (arraySet.getCell(m).getOwner() == 0)
                        cell = arraySet.getCell(m);
        return cell;
    }

    /**
     * Method to return the cell that would make the player 1 winner
     */
    // For all modes
    public Cell searchForPlayer1Winner() {
        Cell cell = null;
        for (SetOfCells arraySet : winnerSets)
            if (arraySet.getSum() == 2) /*  = ((player1-owner)1^2) * (cells) 2; 2 = 1^2*2 *********/
                for (int m = 0; m < arraySet.getCells().length; m++)
                    if (arraySet.getCell(m).getOwner() == 0)
                        cell = arraySet.getCell(m);
        return cell;
    }

    /**
     * Method to return random vacant-cell
     */
    // Only for easy mode
    public Cell searchRandomVacantCell() {
        Cell cell = null;
        int startPoint = new Random().nextInt(8),
                endPoint = startPoint + gameBoard.length,
                max = gameBoard.length - 1;
        for (int n = startPoint; n <= endPoint - 1; n++)
            if (gameBoard[n % max].getOwner() == 0)
                cell = gameBoard[n % max];
        return cell;
    }

    /**
     * Method to return a cell in combination with only one android-owner (if there are any)
     */
    // Only for medium mode
    public Cell searchCombOneAndroidOwner() {
        Cell cell = null;
        for (SetOfCells arraySet : winnerSets)
            if (arraySet.getSum() == 4) /* = ((player1-owner)1^2) * (cells) 1; 2 = 2^2*1 *****/
                for (int m = 0; m < arraySet.getCells().length; m++)
                    if (arraySet.getCell(m).getOwner() == 0)
                        cell = arraySet.getCell(m);
        return cell;
    }

    /**
     * Method to return a cell in two combinations with only one android-owner and the same vacant.
     */
    // Only hard mode
    public Cell searchStrategicCell() {
        Cell cell = null;
        for (int play = 1; play < 3; play++) {
            for (Cell value : gameBoard) {
                if (value.getOwner() == 0) {
                    value.setOwner(2);
                    int number = 0;
                    for (SetOfCells winnerSet : winnerSets)
                        if (winnerSet.getSum() == 2 * (play * play)) number++;
                    if (number > 1) {
                        cell = value;
                        value.setOwner(0);
                        break;
                        //return cell;
                    }
                    value.setOwner(0);
                }
            }
        }
        return cell;
    }

    /**
     * Method to return the vacant cell with more weight
     */
    // Only for medium & hard modes
    public Cell searchMoreWeightCell() {
        Cell cell = null;
        int maxWeight = 0,
                startPoint = new Random().nextInt(8),
                endPoint = startPoint + gameBoard.length,
                max = gameBoard.length - 1;
        for (int n = startPoint; n < endPoint; n++)
            if (gameBoard[n % max].getOwner() == 0 && gameBoard[n % max].getWeight() > maxWeight) {
                maxWeight = gameBoard[n % max].getWeight();
                cell = gameBoard[n % max];
            }
        return cell;
    }
}

