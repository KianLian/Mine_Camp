package Camp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Board implements BiConsumer<Field, FieldEvent> {

    private int rows, columns, mines;
    
    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<Boolean>> observers = new ArrayList<>();
    
    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
        
        generateFields();
        associateNeighbors();
        initializeMines();
    }
    
    public void forEachField(Consumer<Field> function) {
        fields.forEach(function);
    }
    
    public void registerObserver(Consumer<Boolean> observer) {
        observers.add(observer);
    }
    
    public void notifyObservers(Boolean result) {
        observers.forEach(obs -> obs.accept(result));
    }

    void generateFields() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Field field = new Field(i, j);
                field.registerObserver(this);
                fields.add(field);
            }
        }
    }

    void associateNeighbors() {
        for(Field f1: fields) {
            for(Field f2: fields) {
                f1.addNeighbor(f2);
            }
        }
    }

    void initializeMines() {
        long armedMines = 0;
        while (armedMines < mines) {
            int randomIndex = (int)(Math.random() * fields.size());
            if (!fields.get(randomIndex).isMined()) {
                fields.get(randomIndex).mine();
                armedMines++;
            }
        }
    }

    
    public boolean hasWon() {
        return fields.stream().allMatch(f -> f.isGoalAchieved());
    }
    
    public void restart() {
        fields.forEach(Field::restart);
        initializeMines();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                sb.append(" ");
                sb.append(fields.get(index));
                sb.append(" ");
                index++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public void open(int row, int col) {
        fields.parallelStream()
            .filter(f -> f.getRow() == row && f.getColumn() == col)
            .findFirst().ifPresent(Field::open);
    }
    
    private void showMines() {
        fields.stream().filter(Field::isMined).forEach(f -> f.setOpen(true));
    }
    
    public void mark(int row, int col) {
        fields.parallelStream()
            .filter(f -> f.getRow() == row && f.getColumn() == col)
            .findFirst().ifPresent(Field::toggleMark);
    }

    @Override
    public void accept(Field field, FieldEvent event) {
        if(event == FieldEvent.EXPLODE) {
            showMines();
            notifyObservers(false);
        } else if(hasWon()) {
            notifyObservers(true);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
