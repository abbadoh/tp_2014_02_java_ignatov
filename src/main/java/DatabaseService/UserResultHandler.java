package DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gumo on 07/03/14.
 */

public class UserResultHandler implements ResultHandler {
    public void handle(ResultSet result) throws SQLException {
        result.next();
        System.out.append("User: " + result.getString("login") + '\n');
    }
}
