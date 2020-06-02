package gamelib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameActionListener implements ActionListener {
    private int row;
    private int cell;
    private GameButton button;

    public GameActionListener(int row, int cell, GameButton gButton){
        this.row = row;
        this.cell = cell;
        this.button = gButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();

        if(board.isTurnable(row, cell)){
            updateByPlayersData(board);
            if(board.isFull()) {
                board.getGame().showMessage("Ничья!");
                board.getGame().passTurn();
                board.emptyField();
            }
            else {
                board.getGame().passTurn();
                updateByAiData(board);
            }
        }
        else {
            board.getGame().showMessage("Некорректный ход!");
        }
    }

    private void updateByPlayersData(GameBoard board){
        board.updateGameField(row, cell);
        button.setText((Character.toString(board.getGame().getCurrentPlayer().getPlayerSign())));
        if(board.checkWin()) {
            button.getBoard().getGame().showMessage("Вы выиграли!");
            board.getGame().passTurn();
            board.emptyField();
        }
    }

    private void updateByAiData(GameBoard board) {
        int x = -1, y = -1;
        int[][] score = new int[GameBoard.dimension][GameBoard.dimension];                    //инициализация массива, содержащего "вес" ходов

        if(!board.isCenterFree()) {                                 //доп. проверка на свободный центр, если да - нечего и думать:)
            for (int i = 0; i < GameBoard.dimension; i++) {
                for (int j = 0; j < GameBoard.dimension; j++) {
                    if (board.isTurnable(j, i)) {                   //если клетка просто пустая - вес = 1
                        if (isNeighbour(board, i, j)) {                    //если при этом ещё и соседняя своя - вес = 2
                            if (isNeighbourOfNeighbour(board, i, j)) {     //а если при всем этом и "правильный"(на той же линии) сосед соседа тоже свой - вес = 3
                                score[i][j] = 3;
                            } else score[i][j] = 2;
                        } else score[i][j] = 1;
                    } else score[i][j] = 0;                         //иначе (клетка занята) - вес = 0

                }
            }

            outer:
            for (int i = 0; i < GameBoard.dimension; i++) {
                for (int j = 0; j < GameBoard.dimension; j++) {
                    if (score[i][j] == 4) {
                        y = i;
                        x = j;
                        break outer;
                    }
                    if (score[i][j] == 3) {
                        y = i;
                        x = j;
                        break outer;
                    } else if (score[i][j] == 2) {
                        y = i;
                        x = j;
                        break outer;
                    } else if (score[i][j] == 1) {
                        y = i;
                        x = j;
                        break outer;
                    }
                }
            }
        }
        else {
            x = board.dimension / 2;
            y = board.dimension / 2;
        }
        board.updateGameField(x, y);
        int cellIndex = GameBoard.dimension * x + y;
        board.getButton(cellIndex).setText(Character.toString(board.getGame().getCurrentPlayer().getPlayerSign()));

        if (board.checkWin()) {
            button.getBoard().getGame().showMessage("Компьютер выиграл!");
            board.emptyField();
        } else {
            board.getGame().passTurn();
        }
    }

    private boolean isNeighbour (GameBoard board, int x, int y) {
        char s = board.getGame().getCurrentPlayer().getPlayerSign();
        boolean res = false;
        if (board.isYourSymbol(s,x - 1, y - 1)) res = true;
        if (board.isYourSymbol(s,x - 1, y)) res = true;
        if (board.isYourSymbol(s,x - 1, y + 1)) res = true;

        if (board.isYourSymbol(s,x, y - 1)) res = true;
        if (board.isYourSymbol(s,x , y + 1)) res = true;

        if (board.isYourSymbol(s,x + 1 , y - 1 )) res = true;
        if (board.isYourSymbol(s,x + 1 , y)) res = true;
        if (board.isYourSymbol(s,x + 1 , y + 1)) res = true;

        return res;
    }

    private boolean isNeighbourOfNeighbour (GameBoard board, int x, int y){
        //проверяем "в лоб" всё через одну клетку, граничные условия всё равно проверяются в isYourSymbol
        char s = board.getGame().getCurrentPlayer().getPlayerSign();
        boolean res = false;
        if (board.isYourSymbol(s,x - 2, y - 2)) res = true;
        if (board.isYourSymbol(s,x - 2, y)) res = true;
        if (board.isYourSymbol(s,x - 2, y + 2)) res = true;

        if (board.isYourSymbol(s,x, y - 2)) res = true;
        if (board.isYourSymbol(s,x , y + 2)) res = true;

        if (board.isYourSymbol(s,x + 2 , y - 2 )) res = true;
        if (board.isYourSymbol(s,x + 2 , y)) res = true;
        if (board.isYourSymbol(s,x + 2 , y + 2)) res = true;

        return res;
    }

}
