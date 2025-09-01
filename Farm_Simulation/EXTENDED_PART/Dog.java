import java.util.concurrent.ThreadLocalRandom;

public class Dog extends Animal {
    private final int id;
    private static final int[][] DIRECTIONS = {
            { -1, 0 }, 
            { 1, 0 }, 
            { 0, -1 }, 
            { 0, 1 } 
    };

    public Dog(Farm farm, int startRow, int startCol, int id) {
        super(farm, startRow, startCol);
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    protected void move() {
        int currentRow = getRow();
        int currentCol = getCol();

        
        int[] shuffledDirections = getShuffledDirections();


        for (int dirIndex : shuffledDirections) {
            int newRow = currentRow + DIRECTIONS[dirIndex][0];
            int newCol = currentCol + DIRECTIONS[dirIndex][1];

            
            if (!isValidMove(newRow, newCol)) {
                continue;
            }

            
            if (tryMove(newRow, newCol)) {
                break;
            }
        }
    }

    private int[] getShuffledDirections() {
        int[] indices = { 0, 1, 2, 3 };
        for (int i = indices.length - 1; i > 0; i--) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            int temp = indices[i];
            indices[i] = indices[j];
            indices[j] = temp;
        }
        return indices;
    }

    private boolean isInCentralZone(int row, int col) {
        return row >= farm.rows / 3 && row < 2 * farm.rows / 3 &&
                col >= farm.columns / 3 && col < 2 * farm.columns / 3;
    }

    @Override
    protected boolean isValidMove(int newRow, int newCol) {
        
        if (!farm.isWithinBounds(newRow, newCol)) {
            return false;
        }

        
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }

        
        if (isInCentralZone(newRow, newCol)) {
            return false;
        }

        
        
        Cell targetCell = farm.getCell(newRow, newCol);
        return targetCell instanceof EmptyCell;
    }

    public int getId() {
        return id;
    }

    
    @Override
    protected boolean tryMove(int newRow, int newCol) {
        
        boolean moved = super.tryMove(newRow, newCol);


        if (!moved) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(30, 100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return moved;
    }
}