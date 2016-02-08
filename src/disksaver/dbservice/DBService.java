package disksaver.dbservice;

import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */
public interface DBService {
    void printConnectInfo();

    void close();

    List<String> getProfileCategories() throws DBException;

    long addNewProfileCategory(String categoryName, String categoryDesc) throws DBException;
}
