
package runbots;

public class Cell {
        
    private int row;
    private int column;
    
    public Cell(int row, int column){
        this.row = row;
        this.column = column;
    }
    
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Cell{" + "row=" + row + ", column=" + column + '}';
    }
    
    
}
