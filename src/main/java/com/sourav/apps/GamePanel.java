/*
 * GamePanel - abstract class representing GameLoop
 * 
 */
package com.sourav.apps;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Sourav Datta (soura.jagat@gmail.com)
 */
public abstract class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
    private volatile boolean running = false;
    private Thread animator = null;
    protected long interval = 50; //fps
    protected Image buffer = null;
    protected Graphics graphics = null;

    protected int WIDTH = 600;
    protected int HEIGHT = 600;

    public GamePanel(int w, int h) {
        super();
        WIDTH = w;
        HEIGHT = h;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
        addKeyListener(this);
    }
    
    public GamePanel() {
        this(600, 600);
    }

    @Override
    public Dimension preferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public long getFPS() {
        return interval;
    }

    public void setFPS(long interval) {
        this.interval = interval;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void startGame() {
        if (animator == null) {
            animator = new Thread(this);
        }
        running = true;
        animator.start();
    }

    public abstract void update();

    public void render() {
        if (buffer == null) {
            buffer = createImage(WIDTH, HEIGHT);
            if (buffer == null) {
                System.out.println("buffer is null");
                return;
            }

            graphics = buffer.getGraphics();
        }
        if (graphics == null) {
            System.out.println("graphics is null");
            return;
        }
    }

    public Graphics gameGraphics() {
        return this.graphics;
    }

    public void stopGame() {
        running = false;
        animator = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buffer != null) {
            g.drawImage(buffer, 0, 0, null);
        }
    }

    private void paintScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (buffer != null)) {
                g.drawImage(buffer, 0, 0, null);            
            }
            Toolkit.getDefaultToolkit().sync();
            if (g != null) {
                g.dispose();
            }
        } catch (Exception ex) {
            System.out.println("could not paint on screen... aborting");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (running) {
            this.update();
            this.render();
            this.paintScreen();

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {}
        }
    }

    // Convenient methods
    protected void keyPressHandler(int keyCode) {
        // do nothing here
    }

    protected void mouseDownHandler(int x, int y) {
        // do nothing here
    }
    // End convenient methods

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressHandler(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDownHandler(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
