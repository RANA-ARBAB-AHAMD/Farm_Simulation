public abstract class Cell {
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