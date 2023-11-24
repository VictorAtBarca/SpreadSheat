package Model.CellContents;
import Model.CellContent;

public class FormulaContent extends CellContent {
    
    private String formula;

    public FormulaContent(String formula) {
       this.formula = formula;
    }
    
    public Object getValue() {
        return formula;
    }
}
