package com.example.zeroescrosses;

public class Game {
    public static final int SIZE = 3;

    private CellState[][] field = new CellState[SIZE][SIZE];
    private CellState player = CellState.ZERO;
    private boolean ended = false;
    private boolean draw = false;

    public Game() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                field[i][j] = CellState.EMPTY;
            }
        }
    }

    public void changePlayer() {
        if(player == CellState.ZERO)
            player = CellState.CROSS;
        else
            player = CellState.ZERO;
    }

    public CellState getCellState(int row, int column) {
        return field[row][column];
    }

    public void changeCellState(int row, int column) {
        field[row][column] = player;
    }

    public boolean isWinningRow(int row) {
        CellState first = field[row][0];
        if(first != CellState.EMPTY) {
            for(int i = 1; i < SIZE; i++) {
                if(first != field[row][i])
                    return false;
            }
            return true;
        }
        else return false;
    }

    public boolean isWinningColumn(int column) {
        CellState first = field[0][column];
        if(first != CellState.EMPTY) {
            for(int i = 1; i < SIZE; i++) {
                if(first != field[i][column])
                    return false;
            }
            return true;
        }
        else return false;
    }

    public boolean isWinningDiagonal(int row, int column) {
        if(row == column) {
            CellState first = field[0][0];
            if(first != CellState.EMPTY) {
                for(int i = 1; i < SIZE; i++) {
                    if(first != field[i][i])
                        return false;
                }
                return true;
            }
            else return false;
        }
        else if(row == (SIZE - 1 - column)) {
            CellState first = field[0][SIZE - 1];
            if(first != CellState.EMPTY) {
                for(int i = 1; i < SIZE; i++) {
                    if(first != field[i][SIZE - 1 - i])
                        return false;
                }
                return true;
            }
            else return false;
        }
        else return false;
    }

    public void checkDraw() {
        int n = 0;
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(field[i][j] == CellState.EMPTY)
                    n++;
            }
        }
        if(n > 0)
            draw = false;
        else
            draw = true;
    }

    public boolean isEnded() {
        return ended;
    }

    public void endGame() {
        ended = true;
    }

    public boolean isDraw() {
        return draw;
    }

    public CellState getPlayer() {
        return player;
    }
}
