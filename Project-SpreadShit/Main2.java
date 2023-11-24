import Model.Cell;
import Model.CellContent;
import Model.Spreadsheet;
import Model.CellContents.FormulaContent;
import Model.CellContents.NumericalContent;
import Model.CellContents.TextContent;

public class Main2 {
   
         public static void main(String[] args) {
        // Création d'une feuille de calcul
        Spreadsheet spreadsheet = new Spreadsheet();

        Cell cell = new Cell("A", 1, new FormulaContent("A2+A3"));

        // Ajout des cellules à la feuille de calcul
        spreadsheet.addCell(cell);
        spreadsheet.addCell(new Cell("A", 2, new FormulaContent("B1+B2")));
        spreadsheet.addCell(new Cell("A",3, new NumericalContent(3.0)));
        spreadsheet.addCell(new Cell("B",1, new FormulaContent("C1+C2")));
        spreadsheet.addCell(new Cell("B",2, new NumericalContent(3.0)));
        spreadsheet.addCell(new Cell("C",1, new NumericalContent(3.0)));
         spreadsheet.addCell(new Cell("C",2, new NumericalContent(3.0)));
        
        

        // Calcul de la priorité pour une cellule spécifique
        int priorityA1 = calculatePriority(cell,spreadsheet);

        // Affichage des résultats
        System.out.println("Priority for A1: " + priorityA1);
    }

    
    public boolean loopcheck(Cell cell, Spreadsheet spreadsheet) {
        if (cell.getContent() instanceof FormulaContent) {
            String formula = (String) cell.getContent().getValue();
            String[] ListCellKey = formula.split("[+\\-*/]");
    
            boolean validFormula = true;
    
            for (String cellKey : ListCellKey) {

                Cell referencedCell = spreadsheet.getCell(cellKey);
    
                if (referencedCell.getContent() instanceof FormulaContent) {
                    // Recursive check for the referenced cell
                    validFormula = validFormula && loopcheck(referencedCell, spreadsheet);
                } else if (referencedCell.getContent() instanceof TextContent) {
                    System.out.println("Invalid formula. Cell reference '" + cellKey + "' contains text.");
                    validFormula = false;
                    break;
                } else if (referencedCell.getCellKey().equals(cell.getCellKey())) {
                    System.out.println("Invalid formula. Cell reference '" + cellKey + "' references the same cell.");
                    validFormula = false;
                    break;
                }
            }
    
            return validFormula;
        }
    
        
        return true;
    }
}

    




