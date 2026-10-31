package p02.pres;

import p02.pres.events.TickEvent;
import p02.pres.events.TickListener;
import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {

    private static GameThread instance = null;
    private boolean running = false;
    private final Object runningLock = new Object();

    private int delay = 1000;
    private int currentDelay = 0;

    private List<TickListener> tickListeners = new ArrayList<>();

    private GameThread() {
        this.currentDelay = this.delay;
    }

    public static GameThread getInstance() {
        if (instance == null) {
            synchronized (GameThread.class) {
                if (instance == null) {
                    instance = new GameThread();  
                }
            }
        }
        return instance;
    }

    public void addTickListener(TickListener listener) {
        synchronized (tickListeners) {
            tickListeners.add(listener);
        }
    }

    public void removeTickListener(TickListener listener) {
        synchronized (tickListeners) {
            tickListeners.remove(listener);
        }
    }

    private void gameTick() {
        TickEvent tickEvent = new TickEvent(this);
        List<TickListener> listenersCopy;
        synchronized (tickListeners) {
            listenersCopy = new ArrayList<>(tickListeners);
        }
        for (TickListener listener : listenersCopy) {
            listener.onTick(tickEvent);
        }
    }

    public void startGame() {
        boolean canStart = false;
        synchronized (runningLock) {
            if (!this.running) {
                this.running = true;
                canStart = true;
            }
        }

        if (canStart) {
            this.currentDelay = this.delay; 
            try {
                super.start();
            } catch (IllegalThreadStateException e) {
                synchronized (runningLock) {
                    this.running = false; 
                }
            }
        }
    }

    public void stopGame() {
        synchronized (runningLock) {
            this.running = false;
        }
    }

    public static void resetGame() {
        synchronized (GameThread.class) {
            if (instance != null && instance.isAlive()) {
            }
            instance = null;
        }
    }

    public void decreaseDelay() {
        if (this.currentDelay > 200) {
            this.currentDelay -= 50;
        }
    }

    public void run() {
        boolean activeRunLoop;
        synchronized (runningLock) {
            activeRunLoop = this.running;
        }

        while (activeRunLoop) {
            gameTick();
            try {
                Thread.sleep(this.currentDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                synchronized (runningLock) {
                    this.running = false;
                }
            }
            synchronized (runningLock) {
                activeRunLoop = this.running;
            }
        }
    }


    public boolean isRunning() {
        synchronized (runningLock) {
            return this.running;
        }
    }
}
