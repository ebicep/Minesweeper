package types;

import java.awt.*;

public class Empty extends AbstractBox {

    private boolean weird = false;

    public Empty(int x, int y) {
        super(x, y);
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.GREEN);
        if(weird) {
            g.drawString("NEW", x + 10, y + 20);
        } else {
            g.drawString("null", x + 10, y + 20);
        }
    }

    public void setWeird(boolean weird) {
        this.weird = weird;
    }
}
