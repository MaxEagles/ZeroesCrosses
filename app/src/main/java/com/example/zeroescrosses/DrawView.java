package com.example.zeroescrosses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {

    private Paint paint = new Paint();
    private int screenWidth;
    private int screenHeight;
    private int[] columnsX = new int[Game.SIZE];
    private int[] rowsY = new int[Game.SIZE];
    private Game game = new Game();
    private int indent = 5;
    private int cellWidth;
    private int cellHeight;
    private int winLineStartX;
    private int winLineStartY;
    private int winLineEndX;
    private int winLineEndY;


    public DrawView(Context context) {
        super(context);
        makeMarkup();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        preparePaint(Color.BLACK, 10);
        canvas.drawColor(Color.WHITE);
        drawField(canvas);
        drawCells(canvas);
        drawStatus(canvas);
        if(game.isEnded() && !game.isDraw()) {
            preparePaint(Color.BLUE, 20);
            canvas.drawLine(winLineStartX, winLineStartY, winLineEndX, winLineEndY, paint);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getY() > screenHeight || game.isEnded())
            return true;

        int column = identifyColumn(event.getX());
        int row = identifyRow(event.getY());

        if(game.getCellState(row, column) == CellState.EMPTY) {
            game.changeCellState(row, column);
            game.changePlayer();
            checkEnd(row, column);
            invalidate();
        }
        return true;
    }

    private int identifyColumn(float x) {
        for(int i = 1; i < Game.SIZE; i++) {
            if(x < columnsX[i])
                return i - 1;
        }
        return Game.SIZE - 1;
    }

    private int identifyRow(float y) {
        for(int i = 1; i < Game.SIZE; i++) {
            if(y < rowsY[i])
                return i - 1;
        }
        return Game.SIZE - 1;
    }

    private void makeMarkup() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels - 300;
        int horizontalStep = screenWidth / Game.SIZE;
        int verticalStep = screenHeight / Game.SIZE;
        cellWidth = horizontalStep - (indent * 2);
        cellHeight = verticalStep - (indent * 2);
        for(int i = 0; i < Game.SIZE; i++) {
            columnsX[i] = horizontalStep * i;
            rowsY[i] = verticalStep * i;
        }
    }

    private void drawZero(Canvas canvas, int x, int y) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zero);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, cellWidth, cellHeight, false);
        canvas.drawBitmap(scaledBitmap, x, y, paint);
    }

    private void drawCross(Canvas canvas, int x, int y) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, cellWidth, cellHeight, false);
        canvas.drawBitmap(scaledBitmap, x, y, paint);
    }

    private void drawField(Canvas canvas) {
        for (int i = 1; i < 3; i++) {
            canvas.drawLine(columnsX[i], 0, columnsX[i], screenHeight, paint);
            canvas.drawLine(0, rowsY[i], screenWidth, rowsY[i], paint);
        }
        canvas.drawLine(0, screenHeight, screenWidth, screenHeight, paint);
    }

    private void preparePaint(int color, int strokeWidth) {
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
    }

    private void drawCells(Canvas canvas) {
        for(int i = 0; i < Game.SIZE; i++) {
            for(int j = 0; j < Game.SIZE; j++) {
                if(game.getCellState(i, j) == CellState.ZERO) {
                    drawZero(canvas, columnsX[j] + indent, rowsY[i] + indent);
                }
                else if(game.getCellState(i, j) == CellState.CROSS) {
                    drawCross(canvas, columnsX[j] + indent, rowsY[i] + indent);
                }
            }
        }
    }

    private void checkEnd(int row, int column) {
        if(game.isWinningRow(row)) {
            game.endGame();
            winLineStartX = 0;
            winLineStartY = rowsY[row] + cellHeight / 2;
            winLineEndX = screenWidth;
            winLineEndY = winLineStartY;
        }
        else if(game.isWinningColumn(column)) {
            game.endGame();
            winLineStartX = columnsX[column] + cellWidth / 2;
            winLineStartY = 0;
            winLineEndX = winLineStartX;
            winLineEndY = screenHeight;
        }
        else if(game.isWinningDiagonal(row, column)) {
            game.endGame();
            winLineStartX = 0;
            winLineEndX = screenWidth;
            if(row == column) {
                winLineStartY = 0;
                winLineEndY = screenHeight;
            }
            else {
                winLineStartY = screenHeight;
                winLineEndY = 0;
            }
        }
        else game.checkDraw();
    }

    private void drawStatus(Canvas canvas) {
        String status;
        CellState player = game.getPlayer();
        if(game.isDraw()) {
            status = "Ничья";
        }
        else if(game.isEnded()) {
            if(player == CellState.ZERO)
                status = "Победа крестиков";
            else
                status = "Победа ноликов";
        }
        else {
            if(player == CellState.ZERO)
                status = "Ход ноликов";
            else
                status = "Ход крестиков";
        }
        paint.setTextSize(70);
        canvas.drawText(status, 10, screenHeight + 100, paint);
    }
}
