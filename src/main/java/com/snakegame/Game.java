package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {

    private final int TILE_SIZE = 25;
    private final int WIDTH = 30;
    private final int HEIGHT = 25;
    private final int DELAY = 120;

    private int[] x = new int[WIDTH * HEIGHT];
    private int[] y = new int[WIDTH * HEIGHT];
    private int bodyParts = 6;
    private int applesEaten = 0;
    private int highScore = 0;
    private int appleX, appleY;
    private char direction = 'R';
    private boolean running = false;
    private boolean gameOver = false;
    private boolean menuScreen = true;

    private Timer timer;
    private Random random;

    public Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void startGame() {
        menuScreen = false;
        gameOver = false;
        running = true;
        applesEaten = 0;
        bodyParts = 6;
        direction = 'R';

        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        newApple();
        if (timer != null) timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newApple() {
        appleX = random.nextInt(WIDTH) * TILE_SIZE;
        appleY = random.nextInt(HEIGHT) * TILE_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menuScreen) {
            drawMenu(g);
        } else if (running) {
            drawGame(g);
        } else if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "SNAKE GAME", HEIGHT * TILE_SIZE / 3);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        drawCenteredString(g, "Press ENTER to Start", HEIGHT * TILE_SIZE / 2);
        drawCenteredString(g, "Use Arrow Keys to Move", HEIGHT * TILE_SIZE / 2 + 40);
        drawCenteredString(g, "Press ESC to Exit", HEIGHT * TILE_SIZE / 2 + 80);
    }

    private void drawGame(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            g.setColor(i == 0 ? Color.green : new Color(45, 180, 0));
            g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + applesEaten, 10, 25);
        g.drawString("High Score: " + highScore, WIDTH * TILE_SIZE - 180, 25);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "Game Over", HEIGHT * TILE_SIZE / 2 - 50);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        drawCenteredString(g, "Score: " + applesEaten, HEIGHT * TILE_SIZE / 2);
        drawCenteredString(g, "High Score: " + highScore, HEIGHT * TILE_SIZE / 2 + 30);
        drawCenteredString(g, "Press R to Retry or ESC to Exit", HEIGHT * TILE_SIZE / 2 + 80);
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        g.drawString(text, (WIDTH * TILE_SIZE - metrics.stringWidth(text)) / 2, y);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break;
            case 'D': y[0] += TILE_SIZE; break;
            case 'L': x[0] -= TILE_SIZE; break;
            case 'R': x[0] += TILE_SIZE; break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            if (applesEaten > highScore) highScore = applesEaten;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) running = false;
        }
        if (x[0] < 0 || x[0] >= WIDTH * TILE_SIZE || y[0] < 0 || y[0] >= HEIGHT * TILE_SIZE)
            running = false;

        if (!running) {
            timer.stop();
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (menuScreen && key == KeyEvent.VK_ENTER) {
            startGame();
            return;
        }

        if (key == KeyEvent.VK_ESCAPE) System.exit(0);

        if (running) {
            if (key == KeyEvent.VK_LEFT && direction != 'R') direction = 'L';
            if (key == KeyEvent.VK_RIGHT && direction != 'L') direction = 'R';
            if (key == KeyEvent.VK_UP && direction != 'D') direction = 'U';
            if (key == KeyEvent.VK_DOWN && direction != 'U') direction = 'D';
        } else if (gameOver && key == KeyEvent.VK_R) {
            startGame();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
