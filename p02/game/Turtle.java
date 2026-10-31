package p02.game;

import java.util.Random;

public class Turtle {

    private int column;
    private final int homeRow;
    private int currentRow;
    private boolean isEmerged;
    private int changeState;
    private static final Random random = new Random();

    private static final int minEmerged = 19;
    private static final int maxEmerged = 39;
    private static final int minDipped = 4;
    private static final int maxDipped = 7;
    public static final int huntingDuration = 10;

    private boolean playerOnTurtle = false;
    private boolean isHunting = false;

    public Turtle(int column, int row) {
        this.column = column;
        this.homeRow = row;
        this.currentRow = this.homeRow;
        this.isEmerged = true;
        resetTimer();
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return currentRow;
    }

    public int getHomeRow() {
        return homeRow;
    }

    public boolean isEmerged() {
        return isEmerged;
    }

    public boolean isHunting() {
        return isHunting;
    }

    public void playerOnTurtle(boolean occupied) {
        this.playerOnTurtle = occupied;
    }

    private void resetTimer() {
        if (isHunting) {
            this.changeState = huntingDuration;
        } else if (isEmerged) {
            this.currentRow = this.homeRow;
            this.changeState = minEmerged + random.nextInt(maxEmerged - minEmerged + 1);
        } else {
            this.changeState = minDipped + random.nextInt(maxDipped - minDipped + 1);
        }
    }

    public boolean stateUpdate() {
        if (playerOnTurtle) {
            boolean stateForcedToEmerged = false;
            if (!isEmerged) {
                isEmerged = true;
                stateForcedToEmerged = true;
            }
            if (currentRow != homeRow) {
                currentRow = homeRow;
                stateForcedToEmerged = true;
            }
            if (isHunting) {
                isHunting = false;
                stateForcedToEmerged = true;
            }
            if (stateForcedToEmerged) {
                resetTimer();
            }
            return stateForcedToEmerged;
        }

        changeState--;
        if (changeState <= 0) {
            if (isHunting) {
                isHunting = false;
                isEmerged = true;
                this.currentRow = this.homeRow;
                resetTimer();
            } else {
                isEmerged = !isEmerged;
                if (isEmerged) {
                    this.currentRow = this.homeRow;
                } else {
                    this.currentRow = this.homeRow + 1;
                }
                resetTimer();
            }
            return true;
        }
        return false;
    }

    public boolean dive(int fishActualRow) {
        if (playerOnTurtle) {
            return false;
        }
        if (this.isEmerged && !this.isHunting && this.currentRow == this.homeRow && this.homeRow == fishActualRow - 1) {
            this.isEmerged = false;
            this.isHunting = true;
            this.currentRow = fishActualRow;
            resetTimer();
            return true;
        }
        return false;
    }

    public void resetTurtle() {
        this.isHunting = false;
        this.isEmerged = true;
        this.currentRow = this.homeRow;
        resetTimer();
    }
}
