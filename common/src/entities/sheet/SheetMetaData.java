package entities.sheet;

public class SheetMetaData {
    private final String sheetName;
    private final String uploaderName;
    private final int numberOfRows;
    private final int numberOfCols;

    public SheetMetaData(String sheetName, String uploaderName, int numberOfRows, int numberOfCols) {
        this.sheetName = sheetName;
        this.uploaderName = uploaderName;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
    }

    public String getSheetName() {return sheetName;}
    public String getUploaderName() {return uploaderName;}
    public int getNumberOfRows() {return numberOfRows;}
    public int getNumberOfCols() {return numberOfCols;}
}
