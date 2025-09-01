import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class Farm {
    public final int rows;
    public final int columns;
    public Sheep winnerSheep = null;
    private int NoofSheeps = 16;
    private int NoofDogs = 8;
    private final HashMap<String, Cell> area = new HashMap<>();
    private final List<Sheep> sheepList = Collections.synchronizedList(new ArrayList<>());
    private final List<Dog> dogList = Collections.synchronizedList(new ArrayList<>());
    private final AtomicBoolean sheepEscaped = new AtomicBoolean(false);

    public Farm(int rows, int columns) {
        this.rows = (rows % 3 == 0 && rows >= 8) ? rows : 14;
        this.columns = (columns % 3 == 0 && columns >= 8) ? columns : 14;
        initializeFarm();
    }

    
    private void initializeFarm() {
        intializingTheAreaOfTheFarm();
        initialingTheGates();
        placingTheAnimlas();
    }

    private void intializingTheAreaOfTheFarm() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String key = i + "," + j;
                Cell cell;
                if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) {
                    cell = new WallCell();
                } else {
                    cell = new EmptyCell();
                }
                area.put(key, cell);
            }
        }
    }

    private void initialingTheGates() {
        int topGate = ThreadLocalRandom.current().nextInt(columns - 2) + 1;
        int bottomGate = ThreadLocalRandom.current().nextInt(columns - 2) + 1;
        int leftGate = ThreadLocalRandom.current().nextInt(rows - 2) + 1;
        int rightGate = ThreadLocalRandom.current().nextInt(rows - 2) + 1;

        area.put("0," + topGate, new GateCell());
        area.put((rows - 1) + "," + bottomGate, new GateCell());
        area.put(leftGate + ",0", new GateCell());
        area.put(rightGate + "," + (columns - 1), new GateCell());
    }

    
    private void placingTheAnimlas() {
        char sheepId = 'A';
        int startRow = rows / 3 + 1;
        int startCol = columns / 3 + 1;

        for (int i = 0; i < NoofSheeps; i++) {
            int row = startRow + (i / 4);
            int col = startCol + (i % 4);
            String key = row + "," + col;

            if (area.get(key) instanceof EmptyCell) {
                Sheep sheep = new Sheep(this, row, col, sheepId++);
                sheepList.add(sheep);
                area.put(key, sheep);
            }
        }

        for (int i = 0; i < NoofDogs; i++) {
            boolean inOuterZone;
            int row, col;
            do {
                row = ThreadLocalRandom.current().nextInt(rows);
                col = ThreadLocalRandom.current().nextInt(columns);

                inOuterZone = (row < rows / 3 || row >= 2 * rows / 3) ||
                        (col < columns / 3 || col >= 2 * columns / 3);
            } while (!(area.get(row + "," + col) instanceof EmptyCell) || !inOuterZone);

            Dog dog = new Dog(this, row, col, i);
            dogList.add(dog);
            area.put(row + "," + col, dog);
        }
    }

    
    public boolean moveAnimal(Animal animal, int newRow, int newCol) {
        int oldRow = animal.getRow();
        int oldCol = animal.getCol();
        String newKey = newRow + "," + newCol;
        String oldKey = oldRow + "," + oldCol;

        
        Cell sourceCell = getCell(oldRow, oldCol);
        Cell targetCell = getCell(newRow, newCol);


        if (!(targetCell instanceof EmptyCell ||
                (animal instanceof Sheep && targetCell instanceof GateCell))) {
            return false;
        }

        
        sourceCell.getLock().lock();
        try {
            targetCell.getLock().lock();
            try {
                
                if (area.get(oldKey) != animal) {
                    return false;
                }

                if (targetCell instanceof GateCell && animal instanceof Sheep) {
                    area.put(oldKey, new EmptyCell());
                    area.put(newKey, animal);
                    animal.setPosition(newRow, newCol);
                    sheepEscaped.set(true);
                    winnerSheep = (Sheep) animal;
                    return true;
                } else if (targetCell instanceof EmptyCell) {
                    area.put(oldKey, new EmptyCell());
                    area.put(newKey, animal);
                    animal.setPosition(newRow, newCol);
                    return true;
                }
                return false;
            } finally {
                targetCell.getLock().unlock();
            }
        } finally {
            sourceCell.getLock().unlock();
        }
    }

    public Cell getCell(int row, int col) {
        return area.get(row + "," + col);
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < columns;
    }

    public boolean isSheepEscaped() {
        return sheepEscaped.get();
    }

    public void setSheepEscaped(boolean escaped) {
        sheepEscaped.set(escaped);
    }

    public synchronized void displayTheFarm() {
        System.out.print("\033[H\033[2J");
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String key = i + "," + j;
                Cell cell = area.get(key);
                display.append(cell != null ? cell.toString() : " ");
                display.append(" ");
            }
            display.append("\n");
        }
        System.out.println(display.toString());
    }

    public List<Sheep> getSheepList() {
        return sheepList;
    }

    public List<Dog> getDogList() {
        return dogList;
    }
}