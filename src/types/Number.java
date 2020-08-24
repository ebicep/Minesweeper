package types;

import java.awt.*;

public class Number extends AbstractBox {

    private int numberOfAdjacentBombs;

    public Number(int x, int y, int numberOfAdjacentBombs) {
        super(x, y);
        this.numberOfAdjacentBombs = numberOfAdjacentBombs;
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawString("" + numberOfAdjacentBombs,x+10,y+20);
    }

    public int getNumberOfAdjacentBombs() {
        return numberOfAdjacentBombs;
    }

    public void setNumberOfAdjacentBombs(int numberOfAdjacentBombs) {
        this.numberOfAdjacentBombs = numberOfAdjacentBombs;
    }
}
