package engine;

import entities.sheet.CoreSheet;
import exceptions.InvalidPathDetectedException;
import exceptions.NoExistenceException;
import exceptions.SheetLoadFailureException;
import exceptions.SheetSaveFailureException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.util.List;

public class FileIOHandler {
    public static <T> T loadXMLToObject(String xmlFilePath, Class<T> clazz) throws JAXBException {
        xmlFilePath +=".xml";
        validatePathToXMLFile(xmlFilePath);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        File xmlFile = new File(xmlFilePath);
        return clazz.cast(unmarshaller.unmarshal(xmlFile));
    }

    public static void saveCoreSheetsToFile(List<CoreSheet> coreSheets, String filePath) {
        filePath +=".dat";
        File file = new File(filePath);
        validateExistenceOfParentDirectory(file.getParentFile(), file.getName());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(coreSheets);
        } catch (Exception e) {
            throw new SheetSaveFailureException("Could not save CoreSheets to " + filePath);
        }
    }

    public static List<CoreSheet> loadCoreSheetsFromFile(String filePath) {
        filePath +=".dat";
        File file = new File(filePath);
        validateExistenceOfParentDirectory(file.getParentFile(), file.getName());
        List<CoreSheet> coreSheets = null;
        if (!file.exists()) {
            throw new NoExistenceException("File not found", filePath);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            coreSheets = (List<CoreSheet>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SheetLoadFailureException("Could not load CoreSheets from " + filePath);
        }
        return coreSheets;
    }

    public static void validatePathToXMLFile(String path) {

        File file = new File(path);
        validateExistenceOfParentDirectory(file.getParentFile(), file.getName());

        if (!file.exists()) {
            throw new InvalidPathDetectedException("File does does not exist", path);
        }
    }

    public static void validateExistenceOfParentDirectory(File parentDir, String fileName) {
        if (!parentDir.exists()) {
            throw new InvalidPathDetectedException("Couldn't find the parent directory of the file: " + fileName ,parentDir.getAbsolutePath());
        }
    }
}
