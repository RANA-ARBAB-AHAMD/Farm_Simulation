public abstract class Animal extends Cell implements Runnable {

    private volatile int row;
    private volatile int col;
    protected final Farm farm;

    public Animal(Farm farm, int startRow, int startCol) {
        this.farm = farm;
        this.row = startRow;
        this.col = startCol;
    }

    public synchronized int getRow() {
        return row;
    }

    public synchronized int getCol() {
        return col;
    }

    protected synchronized void setPosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    protected abstract void move();

    protected synchronized boolean tryMove(int newRow, int newCol) {
        if (!isValidMove(newRow, newCol)) {
            return false;
        }

        return farm.moveAnimal(this, newRow, newCol);
    }

    protected boolean isValidMove(int newRow, int newCol) {
        return newRow >= 0 && newRow < farm.rows &&
                newCol >= 0 && newCol < farm.columns;
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
}