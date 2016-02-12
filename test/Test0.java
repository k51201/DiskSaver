import disksaver.dbservice.DBException;
import disksaver.dbservice.DBService;
import disksaver.dbservice.MySqlDBService;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created by vampa on 09.02.2016.
 */
public class Test0 {
    public static void main(String[] args) throws DBException {
        DBService dbService = new MySqlDBService();
//        dbService.addDiskProfile("name", "volName", 100500l, "description", new Date(), new Date(), 1);
        long elementCategory = dbService.addElementCategory("no category", "no specified");
        dbService.addElement("name", "path", "desc", 100500l, false, elementCategory, 1l);

        dbService.close();
    }
}
