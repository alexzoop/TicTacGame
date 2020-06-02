package gamelib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3;
    static int cellSize = 150;
    private char[][] gameField;
    private GameButton[] gameButtons;

    private Game game;

    static char nullSymbol = '\u0000';

    public GameBoard(Game currentGame){
        this.game = currentGame;
        initField();
    }

    private void initField(){
        setBounds(cellSize * dimension, cellSize * dimension, 400, 300);
        setTitle("Крестики-Нолики");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("Новая игра");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField();
            }
        });

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(newGameButton);
        controlPanel.setSize(cellSize * dimension, 150);

        JPanel gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(dimension, dimension));
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);

        gameField = new char[dimension][dimension];
        gameButtons = new GameButton[dimension*dimension];

        for(int i = 0; i < (dimension * dimension); i++) {
            GameButton fieldButton = new GameButton(i, this);
            gameFieldPanel.add(fieldButton);
            gameButtons[i] = fieldButton;
        }

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    void emptyField() {
        for(int i = 0; i < (dimension * dimension); i++) {
            gameButtons[i].setText("");

            int x = i / GameBoard.dimension;
            int y = i % GameBoard.dimension;

            gameField[x][y] = nullSymbol;
        }
    }

    Game getGame() {
        return game;
    }

    boolean isTurnable(int x, int y) {

        if(gameField[y][x] == nullSymbol)
            return true;

        return false;
    }

    boolean isYourSymbol(char playerSign, int x, int y) {
        if ((x >= 0) && (x < dimension) && (y >= 0) && (y < dimension) && (gameField[y][x] == playerSign)) return true;
        else return false;
    }

    boolean isCenterFree() {
        if (gameField[dimension / 2][dimension / 2] == nullSymbol) return true;
        else return false;
    }

    void updateGameField(int x, int y) {
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

    boolean checkWin() {
        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        if(checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol))
            return true;

        return false;
    }

    private boolean checkWinDiagonals(char playerSymbol){
        boolean leftRight, rightLeft;

        leftRight = true;
        rightLeft = true;

        for(int i = 0; i < dimension; i++){
            leftRight &= (gameField[i][i] == playerSymbol);
            rightLeft &= (gameField[dimension - i - 1][i] == playerSymbol);
        }

        if(leftRight || rightLeft){
           return true;
        }

        return false;

    }
    private boolean checkWinLines(char playerSymbol){
        boolean cols, rows, result;

        result = false;

        for(int col = 0; col < dimension; col++){
            cols = true;
            rows = true;

            for(int row = 0; row < dimension; row++){
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }
            if(cols || rows){
                result = true;
                break;
            }
            if(result) {
                break;
            }
        }
        return result;
    }

    public boolean isFull() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++){
                if(gameField[i][j] == nullSymbol)
                    return false;
            }
        }
        return true;
    }

    public GameButton getButton(int buttonIndex) {
        return gameButtons[buttonIndex];
    }
}
