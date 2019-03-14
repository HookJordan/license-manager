package repository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Util {
    final static Logger log = Logger.getAnonymousLogger();


    public static String fileAsString(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded);
        } catch (Exception e){
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
