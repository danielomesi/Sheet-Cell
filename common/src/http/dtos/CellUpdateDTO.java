package http.dtos;

public class CellUpdateDTO {
    private final String cellName;
    private final String expression;
    private final String sheetName;
    public CellUpdateDTO(String cellName, String expression, String sheetName) {
        this.cellName = cellName;
        this.expression = expression;
        this.sheetName = sheetName;
    }
    public String getCellName() {return cellName;}
    public String getExpression() {return expression;}
    public String getSheetName() {return sheetName;}
}
