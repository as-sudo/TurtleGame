package p02.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import p02.pres.BoardModel;
import p02.pres.events.TickEvent;
import p02.pres.events.TickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import p02.pres.GameThread;
import p02.pres.events.StartListener;
import p02.pres.SevenSegmentDigit;
import p02.pres.events.PlusEvent;
import p02.pres.events.PlusEventListener;
import p02.pres.events.ResetEvent;
import p02.pres.events.StartEvent;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Board implements KeyListener, TickListener, PlusEventListener ,  StartListener {

    private int[][] gameBoard;
    private final int rows = 12;
    private final int columns = 5;

    public static final int emptyCell = 0;
    public static final int playerCell = 1;
    public static final int fishCell = 2;
    public static final int turtleCell = 3;
    public static final int playerBox = 4;
    public static final int turtleHunt = 5;

    
    public static Image characterImage;
    public static Image turtleImage;
    public static Image playerBoxImage;
    public static Image fishImage;
    public static Image turtleHuntImage;
    
    
    private boolean playerHasPackage = false;
    private int playerRow;
    private int playerColumn;

    private BoardModel boardTableModel;
    private long tickCount = 0;

    private List<Fish> fishList;
    private List<Turtle> turtles;
    private Random random;


    private static final int TURTLE_ROW = 2;
    private static final int fishSpawn = 7;

    private int currentMaxFish = 0;
    private SevenSegmentDigit unitDigit;
    private SevenSegmentDigit tenDigit;
    private SevenSegmentDigit hundredDigit;
    private int score = 0;

    public Board() {
        this.gameBoard = new int[columns][rows];
        this.fishList = new ArrayList<>();
        this.random = new Random();
        this.turtles = new ArrayList<>();
        loadGraphics();
        createGameBoard();
        GameThread.getInstance().addTickListener(this);
    }

    public void loadGraphics(){
         try {
        characterImage = ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\character.png"));
        turtleImage = ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\turtle.png"));
        playerBoxImage = ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\characterbox.png"));
        fishImage = ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\fish.png"));
        turtleHuntImage =  ImageIO.read(new File("Z:\\studia\\s2\\p02.game\\graphics\\TurleUnderWater.png"));


    } catch (IOException e) {
        characterImage = null;
        turtleImage = null;
        playerBoxImage = null;
        fishImage = null;
    }
    }


    public void setBoardModel(BoardModel model) {
        this.boardTableModel = model;
    }

    public void createGameBoard() {
        this.tickCount = 0;
        this.score = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                this.gameBoard[i][j] = emptyCell;
            }
        }
        if (this.fishList != null) {
            this.fishList.clear();
        } else {
            this.fishList = new ArrayList<>();
        }
        if (this.turtles != null) {
            this.turtles.clear();
        } else {
            this.turtles = new ArrayList<>();
        }

        createTurtles();

        this.playerHasPackage = false;
        this.playerColumn = 0;
        this.playerRow = 0;
        this.gameBoard[this.playerColumn][this.playerRow] = playerCell;

        if (unitDigit != null)
        { 
            unitDigit.onStart(new StartEvent(this));
        }
        if (tenDigit != null) {
            tenDigit.onStart(new StartEvent(this));
        }
        if  (hundredDigit != null) {
             hundredDigit.onStart(new StartEvent(this));
        }

        fishLimitUpdate();
    }

    public void keyPressed(KeyEvent e) {
    int oldPlayerColumn = playerColumn;
    int oldPlayerRow = playerRow;
    boolean playerMoved = false;
    boolean stateChangeBox = false;

    char key = e.getKeyChar();

    switch (key) {
        case 's':
        GameThread.getInstance().startGame();
            break;
        case 'a':
            if (playerRow == 1) {
                if (playerColumn == 0) {
                    playerRow = 0;
                    playerMoved = true;
                    if (!playerHasPackage) {
                        playerHasPackage = true;
                        stateChangeBox = true;
                    }
                } else {
                    playerColumn--;
                    playerMoved = true;
                }
            } else if (playerRow == 0) {
                if (playerColumn == columns - 1) {
                    playerRow = 1;
                    playerMoved = true;
                } else {
                }
            }
            break;
        case 'd':
            if (playerRow == 1) {
                if (playerColumn == columns - 1) {
                    playerRow = 0;
                    playerMoved = true;
                    if (playerHasPackage) {
                        playerHasPackage = false;
                        pointsAdd();
                        stateChangeBox = true;
                    }
                } else {
                    playerColumn++;
                    playerMoved = true;
                }
            } else if (playerRow == 0) {
                if (playerColumn == 0) {
                    playerRow = 1;
                    playerMoved = true;
                } else {
                }
            }
            break;
    }

    if (playerMoved || stateChangeBox) {
        if (playerMoved) {
            gameBoard[oldPlayerColumn][oldPlayerRow] = emptyCell;
        }

        if (playerColumn >= 0 && playerColumn < columns && playerRow >= 0 && playerRow < rows) {
            if (playerHasPackage) {
                gameBoard[playerColumn][playerRow] = playerBox;
            } else {
                gameBoard[playerColumn][playerRow] = playerCell;
            }
        }

        if (boardTableModel != null) {
            if (playerMoved) {
                boardTableModel.fireTableCellUpdated(oldPlayerRow, oldPlayerColumn);
            }
            boardTableModel.fireTableCellUpdated(playerRow, playerColumn);
        } else {
        }
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 's') {
            GameThread.getInstance().startGame();
        }
    }

    public int getCell(int column, int row) {
        if (column >= 0 && column < this.columns && row >= 0 && row < this.rows) {
            return this.gameBoard[column][row];
        } else {
            return emptyCell;
        }
    }


    public void keyTyped(KeyEvent e) {
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }


    public void onPlusOne(PlusEvent event) {

        if (event.getSource() == hundredDigit) {
            gameOver();
        }
        fishLimitUpdate();
    }


    public void onTick(TickEvent event) {
       this.tickCount++;

        boolean boardChange = false;

        for (Turtle turtle : turtles) {
    turtle.playerOnTurtle(false);
}

if (playerRow == TURTLE_ROW) {
    for (Turtle turtle : turtles) {
        if (turtle.getColumn() == playerColumn && turtle.isEmerged() && turtle.getRow() == TURTLE_ROW) {
            turtle.playerOnTurtle(true);
            boardChange = true;
            break;
        }
    }
}

        fishLimitUpdate();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (gameBoard[c][r] == fishCell ||
                    gameBoard[c][r] == turtleCell ||
                    gameBoard[c][r] == turtleHunt) {
                    gameBoard[c][r] = emptyCell;
                }
            }
        }

        for (Turtle turtle : turtles) {
            if (turtle.stateUpdate()) {
                boardChange = true;
            }
        }

        Iterator<Fish> fishIterator = fishList.iterator();
        while (fishIterator.hasNext()) {
            Fish currentFish = fishIterator.next();

            if (currentFish.fishMovement()) {
                boardChange = true;
            }

            boolean removedThisTick = false;
            if (currentFish.getRow() == TURTLE_ROW + 1) {
                for (Turtle turtle : turtles) {
                    if (turtle.getColumn() == currentFish.getColumn() && turtle.getHomeRow() == TURTLE_ROW) {
                        if (turtle.isEmerged() && !turtle.isHunting()) {
                            if (turtle.dive(currentFish.getRow())) {
                                boardChange = true;
                            }
                        }
                        fishIterator.remove();
                        removedThisTick = true;
                        boardChange = true;
                        break;
                    }
                }
                if (removedThisTick) {
                    continue;
                }
            }

            if (currentFish.getRow() < TURTLE_ROW) {
                fishIterator.remove();
                boardChange = true;
            }
        }

        if (tickCount % fishSpawn == 0 && fishList.size() < currentMaxFish) {
            int spawnColumn = -1;
            if (columns > 2) {
                do {
                    spawnColumn = random.nextInt(columns);
                } while (spawnColumn == 0 || spawnColumn == columns - 1);
            }

            if (spawnColumn != -1) {
                int spawnRow = rows - 1;
                if (gameBoard[spawnColumn][spawnRow] == emptyCell) {
                    fishList.add(new Fish(spawnColumn, spawnRow));
                    boardChange = true;
                }
            }
        }

        for (Turtle turtle : turtles) {
            if (turtle.getColumn() >= 0 && turtle.getColumn() < columns &&
                turtle.getRow() >= 0 && turtle.getRow() < rows) {

                if (turtle.isEmerged()) {
                    if (gameBoard[turtle.getColumn()][turtle.getRow()] == emptyCell || gameBoard[turtle.getColumn()][turtle.getRow()] != playerCell && gameBoard[turtle.getColumn()][turtle.getRow()] != playerBox) {
                        gameBoard[turtle.getColumn()][turtle.getRow()] = turtleCell;
                    }
                } else {
                    if (gameBoard[turtle.getColumn()][turtle.getRow()] == emptyCell || gameBoard[turtle.getColumn()][turtle.getRow()] != playerCell && gameBoard[turtle.getColumn()][turtle.getRow()] != playerBox) {
                        gameBoard[turtle.getColumn()][turtle.getRow()] = turtleHunt;
                    }
                }
            }
        }

        for (Fish currentFish : fishList) {
            if (currentFish.getRow() >= 0 && currentFish.getRow() < rows &&
                currentFish.getColumn() >= 0 && currentFish.getColumn() < columns) {
                if (gameBoard[currentFish.getColumn()][currentFish.getRow()] == emptyCell) {
                    gameBoard[currentFish.getColumn()][currentFish.getRow()] = fishCell;
                }
            }
        }

        if (playerColumn >= 0 && playerColumn < columns && playerRow >= 0 && playerRow < rows) {
            gameBoard[playerColumn][playerRow] = playerHasPackage ? playerBox : playerCell;
        }

    boolean newGameOverConditionMet = false;
