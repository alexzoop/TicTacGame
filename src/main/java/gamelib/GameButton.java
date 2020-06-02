package gamelib;

import javax.swing.*;

public class GameButton extends JButton {
    private int buttonIndex;
    private gamelib.GameBoard board;

    public GameButton(int gameButtonIndex, gamelib.GameBoard currentGameBoard) {
        buttonIndex = gameButtonIndex;
        board = currentGameBoard;

        int rowNum = buttonIndex / gamelib.GameBoard.dimension;
        int cellNum = buttonIndex % gamelib.GameBoard.dimension;

        setSize(gamelib.GameBoard.cellSize - 5, gamelib.GameBoard.cellSize - 5);
        addActionListener(new gamelib.GameActionListener(rowNum, cellNum, this));
    }

    public gamelib.GameBoard getBoard() { return board; }
}
