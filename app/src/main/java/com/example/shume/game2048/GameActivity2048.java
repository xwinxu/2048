package com.example.shume.game2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * The game activity.
 */
public class GameActivity2048 extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private BoardManager2048 boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView2048 gridView;
    private static int columnWidth, columnHeight;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    @SuppressLint("SetTextI18n")
    public void display() {
        updateTileButtons();

        final TextView txtValue = findViewById(R.id.NumMoves2048);
        txtValue.setText(Integer.toString(Board2048.getScore()));
        gridView.setAdapter(new CustomAdapter2048(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        super.onCreate(savedInstanceState);
        loadFromFile(StartingActivity2048.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_main_2048);

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board2048.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / Board2048.NUM_COLS;
                        columnHeight = displayHeight / Board2048.NUM_ROWS;

                        display();
                    }
                });

       // switchtoScoreboard();
    }

//    /**
//     * Switch to ScoreBoard view after game ends.
//     */
//    private void switchtoScoreboard() {
//        if (boardManager.gameWon()){
//            Intent switchtoscore = new Intent(this, MainListView.class);
//            startActivity(switchtoscore);
//        }
//    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board2048 board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board2048.NUM_ROWS; row++) {
            for (int col = 0; col != Board2048.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

//    /**
//     * Process an undo tap when there are no more undos left.
//     * @param view the current view
//     */
//    public void undoButton(View view) {
//        try {
//            this.boardManager.undoMove();
//        } catch (IndexOutOfBoundsException undoEx) {
//            Toast.makeText(this, "No more moves to undo", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board2048 board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board2048.NUM_ROWS;
            int col = nextPos % Board2048.NUM_COLS;
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity2048.TEMP_SAVE_FILENAME);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager2048) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

//    /**
//     * Auto Save the board manager to fileName every 3 moves.
//     *
//     * @param fileName the name of the file
//     */
//    public void autoSave(String fileName){
//        if (boardManager.getSizeMoves() % 3 == 0) {
//            saveToFile(fileName);
//            Toast.makeText(this, "Your Game was saved!", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void update(Observable o, Object arg) {
        //autoSave(StartingActivity2048.SAVE_FILENAME);
        display();
        Toast.makeText(this, "+" + Integer.toString(Board2048.getScoreAdded()), Toast.LENGTH_SHORT).show();
    }
}

