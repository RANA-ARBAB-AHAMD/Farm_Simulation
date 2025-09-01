import java.util.concurrent.ThreadLocalRandom;

public class Dog extends Animal {
    private final int id;

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

        int[][] directions = {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        boolean moved = false;
        int[] usedDirections = new int[directions.length];
        int directionsTried = 0;

        while (!moved && directionsTried < directions.length) {

            int dirIndex;
            do {
                dirIndex = ThreadLocalRandom.current().nextInt(directions.length);
            } while (usedDirections[dirIndex] == 1);

            usedDirections[dirIndex] = 1;
            directionsTried++;

            int newRow = currentRow + directions[dirIndex][0];
            int newCol = currentCol + directions[dirIndex][1];

            if (farm.isWithinBounds(newRow, newCol)) {
                Cell targetCell = farm.getCell(newRow, newCol);

                if (targetCell instanceof EmptyCell && !isInCentralZone(newRow, newCol)) {
                    moved = tryMove(newRow, newCol);
                }
            }
        }
    }

    private boolean isInCentralZone(int row, int col) {
        return row >= farm.rows / 3 && row < 2 * farm.rows / 3 &&
                col >= farm.columns / 3 && col < 2 * farm.columns / 3;
    }

    @Override
    protected boolean isValidMove(int newRow, int newCol) {

        if (!super.isValidMove(newRow, newCol)) {
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
}