package gui.scenes.dashboard.sheetsTable;

import javafx.beans.property.SimpleStringProperty;

public class SheetTableEntry {
    private final SimpleStringProperty uploader;
    private final SimpleStringProperty sheetName;
    private final SimpleStringProperty sheetSize;
    private final SimpleStringProperty accessLevel;

    public SheetTableEntry(String uploader, String sheetName, String sheetSize, String accessLevel) {
        this.uploader = new SimpleStringProperty(uploader);
        this.sheetName = new SimpleStringProperty(sheetName);
        this.sheetSize = new SimpleStringProperty(sheetSize);
        this.accessLevel = new SimpleStringProperty(accessLevel);
    }

    //getters
    public String getUploader() {return uploader.get();}
    public String getSheetName() {return sheetName.get();}
    public String getSheetSize() {return sheetSize.get();}
    public String getAccessLevel() {return accessLevel.get();}

    //setters
    public void setUploader(String uploader) {this.uploader.set(uploader);}
    public void setSheetName(String sheetName) {this.sheetName.set(sheetName);}
    public void setSheetSize(String sheetSize) {this.sheetSize.set(sheetSize);}
    public void setAccessLevel(String accessLevel) {this.accessLevel.set(accessLevel);}
}

