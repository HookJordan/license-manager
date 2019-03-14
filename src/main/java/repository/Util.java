package repository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public static void writeFile(String path, String data) {
        try {
            Files.write(Paths.get(path), data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
