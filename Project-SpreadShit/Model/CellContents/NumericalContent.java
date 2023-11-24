package Model.CellContents;
import Model.CellContent;

public class NumericalContent extends CellContent {
    
    private double value;

    public NumericalContent(double value) {
        this.value = value;
    }
    
    public Object getValue() {
        return value;
    }
}
