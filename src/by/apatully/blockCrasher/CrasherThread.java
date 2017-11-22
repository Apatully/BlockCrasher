package by.apatully.blockCrasher;

public class CrasherThread extends Thread {

    private Crasher game;

    public CrasherThread(Crasher game) {
        this.game = game;
    }

    public void run() {

        game.isRunning = true;
        game.isPaused = false;
        game.lastUpdate = System.nanoTime();

        while (game.isRunning) {
            try {
                if (game.isPaused) {
                    game.lastUpdate = System.nanoTime();
                    Thread.sleep(1);
                } else {
                    game.tick();
                    game.lastUpdate = System.nanoTime();
                    Thread.sleep((long) (1000.0 / game.rate));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

