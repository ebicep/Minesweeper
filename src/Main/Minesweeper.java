package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper extends JComponent implements KeyListener, MouseListener, MouseMotionListener {

    public static final int width = 1007;
    public static final int height = 1030;
    public static final int offset = 1000/20;

    private static CustomRectangle[][] rectangles;
    private Board board = new Board();

    JFrame gui;
    public Minesweeper() {
        gui = new JFrame(); //This makes the gui box
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Makes sure program can close
        gui.setTitle("Main.Minesweeper"); //This is the title of the game, you can change it
        gui.setPreferredSize(new Dimension(width,height)); //Setting the size for gui
        gui.setResizable(false); //Makes it sso the gui cant be resized
        gui.getContentPane().add(this); //Adding this class to the gui

        /*If after you finish everything, you can declare your buttons or other things
         *at this spot. AFTER gui.getContentPanaae().add(this) and BEFORE gui.pack();
         */

        gui.pack(); //Packs everything together
        gui.setLocationRelativeTo(this); //Makes so the gui opens in the center of screen - FALSE
        gui.setVisible(true); //Makes the gui visible
        gui.addKeyListener(this);//stating that this object will listen to the keyboard
        gui.addMouseListener(this); //stating that this object will listen to the Mouse
        gui.addMouseMotionListener(this); //stating that this object will acknowledge when the Mouse moves

        gui.addMouseListener(board); //stating that this object will listen to the Mouse

        rectangles = new CustomRectangle[20][20];
        int x = 0;
        int y = 0;
        for (int r = 0; r < rectangles.length; r++) {
            for(int c = 0; c < rectangles[0].length; c++) {
                rectangles[r][c] = new CustomRectangle(x, y, offset, offset, false);
                x += offset;
            }
            x = 0;
            y += offset;
        }

    }

    public void paintComponent(Graphics g) {
        for (int r = 0; r < rectangles.length; r++) {
            for(int c = 0; c < rectangles[0].length; c++) {
                rectangles[r][c].drawSelf(g);
            }
        }

        g.setColor(Color.black);
        board.drawSelf(g);

        if(board.isDead()) {
            board.reset();
        }
    }

    public void loop() {

        repaint();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public static void flag(int r, int c) {
        System.out.println("test");
        rectangles[r][c].setFlagged(!rectangles[r][c].isFlagged());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

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
        if(key == 3) {
            for (int r = 0; r < rectangles.length; r++) {
                for (int c = 0; c < rectangles[0].length; c++) {
                    if (rectangles[r][c].getRectangle().contains(e.getX(), e.getY() - 25)) {
                        Minesweeper.flag(r, c);
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void start(final int ticks) {
        Thread gameThread = new Thread(() -> {
            while (true) {
                loop();
                try {
                    Thread.sleep(1000 / ticks);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        ms.start(60);
    }

}
