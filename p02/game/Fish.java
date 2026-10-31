package p02.game;
import java.util.Random;
 
public class Fish {
    private int column;
    private int row;
    private int moveSpeed;
    private int moveCooldown;

    private static Random random = new Random();

    public Fish(int column, int row) {
        this.column = column;
        this.row = row;
        this.moveSpeed = random.nextInt(4) + 1;
        this.moveCooldown = this.moveSpeed;
    }

    public int getColumn() { 
        return column; 
    }
    public int getRow() {
         return row;
         }

    public boolean fishMovement() {
        moveCooldown--;
        if(moveCooldown <= 0){
            this.row--;
            this.moveCooldown = this.moveSpeed;
            return true;
        }
        return false;
    }
}
