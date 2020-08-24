package types;

import Main.Minesweeper;

import java.awt.*;

public abstract class AbstractBox {

    protected int x;
    protected int y;
    private Rectangle rectangle;
    private boolean hidden;
    private boolean starting;

    public AbstractBox(int x, int y) {
        this.x = x;
        this.y = y;
        rectangle = new Rectangle(x,y, Minesweeper.offset,Minesweeper.offset);
        hidden = true;
        starting = false;
    }

    public abstract void drawSelf(Graphics g);

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isStarting() {
        return starting;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }
}