if (playerRow == 1) {
    if (playerColumn >= 0 && playerColumn < columns) {
        int cellStateOnTurtleRowBelowPlayer = gameBoard[playerColumn][TURTLE_ROW];
        if (cellStateOnTurtleRowBelowPlayer == emptyCell) {
            newGameOverConditionMet = true;
        }
    }
}

if (newGameOverConditionMet) {
    gameOver();
    return;
}
            if (boardChange || true) {
            if (boardTableModel != null) {
                boardTableModel.fireTableDataChanged();
            }
        }

        if (this.tickCount > 0 && this.tickCount % 50 == 0) {
            GameThread.getInstance().decreaseDelay();
        }

    }

    public void createTurtles() {
        this.turtles.clear();

        for (int col = 0; col < columns; col = col + 1) {
            this.turtles.add(new Turtle(col, TURTLE_ROW));
        }

       for (Turtle turtle : this.turtles) {
            if (turtle.isEmerged()) this.gameBoard[turtle.getColumn()][turtle.getRow()] = turtleCell;
            else this.gameBoard[turtle.getColumn()][turtle.getRow()] = emptyCell;
        }
    }

    public void setScore(SevenSegmentDigit units, SevenSegmentDigit tens, SevenSegmentDigit hundreds) {
        this.unitDigit = units;
        this.tenDigit = tens;
        this.hundredDigit = hundreds;
         if (this.hundredDigit != null) {
            this.hundredDigit.registerPlusEventListener(this);
        }
        fishLimitUpdate();
    }

    private void fishLimitUpdate() {
        int nonZeroDigits = 0;
        if (unitDigit != null && unitDigit.getCurrentValue() != 0 && unitDigit.getCurrentValue() != -1) {
            nonZeroDigits++;
        }
        if (tenDigit != null && tenDigit.getCurrentValue() != 0 && tenDigit.getCurrentValue() != -1) {
            nonZeroDigits++;
        }
        if (hundredDigit != null && hundredDigit.getCurrentValue() != 0 && hundredDigit.getCurrentValue() != -1) {
            nonZeroDigits++;
        }
        this.currentMaxFish = nonZeroDigits;
    }

    private void pointsAdd() {
        this.score++;
        if (unitDigit != null) {
            unitDigit.onPlusOne(new PlusEvent(this));
        }
    }
 

     public void onStart(StartEvent event) {
    }
 


    public interface GameOverListener {
        void onGameOver();
    }
    private GameOverListener gameOverListener;

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public void gameOver() {
       GameThread.getInstance().stopGame();
    GameThread.resetGame();

    ResetEvent gameResetEvent = new ResetEvent(this);
    if (unitDigit != null) unitDigit.onReset(gameResetEvent);
    if (tenDigit != null) tenDigit.onReset(gameResetEvent);
    if (hundredDigit != null) hundredDigit.onReset(gameResetEvent);

    createGameBoard();  

    if (boardTableModel != null) {
        boardTableModel.fireTableDataChanged();
    }

    if (gameOverListener != null) {
        gameOverListener.onGameOver(); 
    }
}
}
