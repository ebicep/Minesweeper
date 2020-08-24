package Main;

import types.AbstractBox;
import types.Bomb;
import types.Empty;
import types.Number;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board implements MouseListener {

    private AbstractBox[][] board;
    private boolean boardSet;
    private boolean dead;

    public Board() {
        board = new AbstractBox[20][20];
        boardSet = false;
        dead = false;
    }

    public void drawSelf(Graphics g) {
        if (boardSet) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.black);
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (!board[r][c].isHidden())
                        board[r][c].drawSelf(g);
                }
            }
        }
    }

    private void setBombs(int xPos, int yPos) {
        int x = 0;
        int y = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                double random = Math.random() * 11;
                board[r][c] = random < 1.75 ? new Bomb(x, y) : new Empty(x, y);
                if (board[r][c].getRectangle().contains(xPos, yPos) && board[r][c] instanceof Bomb)
                    board[r][c] = new Empty(x, y);
                x += Minesweeper.offset;
            }
            x = 0;
            y += Minesweeper.offset;
        }
    }

    private void setNumbers(int xPos, int yPos) {
        int x = 0;
        int y = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] instanceof Empty && !board[r][c].getRectangle().contains(xPos, yPos)) {
                    if (surroundingBombs(r, c) > 0)
                        board[r][c] = new Number(x, y, surroundingBombs(r, c));
                }
                x += Minesweeper.offset;
            }
            x = 0;
            y += Minesweeper.offset;
        }
    }

    private void removeSurroundingBombs() {
        int x = 0;
        int y = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] instanceof Bomb && nextToStarting(r, c)) {
                    board[r][c] = new Empty(x, y);
                    updateNumber(r, c);
                    board[r][c].setHidden(false);
                    board[r][c].setStarting(true);
                }
                x += Minesweeper.offset;
            }
            x = 0;
            y += Minesweeper.offset;
        }
    }

    private void updateNumber(int r, int c) {
        if (inInnerBox(r, c)) {
            if (board[r][c - 1] instanceof Number) {
                if (surroundingBombs(r, c - 1) > 0)
                    ((Number) board[r][c - 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c - 1));
                else {
                    board[r][c - 1] = new Empty((c - 1) * 50, r * 50);
                    ((Empty) board[r][c - 1]).setWeird(true);
                    board[r][c - 1].setHidden(false);
                    board[r][c - 1].setStarting(true);
                    floodFill(r, c - 1);
                }
            }
            if (board[r][c + 1] instanceof Number) {
                if (surroundingBombs(r, c + 1) > 0)
                    ((Number) board[r][c + 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c + 1));
                else {
                    board[r][c + 1] = new Empty((c + 1) * 50, r * 50);
                    ((Empty) board[r][c + 1]).setWeird(true);
                    board[r][c + 1].setHidden(false);
                    board[r][c + 1].setStarting(true);
                    floodFill(r, c + 1);
                }
            }
            for (int col = c - 1; col <= c + 1; col++) {
                if (board[r - 1][col] instanceof Number) {
                    if (surroundingBombs(r - 1, col) > 0)
                        ((Number) board[r - 1][col]).setNumberOfAdjacentBombs(surroundingBombs(r - 1, col));
                    else {
                        board[r - 1][col] = new Empty(col * 50, (r - 1) * 50);
                        ((Empty) board[r - 1][col]).setWeird(true);
                        board[r - 1][col].setHidden(false);
                        board[r - 1][col].setStarting(true);
                        floodFill(r - 1, col);
                    }
                }
                if (board[r + 1][col] instanceof Number) {
                    if (surroundingBombs(r + 1, col) > 0)
                        ((Number) board[r + 1][col]).setNumberOfAdjacentBombs(surroundingBombs(r + 1, col));
                    else {
                        board[r + 1][col] = new Empty(col * 50, (r + 1) * 50);
                        ((Empty) board[r + 1][col]).setWeird(true);
                        board[r + 1][col].setHidden(false);
                        board[r + 1][col].setStarting(true);
                        floodFill(r + 1, col);
                    }
                }
            }
        } else if (onSides(r, c)) {
            if (r == 0) {
                if (board[r][c - 1] instanceof Number) {
                    if (surroundingBombs(r, c - 1) > 0)
                        ((Number) board[r][c - 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c - 1));
                    else {
                        board[r][c - 1] = new Empty((c - 1) * 50, 0);
                        ((Empty) board[r][c - 1]).setWeird(true);
                        board[r][c - 1].setHidden(false);
                        board[r][c - 1].setStarting(true);
                        floodFill(r, c - 1);
                    }
                }
                if (board[r][c + 1] instanceof Number) {
                    if (surroundingBombs(r, c + 1) > 0)
                        ((Number) board[r][c + 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c + 1));
                    else {
                        board[r][c + 1] = new Empty((c + 1) * 50, 0);
                        ((Empty) board[r][c + 1]).setWeird(true);
                        board[r][c + 1].setHidden(false);
                        board[r][c + 1].setStarting(true);
                        floodFill(r, c + 1);
                    }
                }
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r + 1][col] instanceof Number) {
                        if (surroundingBombs(r + 1, col) > 0)
                            ((Number) board[r + 1][col]).setNumberOfAdjacentBombs(surroundingBombs(r + 1, col));
                        else {
                            board[r + 1][col] = new Empty(col * 50, (r + 1) * 50);
                            ((Empty) board[r + 1][col]).setWeird(true);
                            board[r + 1][col].setHidden(false);
                            board[r + 1][col].setStarting(true);
                            floodFill(r + 1, col);
                        }
                    }
                }
            } else if (r == board.length - 1) {
                if (board[r][c - 1] instanceof Number) {
                    if (surroundingBombs(r, c - 1) > 0)
                        ((Number) board[r][c - 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c - 1));
                    else {
                        board[r][c - 1] = new Empty((c - 1) * 50, r * 50);
                        ((Empty) board[r][c - 1]).setWeird(true);
                        board[r][c - 1].setHidden(false);
                        board[r][c - 1].setStarting(true);
                        floodFill(r, c - 1);
                    }
                }
                if (board[r][c + 1] instanceof Number) {
                    if (surroundingBombs(r, c + 1) > 0)
                        ((Number) board[r][c + 1]).setNumberOfAdjacentBombs(surroundingBombs(r, c + 1));
                    else {
                        board[r][c + 1] = new Empty((c + 1) * 50, r * 50);
                        ((Empty) board[r][c + 1]).setWeird(true);
                        board[r][c + 1].setHidden(false);
                        board[r][c + 1].setStarting(true);
                        floodFill(r, c + 1);
                    }
                }
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r - 1][col] instanceof Number) {
                        if (surroundingBombs(r - 1, col) > 0)
                            ((Number) board[r - 1][col]).setNumberOfAdjacentBombs(surroundingBombs(r - 1, col));
                        else {
                            board[r - 1][col] = new Empty(col * 50, (r - 1) * 50);
                            ((Empty) board[r - 1][col]).setWeird(true);
                            board[r - 1][col].setHidden(false);
                            board[r - 1][col].setStarting(true);
                            floodFill(r - 1, col);
                        }
                    }
                }
            } else if (c == 0) {
                if (board[r - 1][c] instanceof Number) {
                    if (surroundingBombs(r - 1, c) > 0)
                        ((Number) board[r - 1][c]).setNumberOfAdjacentBombs(surroundingBombs(r - 1, c));
                    else {
                        board[r - 1][c] = new Empty(0, (r - 1) * 50);
                        ((Empty) board[r - 1][c]).setWeird(true);
                        board[r - 1][c].setHidden(false);
                        board[r - 1][c].setStarting(true);
                        floodFill(r - 1, c);
                    }
                }
                if (board[r + 1][c] instanceof Number) {
                    if (surroundingBombs(r + 1, c) > 0)
                        ((Number) board[r + 1][c]).setNumberOfAdjacentBombs(surroundingBombs(r + 1, c));
                    else {
                        board[r + 1][c] = new Empty(0, (r + 1) * 50);
                        ((Empty) board[r + 1][c]).setWeird(true);
                        board[r + 1][c].setHidden(false);
                        board[r + 1][c].setStarting(true);
                        floodFill(r + 1, c);
                    }
                }
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c + 1] instanceof Number) {
                        if (surroundingBombs(row, c + 1) > 0)
                            ((Number) board[row][c + 1]).setNumberOfAdjacentBombs(surroundingBombs(row, c + 1));
                        else {
                            board[row][c + 1] = new Empty((c + 1) * 50, row * 50);
                            ((Empty) board[row][c + 1]).setWeird(true);
                            board[row][c + 1].setHidden(false);
                            board[row][c + 1].setStarting(true);
                            floodFill(row, c + 1);
                        }
                    }
                }
            } else if (c == board[0].length - 1) {
                if (board[r - 1][c] instanceof Number) {
                    if (surroundingBombs(r - 1, c) > 0)
                        ((Number) board[r - 1][c]).setNumberOfAdjacentBombs(surroundingBombs(r - 1, c));
                    else {
                        board[r - 1][c] = new Empty(c * 50, (r - 1) * 50);
                        ((Empty) board[r - 1][c]).setWeird(true);
                        board[r - 1][c].setHidden(false);
                        board[r - 1][c].setStarting(true);
                        floodFill(r - 1, c);
                    }
                }
                if (board[r + 1][c] instanceof Number) {
                    if (surroundingBombs(r + 1, c) > 0)
                        ((Number) board[r + 1][c]).setNumberOfAdjacentBombs(surroundingBombs(r + 1, c));
                    else {
                        board[r + 1][c] = new Empty(c * 50, (r + 1) * 50);
                        ((Empty) board[r + 1][c]).setWeird(true);
                        board[r + 1][c].setHidden(false);
                        board[r + 1][c].setStarting(true);
                        floodFill(r + 1, c);
                    }
                }
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c - 1] instanceof Number) {
                        if (surroundingBombs(row, c - 1) > 0)
                            ((Number) board[row][c - 1]).setNumberOfAdjacentBombs(surroundingBombs(row, c - 1));
                        else {
                            board[row][c - 1] = new Empty((c - 1) * 50, row * 50);
                            ((Empty) board[row][c - 1]).setWeird(true);
                            board[row][c - 1].setHidden(false);
                            board[row][c - 1].setStarting(true);
                            floodFill(row, c - 1);
                        }
                    }
                }
            }
        }
    }

    private void revealSurroundingNumbers() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] instanceof Number) {
                    if (nextToStarting(r, c))
                        board[r][c].setHidden(false);
                }
            }
        }
    }

    private boolean nextToStarting(int r, int c) {
        if (inInnerBox(r, c)) {
            if (board[r][c - 1].isStarting() || board[r][c + 1].isStarting())
                return true;
            for (int col = c - 1; col <= c + 1; col++) {
                if (board[r - 1][col].isStarting() || board[r + 1][col].isStarting())
                    return true;
            }
        } else if (onSides(r, c)) {
            if (r == 0) {
                if (board[r][c - 1].isStarting() || board[r][c + 1].isStarting())
                    return true;
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r + 1][col].isStarting())
                        return true;
                }
            } else if (r == board.length - 1) {
                if (board[r][c - 1].isStarting() || board[r][c + 1].isStarting())
                    return true;
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r - 1][col].isStarting())
                        return true;
                }
            } else if (c == 0) {
                if (board[r - 1][c].isStarting() || board[r + 1][c].isStarting())
                    return true;
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c + 1].isStarting())
                        return true;
                }
            } else if (c == board[0].length - 1) {
                if (board[r - 1][c].isStarting() || board[r + 1][c].isStarting())
                    return true;
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c - 1].isStarting())
                        return true;
                }
            }
        }
        return false;
    }

    private void floodFill(int r, int c) {
        if (inInnerBox(r, c)) {
            fillLeft(r, c);
            fillRight(r, c);
            fillTop(r, c);
            fillBottom(r, c);
        } else if (onSides(r, c)) {
            if (r == 0 || r == board.length - 1) {
                fillLeft(r, c);
                fillRight(r, c);
                if (r == 0)
                    fillBottom(r, c);
                else
                    fillTop(r, c);
            } else if (c == 0 || c == board[0].length - 1) {
                fillTop(r, c);
                fillBottom(r, c);
                if (c == 0)
                    fillRight(r, c);
                else
                    fillLeft(r, c);
            }
        } else if (onCorners(r, c)) {
            if (r == 0 && c == 0) {
                fillRight(r, c);
                fillBottom(r, c);
            } else if (r == 0 && c == board[0].length - 1) {
                fillLeft(r, c);
                fillBottom(r, c);
            } else if (r == board.length - 1 && c == 0) {
                fillRight(r, c);
                fillTop(r, c);
            } else if (r == board.length - 1 && c == board[0].length - 1) {
                fillLeft(r, c);
                fillTop(r, c);
            }
        }
    }

    private void fillRight(int r, int c) {
        if (board[r][c + 1].isHidden()) {
            if (board[r][c + 1] instanceof Empty) {
                board[r][c + 1].setHidden(false);
                board[r][c + 1].setStarting(true);
                floodFill(r, c + 1);
                removeSurroundingBombs();
            }
        }
    }

    private void fillLeft(int r, int c) {
        if (board[r][c - 1].isHidden()) {
            if (board[r][c - 1] instanceof Empty) {
                board[r][c - 1].setHidden(false);
                board[r][c - 1].setStarting(true);
                floodFill(r, c - 1);
                removeSurroundingBombs();
            }
        }
    }

    private void fillTop(int r, int c) {
        if (board[r - 1][c].isHidden()) {
            if (board[r - 1][c] instanceof Empty) {
                board[r - 1][c].setHidden(false);
                board[r - 1][c].setStarting(true);
                floodFill(r - 1, c);
                removeSurroundingBombs();
            }
        }
    }

    private void fillBottom(int r, int c) {
        if (board[r + 1][c].isHidden()) {
            if (board[r + 1][c] instanceof Empty) {
                board[r + 1][c].setHidden(false);
                board[r + 1][c].setStarting(true);
                floodFill(r + 1, c);
                removeSurroundingBombs();
            }
        }
    }

    private void extraFill() {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if(board[r][c] instanceof Empty && board[r][c].isHidden()) {
                    if(inInnerBox(r,c)) {
                        //RELATIVE TO THIS BOARD[R][C]
                        //TOP RIGHT
                        if(board[r - 1][c + 1].isStarting()) {
                            floodFill(r,c);
                        }
                        //TOP LEFT
                        if(board[r - 1][c - 1].isStarting()) {
                            floodFill(r,c);
                        }
                        //BOTTOM RIGHT
                        if(board[r + 1][c + 1].isStarting()) {
                            floodFill(r,c);
                        }
                        //BOTTOM LEFT
                        if(board[r + 1][c - 1].isStarting()) {
                            floodFill(r,c);
                        }
                    }
                }
            }
        }
    }

    private int surroundingBombs(int r, int c) {
        int output = 0;
        //inside outline of box
        if (inInnerBox(r, c)) {
            //top and bottom 3
            for (int col = c - 1; col <= c + 1; col++) {
                if (board[r - 1][col] instanceof Bomb)
                    output++;
                if (board[r + 1][col] instanceof Bomb)
                    output++;
            }
            //two sides
            if (board[r][c - 1] instanceof Bomb)
                output++;
            if (board[r][c + 1] instanceof Bomb)
                output++;
        }
        //corners
        else if (onCorners(r, c)) {
            if (r == 0 && c == 0) {
                if (board[r][c + 1] instanceof Bomb)
                    output++;
                if (board[r + 1][c] instanceof Bomb)
                    output++;
                if (board[r + 1][c + 1] instanceof Bomb)
                    output++;
            } else if (r == 0 && c == board[0].length - 1) {
                if (board[r][c - 1] instanceof Bomb)
                    output++;
                if (board[r + 1][c] instanceof Bomb)
                    output++;
                if (board[r + 1][c - 1] instanceof Bomb)
                    output++;
            } else if (r == board.length - 1 && c == 0) {
                if (board[r][c + 1] instanceof Bomb)
                    output++;
                if (board[r - 1][c] instanceof Bomb)
                    output++;
                if (board[r - 1][c + 1] instanceof Bomb)
                    output++;
            } else if (r == board.length - 1 && c == board[0].length - 1) {
                if (board[r][c - 1] instanceof Bomb)
                    output++;
                if (board[r - 1][c] instanceof Bomb)
                    output++;
                if (board[r - 1][c - 1] instanceof Bomb)
                    output++;
            }
            //sides
        } else if (onSides(r, c)) {
            if (r == 0) {
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r + 1][col] instanceof Bomb)
                        output++;
                }
                if (board[r][c - 1] instanceof Bomb)
                    output++;
                if (board[r][c + 1] instanceof Bomb)
                    output++;
            } else if (r == board.length - 1) {
                for (int col = c - 1; col <= c + 1; col++) {
                    if (board[r - 1][col] instanceof Bomb)
                        output++;
                }
                if (board[r][c - 1] instanceof Bomb)
                    output++;
                if (board[r][c + 1] instanceof Bomb)
                    output++;
            } else if (c == 0) {
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c + 1] instanceof Bomb)
                        output++;
                }
                if (board[r - 1][c] instanceof Bomb)
                    output++;
                if (board[r + 1][c] instanceof Bomb)
                    output++;
            } else if (c == board[0].length - 1) {
                for (int row = r - 1; row <= r + 1; row++) {
                    if (board[row][c - 1] instanceof Bomb)
                        output++;
                }
                if (board[r - 1][c] instanceof Bomb)
                    output++;
                if (board[r + 1][c] instanceof Bomb)
                    output++;
            }
        }
        return output;
    }

    private boolean inInnerBox(int r, int c) {
        return (r >= 1 && r + 1 < board.length && c >= 1 && c + 1 < board[0].length);
    }

    private boolean onCorners(int r, int c) {
        return (r == 0 && c == 0) || (r == 0 && c == board[0].length - 1) || (r == board.length - 1 && c == 0) || (r == board.length - 1 && c == board[0].length - 1);
    }

    private boolean onSides(int r, int c) {
        return !onCorners(r, c) && (r == 0 || r == board.length - 1 || c == 0 || c == board[0].length - 1);
    }

    public boolean isDead() {
        return dead;
    }

    public void reset() {
        board = new AbstractBox[20][20];
        boardSet = false;
        dead = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int key = e.getButton();
        String keyStr = e.paramString();
        if(keyStr.contains("modifiers=Button1+Button3")) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {

                }
            }
        } else if (key == 1) {
            if (!boardSet) {
                setBombs(e.getX(), e.getY() - 25);
                setNumbers(e.getX(), e.getY() - 25);
                for (int r = 0; r < board.length; r++) {
                    for (int c = 0; c < board[0].length; c++) {
                        if (board[r][c].getRectangle().contains(e.getX(), e.getY() - 25)) {
                            board[r][c].setHidden(false);
                            board[r][c].setStarting(true);
                            floodFill(r, c);
                            extraFill();
                            r = board.length;
                            break;
                        }
                    }
                }
                removeSurroundingBombs();
                revealSurroundingNumbers();
                boardSet = true;
            } else {
                for (int r = 0; r < board.length; r++) {
                    for (int c = 0; c < board[0].length; c++) {
                        if (board[r][c].getRectangle().contains(e.getX(), e.getY() - 25)) {
                            if (board[r][c] instanceof Bomb)
                                dead = true;
                            board[r][c].setHidden(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
