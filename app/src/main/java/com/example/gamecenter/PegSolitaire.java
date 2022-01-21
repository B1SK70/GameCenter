package com.example.gamecenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PegSolitaire extends AppCompatActivity {

    private boolean firstTap = true;
    private int[] tappedCel = new int[2];

    TextView c00, c01, c02, c03, c04, c05, c06,
            c10, c11, c12, c13, c14, c15, c16,
            c20, c21, c22, c23, c24, c25, c26,
            c30, c31, c32, c33, c34, c35, c36,
            c40, c41, c42, c43, c44, c45, c46,
            c50, c51, c52, c53, c54, c55, c56,
            c60, c61, c62, c63, c64, c65, c66;

    TextView[][] allC;

    int boardStatus[][] = {
            {2, 2, 1, 1, 1, 2, 2},
            {2, 2, 1, 1, 1, 2, 2},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {2, 2, 1, 1, 1, 2, 2},
            {2, 2, 1, 1, 1, 2, 2},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc_pegsolitaire);

        identifyCells();
        setListeners();
        initialBoardPaint();

    }

    private void initialBoardPaint() {
        for (int x = 0; x <= 6; x++) {
            for (int y = 0; y <= 6; y++) {
                switch (boardStatus[x][y]) {
                    case 0:
                        deleteCircle(x, y);
                        break;
                    case 1:
                        drawCircle(x, y);
                        break;
                    case 2:
                        allC[x][y].setBackground(getDrawable(R.drawable.nonplayablecell));
                        break;
                }
            }
        }
    }

    private void setListeners() {

        for (int x = 0; x < allC.length; x++) {
            for (int y = 0; y < allC[x].length; y++) {
                allC[x][y].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        boolean actionPerformed = false;

                        String[] vCoords = getResources().getResourceEntryName(v.getId()).split("textView")[1].split("");
                        int vX = Integer.parseInt(vCoords[0]);
                        int vY = Integer.parseInt(vCoords[1]);

                        //First tap
                        if (firstTap && boardStatus[vX][vY] == 1) {
                            holdCircle(vX, vY);
                            firstTap = false;
                            tappedCel = new int[]{vX, vY};
                            actionPerformed = true;
                        }

                        //Second tap
                        //Check if tapped is playable
                        if (!firstTap && boardStatus[vX][vY] == 0) {

                            //Check direction
                            if (!((tappedCel[0] == vX) == (tappedCel[1] == vY))) {

                                //Check distance
                                if (((tappedCel[0] == vX) && (tappedCel[1] == vY - 2 || tappedCel[1] == vY + 2)) ||
                                        ((tappedCel[1] == vY) && (tappedCel[0] == vX - 2 || tappedCel[0] == vX + 2))
                                ) {
                                    //Check if jumpable
                                    int[] jumpedCell = getJumpedCell(tappedCel, new int[]{vX, vY});
                                    if (boardStatus[jumpedCell[0]][jumpedCell[1]] == 1) {

                                        //Movement executable
                                        deleteCircle(tappedCel[0], tappedCel[1]);
                                        deleteCircle(jumpedCell[0], jumpedCell[1]);
                                        drawCircle(vX, vY);

                                        firstTap = true;
                                        tappedCel = new int[2];
                                        actionPerformed = true;
                                    }
                                }
                            }
                        }

                        if (!actionPerformed) {
                            if (!firstTap) drawCircle(tappedCel[0], tappedCel[1]);
                            firstTap = true;
                            tappedCel = new int[2];
                        }
                    }
                });
            }
        }
    }

    private void drawCircle(int x, int y) {
        allC[x][y].setBackground(getDrawable(R.drawable.circle));
        boardStatus[x][y] = 1;
    }

    private void deleteCircle(int x, int y) {
        allC[x][y].setBackground(getDrawable(R.drawable.emptycell));
        boardStatus[x][y] = 0;
    }

    private void holdCircle(int x, int y) {
        allC[x][y].setBackground(getDrawable(R.drawable.selectedcircle));
    }

    private int[] getJumpedCell(int[] from, int[] to) {
        if (from[0] == to[0]) {
            return new int[]{from[0], Math.max(from[1], to[1]) - 1};
        } else {
            return new int[]{Math.max(from[0], to[0]) - 1, from[1]};
        }
    }

    private void identifyCells() {
        c00 = (TextView) findViewById(R.id.textView00);
        c01 = (TextView) findViewById(R.id.textView01);
        c02 = (TextView) findViewById(R.id.textView02);
        c03 = (TextView) findViewById(R.id.textView03);
        c04 = (TextView) findViewById(R.id.textView04);
        c05 = (TextView) findViewById(R.id.textView05);
        c06 = (TextView) findViewById(R.id.textView06);
        c10 = (TextView) findViewById(R.id.textView10);
        c11 = (TextView) findViewById(R.id.textView11);
        c12 = (TextView) findViewById(R.id.textView12);
        c13 = (TextView) findViewById(R.id.textView13);
        c14 = (TextView) findViewById(R.id.textView14);
        c15 = (TextView) findViewById(R.id.textView15);
        c16 = (TextView) findViewById(R.id.textView16);
        c20 = (TextView) findViewById(R.id.textView20);
        c21 = (TextView) findViewById(R.id.textView21);
        c22 = (TextView) findViewById(R.id.textView22);
        c23 = (TextView) findViewById(R.id.textView23);
        c24 = (TextView) findViewById(R.id.textView24);
        c25 = (TextView) findViewById(R.id.textView25);
        c26 = (TextView) findViewById(R.id.textView26);
        c30 = (TextView) findViewById(R.id.textView30);
        c31 = (TextView) findViewById(R.id.textView31);
        c32 = (TextView) findViewById(R.id.textView32);
        c33 = (TextView) findViewById(R.id.textView33);
        c34 = (TextView) findViewById(R.id.textView34);
        c35 = (TextView) findViewById(R.id.textView35);
        c36 = (TextView) findViewById(R.id.textView36);
        c40 = (TextView) findViewById(R.id.textView40);
        c41 = (TextView) findViewById(R.id.textView41);
        c42 = (TextView) findViewById(R.id.textView42);
        c43 = (TextView) findViewById(R.id.textView43);
        c44 = (TextView) findViewById(R.id.textView44);
        c45 = (TextView) findViewById(R.id.textView45);
        c46 = (TextView) findViewById(R.id.textView46);
        c50 = (TextView) findViewById(R.id.textView50);
        c51 = (TextView) findViewById(R.id.textView51);
        c52 = (TextView) findViewById(R.id.textView52);
        c53 = (TextView) findViewById(R.id.textView53);
        c54 = (TextView) findViewById(R.id.textView54);
        c55 = (TextView) findViewById(R.id.textView55);
        c56 = (TextView) findViewById(R.id.textView56);
        c60 = (TextView) findViewById(R.id.textView60);
        c61 = (TextView) findViewById(R.id.textView61);
        c62 = (TextView) findViewById(R.id.textView62);
        c63 = (TextView) findViewById(R.id.textView63);
        c64 = (TextView) findViewById(R.id.textView64);
        c65 = (TextView) findViewById(R.id.textView65);
        c66 = (TextView) findViewById(R.id.textView66);
        allC = new TextView[][]{
                {c00, c01, c02, c03, c04, c05, c06},
                {c10, c11, c12, c13, c14, c15, c16},
                {c20, c21, c22, c23, c24, c25, c26},
                {c30, c31, c32, c33, c34, c35, c36},
                {c40, c41, c42, c43, c44, c45, c46},
                {c50, c51, c52, c53, c54, c55, c56},
                {c60, c61, c62, c63, c64, c65, c66}
        };
    }
}