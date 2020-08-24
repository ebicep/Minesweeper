package types;

import java.awt.*;

public class Bomb extends AbstractBox {

    public Bomb(int x, int y) {
        super(x, y);
    }

    @Override
    public void drawSelf(Graphics g) {
        g.setColor(Color.RED);
        g.drawOval(x+7,y+7,10,10);
    }
}
