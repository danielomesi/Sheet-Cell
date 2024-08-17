package engine;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XMLHandler {
    public static <T> T loadXmlToObject(String xmlFilePath, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        File xmlFile = new File(xmlFilePath);
        return clazz.cast(unmarshaller.unmarshal(xmlFile));
    }
}
