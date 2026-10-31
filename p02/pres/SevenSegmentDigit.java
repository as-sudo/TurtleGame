package p02.pres;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import p02.pres.events.PlusEvent;
import p02.pres.events.PlusEventListener;
import p02.pres.events.ResetEvent;
import p02.pres.events.ResetEventListener;
import p02.pres.events.StartEvent;
import p02.pres.events.StartListener;

public class SevenSegmentDigit extends JPanel implements StartListener, PlusEventListener, ResetEventListener {

    private int currentValue = 0;
    private boolean[] segmentsOn = new boolean[7];

    private static final boolean[][] DIGIT_PATTERNS = {
            {true, true, true, true, true, true, false},
            {false, true, true, false, false, false, false},
            {true, true, false, true, true, false, true},
            {true, true, true, true, false, false, true},
            {false, true, true, false, false, true, true},
            {true, false, true, true, false, true, true},
            {true, false, true, true, true, true, true},
            {true, true, true, false, false, false, false},
            {true, true, true, true, true, true, true},
            {true, true, true, true, false, true, true}
    };

    private List<StartListener> startEventSubscribers = new ArrayList<>();
    private List<PlusEventListener> plusEventSubscribers = new ArrayList<>();
    private List<ResetEventListener> resetEventSubscribers = new ArrayList<>();

    public SevenSegmentDigit() {
        this(0);
    }

    public SevenSegmentDigit(int initialValue) {
        setPreferredSize(new Dimension(60, 100));
        setBackground(Color.BLACK);
        setValue(initialValue);
    }

    public void setValue(int value) {
        if (value < -1 || value > 9) {
            this.currentValue = -1;
        } else {
            this.currentValue = value;
        }
        updateSegmentStates();
        repaint();
    }

    public int getCurrentValue() {
        return currentValue;
    }

    private void updateSegmentStates() {
        if (currentValue >= 0 && currentValue <= 9) {
            for (int i = 0; i < 7; i++) {
                segmentsOn[i] = DIGIT_PATTERNS[currentValue][i];
            }
        } else {
            for (int i = 0; i < 7; i++) {
                segmentsOn[i] = false;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int margin = panelWidth / 10;
        int segmentThickness = Math.max(2, panelHeight / 12);
        int segmentWidth = panelWidth - 2 * margin - 2 * segmentThickness;
        int segmentHeight = (panelHeight - 3 * margin - 3 * segmentThickness) / 2;
        int x = margin + segmentThickness;
        int y = margin;
        int yMid = margin + segmentHeight + segmentThickness;
        int yBot = margin + 2 * segmentHeight + 2 * segmentThickness;
        int xRight = margin + segmentWidth + segmentThickness;
        g.setColor(Color.RED);
        if (segmentsOn[0]) g.fillRect(x, y, segmentWidth, segmentThickness);
        if (segmentsOn[1]) g.fillRect(xRight, y + segmentThickness, segmentThickness, segmentHeight);
        if (segmentsOn[2]) g.fillRect(xRight, yMid + segmentThickness, segmentThickness, segmentHeight);
        if (segmentsOn[3]) g.fillRect(x, yBot, segmentWidth, segmentThickness);
        if (segmentsOn[4]) g.fillRect(margin, yMid + segmentThickness, segmentThickness, segmentHeight);
        if (segmentsOn[5]) g.fillRect(margin, y + segmentThickness, segmentThickness, segmentHeight);
        if (segmentsOn[6]) g.fillRect(x, yMid, segmentWidth, segmentThickness);
    }

    public void onStart(StartEvent event) {
        setValue(0);
        notifyStartSubscribers(new StartEvent(this));
    }

    public void onPlusOne(PlusEvent event) {
        if (this.currentValue == -1) {
            this.currentValue = 0;
        }
        this.currentValue++;
        if (this.currentValue > 9) {
            this.currentValue = 0;
            setValue(this.currentValue);
            notifyPlusSubscribers(new PlusEvent(this));
        } else {
            setValue(this.currentValue);
        }
    }

    public void onReset(ResetEvent event) {
        setValue(-1);
        notifyResetSubscribers(new ResetEvent(this));
    }

    public synchronized void registerStartListener(StartListener listener) {
        startEventSubscribers.add(listener);
    }

    public synchronized void unregisterStartListener(StartListener listener) {
        startEventSubscribers.remove(listener);
    }

    private synchronized void notifyStartSubscribers(StartEvent event) {
        List<StartListener> listeners = new ArrayList<>(startEventSubscribers);
        for (StartListener listener : listeners) {
            listener.onStart(event);
        }
    }

    public synchronized void registerPlusEventListener(PlusEventListener listener) {
        plusEventSubscribers.add(listener);
    }

    public synchronized void unregisterPlusEventListener(PlusEventListener listener) {
        plusEventSubscribers.remove(listener);
    }

    private synchronized void notifyPlusSubscribers(PlusEvent event) {
        List<PlusEventListener> listeners = new ArrayList<>(plusEventSubscribers);
        for (PlusEventListener listener : listeners) {
            listener.onPlusOne(event);
        }
    }

    public synchronized void registerResetListener(ResetEventListener listener) {
        resetEventSubscribers.add(listener);
    }

    public synchronized void unregisterResetListener(ResetEventListener listener) {
        resetEventSubscribers.remove(listener);
    }

    private synchronized void notifyResetSubscribers(ResetEvent event) {
        List<ResetEventListener> listeners = new ArrayList<>(resetEventSubscribers);
        for (ResetEventListener listener : listeners) {
            listener.onReset(event);
        }
    }

 
}
