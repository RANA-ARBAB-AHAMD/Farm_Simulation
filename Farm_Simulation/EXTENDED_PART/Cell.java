import java.util.concurrent.locks.ReentrantLock;

public abstract class Cell {
    private final ReentrantLock lock = new ReentrantLock(true);

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public abstract String toString();
}

class WallCell extends Cell {
    @Override
    public String toString() {
        return "#";
    }
}

class GateCell extends Cell {
    @Override
    public String toString() {
        return " ";
    }
}

class EmptyCell extends Cell {
    @Override
    public String toString() {
        return " ";
    }
}