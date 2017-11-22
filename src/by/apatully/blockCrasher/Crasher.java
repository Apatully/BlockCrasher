package by.apatully.blockCrasher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Crasher extends JPanel {

    public int width, height;
    public int rate = 30;
    public Bumper bumper;
    private Ball ball;
    private int balls = 3;
    public boolean isRunning = false, isPaused = false;

    private Color[] rowColors = new Color[]{Color.gray, Color.red.darker(), Color.yellow.darker(), Color.blue.darker(), Color.pink, Color.green.darker()};
    public ArrayList<Block> blocks;

    private Image backgroundTile;

    public long lastUpdate;

    private CrasherThread crasherThread;

    public Crasher(int width, int height) {
        this.width = width;
        this.height = height;

        backgroundTile = new ImageIcon(getClass().getResource("files/Backgroundtile.png")).getImage();

        reset();

        this.setFocusable(true);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                bumper.position.x = e.getX() - getWidth() / 2;
                repaint();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !isRunning) run();
                if (e.getKeyCode() == KeyEvent.VK_P) Pause();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) quit();
            }
        });
    }

    public void reset() {
        bumper = new Bumper(this);
        ball = new Ball(this);
        balls = 3;
        createBlocks(6, 10);
    }

    public void run() {
        if (crasherThread != null) if (crasherThread.isAlive()) crasherThread.interrupt();
        reset();
        crasherThread = new CrasherThread(this);
        crasherThread.start();
    }

    public void Pause() {
        isPaused = !isPaused;
    }

    public void quit() {
        isRunning = false;
    }

    private void createBlocks(int rows, int columns) {
        blocks = new ArrayList<Block>();
        int gap = 10;
        float w = (((float) width - 10) / columns) - 10;
        float h = 30;
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Block b = new Block();
                b.mainColor = rowColors[y % rowColors.length];
                b.position.x = (int) (x * (w + gap) + gap) - width / 2;
                b.position.y = (int) (y * (h + gap) + gap) - height / 2;
                b.height = (int) h;
                b.width = (int) w;
                blocks.add(b);
            }
        }
    }

    public void OnBallLost() {
        balls--;
        if (balls <= 0) OnGameOver(false);
        else ball.position = new Point(0, 0);
    }

    public void OnBlockBroken(Block b) {

    }

    public void OnGameOver(boolean won) {
        quit();
    }

    public void tick() {
        double time = (System.nanoTime() - lastUpdate) / 1000000.0;
        ball.tick(time);
        if (blocks.isEmpty()) OnGameOver(true);
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate((getWidth() - width) / 2, (getHeight() - height) / 2);

        int bgScale = 3;
        for (int x = 0; x < width; x += backgroundTile.getWidth(null) * bgScale) {
            for (int y = 0; y < height; y += backgroundTile.getHeight(null) * bgScale) {
                g.drawImage(backgroundTile, x, y, backgroundTile.getWidth(null) * bgScale, backgroundTile.getHeight(null) * bgScale, null);
            }
        }

        g.translate(width / 2, height / 2);

        bumper.render(g);
        ball.render(g);

        if (blocks != null) for (int i = 0; i < blocks.size(); i++) blocks.get(i).render(g);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.ITALIC, 30));
        String msg = "";
        if (!isRunning) msg = "Press space to start the game";
        else if (isPaused) msg = "Game Paused";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(msg, -fm.stringWidth(msg) / 2, fm.getHeight() / 2);
    }
}

