package engine;

import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.stl.*;
import jakarta.xml.bind.Marshaller;
import service_exceptions.InvalidPathDetectedException;
import service_exceptions.SheetLoadFailureException;
import service_exceptions.SheetSaveFailureException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileIOHandler {
    public static <T> T loadXMLToObject(String xmlFilePath, Class<T> clazz) throws JAXBException {
        validateFileExistence(xmlFilePath);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        File xmlFile = new File(xmlFilePath);
        return clazz.cast(unmarshaller.unmarshal(xmlFile));
    }

    public static <T> T loadXMLStringToObject(String xmlString, Class<T> clazz) throws JAXBException {
        if (xmlString == null || xmlString.trim().isEmpty()) {
            throw new IllegalArgumentException("The XML string cannot be null or empty");
        }

        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Use try-with-resources to ensure StringReader is closed
        try (StringReader stringReader = new StringReader(xmlString)) {
            return clazz.cast(unmarshaller.unmarshal(stringReader));  // Safe casting
        }
    }

    public static String getXMLOfObject(CoreSheet coreSheet, String fileName) {
        if (coreSheet == null) {
            throw new IllegalArgumentException("The object to save cannot be null");
        }



        STLLayout stlLayout = new STLLayout();
        stlLayout.setRows(coreSheet.getNumOfRows());
        stlLayout.setColumns(coreSheet.getNumOfCols());

        STLSize stlSize = new STLSize();
        stlSize.setColumnWidthUnits(coreSheet.getLayout().getColumnWidthUnits());
        stlSize.setRowsHeightUnits(coreSheet.getLayout().getRowHeightUnits());

        stlLayout.setSTLSize(stlSize);

        STLRanges stlRanges = new STLRanges();
        List<STLRange> stlRangesList = new ArrayList<>();
        stlRanges.setSTLRange(stlRangesList);

        for (Map.Entry<String, Range> entry :coreSheet.getRangesMap().entrySet()) {
            STLBoundaries stlBoundaries = new STLBoundaries();
            stlBoundaries.setFrom(entry.getValue().findMostLeftUpper().getCellID());
            stlBoundaries.setTo(entry.getValue().findMostBottomRight().getCellID());
            STLRange stlRange = new STLRange();
            stlRange.setSTLBoundaries(stlBoundaries);
            stlRange.setName(entry.getKey());
            stlRangesList.add(stlRange);
        }

        STLCells stlCells = new STLCells();
        List<STLCell> stlCellsList = new ArrayList<>();
        stlCells.setSTLCell(stlCellsList);

        for (Map.Entry<Coordinates, CoreCell> entry : coreSheet.getCoreCellsMap().entrySet()) {
            Coordinates cellCoordinates = entry.getKey();
            CoreCell coreCell = entry.getValue();
            STLCell stlCell = new STLCell();
            stlCell.setRow(cellCoordinates.getRow() + 1);
            stlCell.setColumn(Coordinates.numberToLetter(cellCoordinates.getCol()));
            stlCell.setSTLOriginalValue(coreCell.getOriginalExpression());
            stlCellsList.add(stlCell);
        }

        STLSheet stlSheet = new STLSheet();
        stlSheet.setName(fileName);
        stlSheet.setSTLCells(stlCells);
        stlSheet.setSTLRanges(stlRanges);
        stlSheet.setSTLLayout(stlLayout);

        try {
            JAXBContext context = JAXBContext.newInstance(STLSheet.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(stlSheet, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Error while marshalling the object to XML", e);
        }
    }

    public static void saveCoreSheetsToFile(List<CoreSheet> coreSheets, String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            validateExistenceOfParentDirectory(parentDir, file.getName());
        }
        else {
            File cwd = new File(System.getProperty("user.dir"));
            file = new File(cwd,file.getName());
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(coreSheets);
        } catch (Exception e) {
            throw new SheetSaveFailureException("Could not save CoreSheets to " + filePath);
        }
    }

    public static List<CoreSheet> loadCoreSheetsFromFile(String filePath) {
        validateFileExistence(filePath);
        File file = new File(filePath);
        List<CoreSheet> coreSheets = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            coreSheets = (List<CoreSheet>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SheetLoadFailureException("Could not load CoreSheets from " + filePath);
        }
        return coreSheets;
    }

    public static void validateFileExistence(String fullFilePath) {
        File file = new File(fullFilePath);

        if (!file.exists()) {
            throw new InvalidPathDetectedException("File does does not exist", fullFilePath);
        }
    }

    public static void validateExistenceOfParentDirectory(File parentDir, String fileName) {
        if (!parentDir.exists()) {
            throw new InvalidPathDetectedException("Couldn't find the parent directory of the file: " + fileName ,parentDir.getAbsolutePath());
        }
    }
}
