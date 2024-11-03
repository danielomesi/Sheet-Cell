package engine;

import entities.sheet.CoreSheet;
import service_exceptions.InvalidPathDetectedException;
import service_exceptions.SheetLoadFailureException;
import service_exceptions.SheetSaveFailureException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.util.List;

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
