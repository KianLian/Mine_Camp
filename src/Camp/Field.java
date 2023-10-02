package Camp; // Package declaration

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Field {

    private final int column;
    private final int row;
    
    private boolean opened;
    private boolean marked;
    private boolean mined;
    
    private List<Field> neighbors = new ArrayList<>();
    
    private List<BiConsumer<Field, FieldEvent>> observers = new ArrayList<>();
    
    public Field(int row, int column){
        this.column = column;
        this.row = row;
    }
    
    public void registerObserver(BiConsumer<Field, FieldEvent> fieldObs) {
        observers.add(fieldObs);
    }
    
    private void notifyObservers(FieldEvent event) {
        observers.forEach(obs -> obs.accept(this, event));
    }
    
    public boolean addNeighbor(Field neighbor) {
        boolean diagonal = (row != neighbor.row) && (column != neighbor.column);
        int overallDelta = Math.abs(column - neighbor.column) + Math.abs(row - neighbor.row);
        
        if((overallDelta == 1 && !diagonal) || (overallDelta == 2 && diagonal)){
            neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }
    
    public void toggleMark() {
        if(!opened) {
            marked = !marked;
            if(marked) {
                notifyObservers(FieldEvent.MARK);
            } else {
                notifyObservers(FieldEvent.UNMARK);
            }
        }
    }
    
    public boolean open() {
        if(!opened && !marked) {            
            if(mined) {
                notifyObservers(FieldEvent.EXPLODE);
                return true;
            }
            
            opened = true;
            setOpen(opened);
            
            if(isSafeNeighborhood()) {
                neighbors.forEach(n -> n.open());
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean isSafeNeighborhood() {
        return neighbors.stream().noneMatch(n -> n.mined);
    }
    
    public boolean isMarked() {
        return marked;
    }
    
    public boolean isMined() {
        return mined;
    }
    
    public void mine() {
        mined = true;
    }
    
    public boolean isOpened() {
        return opened;
    }
    
    public int getColumn() {
        return column;
    }
    
    public int getRow() {
        return row;
    }
    
    public boolean isGoalAchieved() {
        boolean uncovered = !mined && opened;
        boolean protectedField = mined && marked;
        return protectedField || uncovered;
    }
    
    public void setOpen(boolean opened) {
        this.opened = opened;
        
        if(opened) {
            notifyObservers(FieldEvent.OPEN);
        }
    }
    
    public long minesInNeighborhood() {
        return neighbors.stream().filter(n -> n.mined).count();
    }
    
    public void restart() {
        mined = false;
        marked = false;
        opened = false;
        
        notifyObservers(FieldEvent.RESTART);
    }
}
