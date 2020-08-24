package Main;

import java.awt.*;

public class CustomRectangle {

    private Rectangle rectangle;
    private boolean flagged;

    public CustomRectangle(int x, int y, int width, int height, boolean flagged) {
        this.rectangle = new Rectangle(x,y,width,height);
        this.flagged = flagged;
    }

    public void drawSelf(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        if(flagged) {
            g2d.fill(rectangle);
        } else {
            g2d.draw(rectangle);
        }
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
