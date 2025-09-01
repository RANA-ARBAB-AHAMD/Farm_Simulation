import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Animal extends Cell implements Runnable {
    private int row;
    private int col;
    protected final Farm farm;

    public Animal(Farm farm, int startRow, int startCol) {
        super();
        this.farm = farm;
        this.row = startRow;
        this.col = startCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    protected void setPosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    protected abstract void move();

    protected boolean tryMove(int newRow, int newCol) {
        if (!isValidMove(newRow, newCol)) {
            return false;
        }

        boolean moved = farm.moveAnimal(this, newRow, newCol);

        if (!moved) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 150));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return moved;
    }

    protected boolean isValidMove(int newRow, int newCol) {
        if (!(newRow >= 0 && newRow < farm.rows &&
                newCol >= 0 && newCol < farm.columns)) {
            return false;
        }

        Cell targetCell = farm.getCell(newRow, newCol);
        return targetCell instanceof EmptyCell ||
                (this instanceof Sheep && targetCell instanceof GateCell);
    }

    @Override
    public void run() {
        while (!farm.isSheepEscaped() && !Thread.currentThread().isInterrupted()) {
            try {
                move();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted in the run function of the Animal class");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public ReentrantLock getLock() {
        return super.getLock();
    }
}