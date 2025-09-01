import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends Animal {
    private final char name;

    public Sheep(Farm farm, int startRow, int startCol, char name) {
        super(farm, startRow, startCol);
        this.name = name;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    @Override
    protected void move() {
        int newRow, newCol;
        int currentRow = getRow();
        int currentCol = getCol();


        int horizontalMovement = detectDogsAndDecideDirection(true);
        int verticalMovement = detectDogsAndDecideDirection(false);

        newRow = currentRow + verticalMovement;
        newCol = currentCol + horizontalMovement;

        
        newRow = Math.max(0, Math.min(newRow, farm.rows - 1));
        newCol = Math.max(0, Math.min(newCol, farm.columns - 1));

        
        boolean moved = tryMove(newRow, newCol);

        
        if (!moved) {
            for (int attempts = 0; attempts < 4 && !moved; attempts++) {
                newRow = currentRow + ThreadLocalRandom.current().nextInt(-1, 2);
                newCol = currentCol + ThreadLocalRandom.current().nextInt(-1, 2);

                newRow = Math.max(0, Math.min(newRow, farm.rows - 1));
                newCol = Math.max(0, Math.min(newCol, farm.columns - 1));

                moved = tryMove(newRow, newCol);


                if (!moved) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 50));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected boolean isValidMove(int newRow, int newCol) {
        if (!farm.isWithinBounds(newRow, newCol)) {
            return false;
        }

        
        if (newRow == getRow() && newCol == getCol()) {
            return false;
        }

        Cell targetCell = farm.getCell(newRow, newCol);
        return targetCell instanceof EmptyCell || targetCell instanceof GateCell;
    }

    private int detectDogsAndDecideDirection(boolean isHorizontal) {
        int currentRow = getRow();
        int currentCol = getCol();
        int checkPosition = isHorizontal ? currentCol : currentRow;
        int dimensionSize = isHorizontal ? farm.columns : farm.rows;

        boolean negativeDirectionDog = false;
        boolean positiveDirectionDog = false;

        
        if (checkPosition > 0) {
            negativeDirectionDog = isDogPresent(isHorizontal, checkPosition - 1, currentRow, currentCol);
        }
        if (checkPosition < dimensionSize - 1) {
            positiveDirectionDog = isDogPresent(isHorizontal, checkPosition + 1, currentRow, currentCol);
        }


        if (negativeDirectionDog && positiveDirectionDog) {
            return 0; 
        } else if (negativeDirectionDog) {
            return 1; 
        } else if (positiveDirectionDog) {
            return -1; 
        }

        return ThreadLocalRandom.current().nextInt(-1, 2); 
    }

    private boolean isDogPresent(boolean isHorizontal, int position, int currentRow, int currentCol) {
        int checkRow = isHorizontal ? currentRow : position;
        int checkCol = isHorizontal ? position : currentCol;

        Cell cell = farm.getCell(checkRow, checkCol);
        return cell instanceof Dog;
    }

    public char getName() {
        return name;
    }
}