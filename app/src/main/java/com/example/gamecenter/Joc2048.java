package com.example.gamecenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class Joc2048 extends AppCompatActivity implements
        GestureDetector.OnGestureListener {

    boolean gameRunning = true;

    int scoreN = 0;

    GestureDetectorCompat mDetector;

    Button resetMove;
    TextView score;
    TextView c00, c10, c20, c30, c01, c11, c21, c31, c02, c12, c22, c32, c03, c13, c23, c33;
    TextView[][] cellsMap;

    int[][] gameMap = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 4, 4},
            {2, 4, 2, 4},
            {4, 2, 4, 4}
    };

    LastStatus lastStatus = new LastStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc_2048);
        mDetector = new GestureDetectorCompat(this, this);

        identifyButtons();

        updateGameStatus(gameMap, scoreN);

    }

    private void identifyButtons() {

        resetMove = (Button) findViewById(R.id.rewind_move);
        resetMove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetLastMove();
            }
        });

        score = (TextView) findViewById(R.id.score);

        c00 = (TextView) findViewById(R.id.button00);
        c10 = (TextView) findViewById(R.id.button10);
        c20 = (TextView) findViewById(R.id.button20);
        c30 = (TextView) findViewById(R.id.button30);
        c01 = (TextView) findViewById(R.id.button01);
        c11 = (TextView) findViewById(R.id.button11);
        c21 = (TextView) findViewById(R.id.button21);
        c31 = (TextView) findViewById(R.id.button31);
        c02 = (TextView) findViewById(R.id.button02);
        c12 = (TextView) findViewById(R.id.button12);
        c22 = (TextView) findViewById(R.id.button22);
        c32 = (TextView) findViewById(R.id.button32);
        c03 = (TextView) findViewById(R.id.button03);
        c13 = (TextView) findViewById(R.id.button13);
        c23 = (TextView) findViewById(R.id.button23);
        c33 = (TextView) findViewById(R.id.button33);

        cellsMap = new TextView[][]{
                {c00, c01, c02, c03},
                {c10, c11, c12, c13},
                {c20, c21, c22, c23},
                {c30, c31, c32, c33}
        };
    }

    private void generateCell() {

        boolean spaceAvailable = false;
        for (int[] gameRow : gameMap) {
            for (int cellValue : gameRow) {
                if (cellValue == 0) spaceAvailable = true;
            }
        }

        if (spaceAvailable) {

            while (true) {
                Random r = new Random();
                int randomCell = r.nextInt(16);

                int rX = randomCell / 4;
                int rY = randomCell % 4;

                if (gameMap[rX][rY] == 0) {

                    int v2or4 = new Random().nextInt(100);

                    if (v2or4 < 95) {
                        updateCell(rX, rY, 2);
                        break;
                    } else {
                        updateCell(rX, rY, 4);
                        break;
                    }
                }
            }
        } else {
            if (!StillPlayable()) gameOver();
        }
    }

    private boolean StillPlayable() {
        //random unexistent num
        int comodin = 1;

        //Check horizontally
        for (int[] row : gameMap) {
            for (int num : row) {
                if (comodin == num) return true;
                comodin = num;
            }
            comodin = 1;
        }


        //Check vertically
        comodin = 1;
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {

                if (gameMap[j][i] == comodin) return true;
                comodin = gameMap[j][i];
            }
            comodin = 1;
        }

        return false;
    }

    private void gameOver() {
        gameRunning = false;
        requestPlayAgain();
    }

    private void requestPlayAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GAME OVER");

        builder.setMessage("Your score -> " + scoreN );

        builder.setPositiveButton("GO BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendScore();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void sendScore() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("score", String.valueOf(scoreN) );
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        double XDisplacement = e2.getX() - e1.getX();
        double YDisplacement = e2.getY() - e1.getY();

        boolean right = false;
        if (XDisplacement > 0) {
            right = true;
        } else XDisplacement = -XDisplacement;

        boolean up = true;
        if (YDisplacement > 0) {
            up = false;
        } else YDisplacement = -YDisplacement;

        String direction = "N";

        if (XDisplacement > YDisplacement + 200) {
            if (right) {
                direction = "E";
            } else direction = "W";
        } else if (YDisplacement > XDisplacement + 200) {
            if (up) {
                direction = "N";
            } else direction = "S";
        }

        if (gameRunning) moveCells(direction);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);

    }

    // ---------------------------------------

    private void updateCell(int x, int y, int newValue) {
        gameMap[x][y] = newValue;

        if (newValue != 0) cellsMap[x][y].setText(String.valueOf(newValue));
        else cellsMap[x][y].setText("");
        cellsMap[x][y].setBackgroundColor(getNumberColor(newValue));
    }

    public void moveCells(String direction) {

        int[][] easySort = new int[4][4];

        switch (direction) {
            case "N":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[y][3 - x];
                }
                break;
            case "E":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[x][3 - y];
                }
                break;
            case "S":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[3 - y][x];
                }
                break;
            case "W":
                for (int x = 0; x < gameMap.length; x++) {
                    for (int y = 0; y < gameMap[x].length; y++) {
                        easySort[x][y] = gameMap[x][y];
                    }
                }
                break;
        }

        int newScore = linearMovement(easySort, scoreN);

        int[][] finalEasySort = new int[4][4];

        switch (direction) {
            case "N":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[3 - y][x];
                }
                break;
            case "E":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[x][3 - y];
                }
                break;
            case "S":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[y][3 - x];
                }
                break;
            case "W":
                for (int x = 0; x < easySort.length; x++) {
                    for (int y = 0; y < easySort[x].length; y++) {
                        finalEasySort[x][y] = easySort[x][y];
                    }
                }
                break;
        }


        updateGameStatus(finalEasySort, newScore);

    }

    private int getNumberColor(int value) {
        switch (value) {
            case 2:
                return getResources().getColor(R.color.cell_2);
            case 4:
                return getResources().getColor(R.color.cell_4);
            case 8:
                return getResources().getColor(R.color.cell_8);
            case 16:
                return getResources().getColor(R.color.cell_16);
            case 32:
                return getResources().getColor(R.color.cell_32);
            case 64:
                return getResources().getColor(R.color.cell_64);
            case 128:
                return getResources().getColor(R.color.cell_128);
            case 256:
                return getResources().getColor(R.color.cell_256);
            case 512:
                return getResources().getColor(R.color.cell_512);
            case 1024:
                return getResources().getColor(R.color.cell_1024);
            case 2048:
                return getResources().getColor(R.color.cell_2048);
            default:
                return getResources().getColor(R.color.empty_cell);
        }
    }

    private int linearMovement(int[][] easySort, int scoreN) {

        int newScore = scoreN;

        for (int[] move : easySort) {

            boolean[] cellsFusionated = {false, false, false, false};

            boolean actionPerformed = true;
            while (actionPerformed) {
                actionPerformed = false;
                for (int i = 1; i < 4; i++) {

                    if (move[i - 1] == move[i] && cellsFusionated[i - 1] == false && cellsFusionated[i] == false) {
                        move[i - 1] += move[i - 1];
                        newScore += move[i - 1];
                        move[i] = 0;
                        cellsFusionated[i - 1] = true;
                        cellsFusionated[i] = false;

                        actionPerformed = true;

                    } else if (move[i] != 0 && move[i - 1] == 0) {

                        move[i - 1] = move[i];
                        move[i] = 0;
                        cellsFusionated[i - 1] = cellsFusionated[i];
                        cellsFusionated[i] = false;

                        actionPerformed = true;

                    }
                }
            }
        }
        return newScore;
    }

    private void updateGameStatus(int[][] easySort, int newScore) {
        //Save last game status
        for (int x = 0; x < gameMap.length; x++) {
            for (int y = 0; y < gameMap[x].length; y++) {
                lastStatus.map[x][y] = gameMap[x][y];
            }
        }
        lastStatus.score = scoreN;


        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                updateCell(x, y, easySort[x][y]);
            }
        }
        scoreN = newScore;
        score.setText(String.valueOf(scoreN));

        generateCell();
    }

    private void resetLastMove() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                updateCell(x, y, lastStatus.map[x][y]);
            }
        }

        scoreN = lastStatus.score;
        score.setText(String.valueOf(scoreN));

    }


    // FORCED OVERRIDES  --IGNORE
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}