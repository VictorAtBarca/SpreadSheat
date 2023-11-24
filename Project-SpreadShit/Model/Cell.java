package Model;

import Model.CellContents.FormulaContent;
import Model.CellContents.TextContent;

public class Cell {

    private String column;
    private int row;
    private CellContent content;

    public Cell(String column, int row, CellContent content) {
        this.column = column;
        this.row = row;
        this.content = content;
    }

    public String getCellKey() {
        return column + row;
    }

    public CellContent getContent() {
        return content;
    }

    public double evaluateCell(Spreadsheet spreadsheet) {

        if (getContent() instanceof FormulaContent) {

            FormulaContent formulaContent = (FormulaContent) getContent();
            String formula = (String) formulaContent.getValue();
    
            String[] ListCellKey = formula.split("[+\\-*/]");
            String[] ListOperator = formula.split("[A-Z0-9]+");
            
            double result = 0.0;
            double currentValue;
    
            for (int i = 0; i < ListOperator.length; i++) {
                String operator = ListOperator[i];

                if (isNumeric(ListCellKey[i])) {
                    currentValue = Double.parseDouble(ListCellKey[i]);
                } else {
                    currentValue = evaluateTerm(spreadsheet, ListCellKey[i]);
                }
    
                if (i == 0) {
                    result = currentValue; // Initialisation
                } else {
                    result = applyOperator(result, operator, currentValue);
                }
            }
    
            return result;
            
        } else {
            return (double) getContent().getValue();
        }
    }
    
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double applyOperator(double operand1, String operator, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                System.out.println("Invalid operator: " + operator);
                return 0.0;
        }
    }
    
    private double evaluateTerm(Spreadsheet spreadsheet, String cellKey) {
        Cell cell = spreadsheet.getCell(cellKey);
    
        if (cell == null) {
            System.out.println("Invalid cell reference: " + cellKey);
            return 0.0;
        }
    
        if (cell.getContent() instanceof FormulaContent) {
            return cell.evaluateCell(spreadsheet);
        } else if (cell.getContent() instanceof TextContent) {
            System.out.println(cellKey + " Error: Content is a String");
            return 0.0;
        } else {
            return (double) cell.getContent().getValue();
        }
    }
    
    @Override
    public String toString() {
        return content.toString();
    }
}