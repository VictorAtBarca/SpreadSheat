package Model.CellContents;
import Model.CellContent;


public class TextContent extends CellContent {

    private String text;

    public TextContent(String text) {
        this.text = text;
    }

    public Object getValue() {
        return text;
    }
}
