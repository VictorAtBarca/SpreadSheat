package Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spreadsheet {

    private Map<String, Cell> spreadsheet = new HashMap<>();

    public void addCell(Cell cell) {
        spreadsheet.put(cell.getCellKey(), cell);
    }

    public Cell getCell(String cellKey) {
        return spreadsheet.get(cellKey);
    }

    public List<Cell> getCells() {
        return new ArrayList<>(spreadsheet.values());
    }
}




