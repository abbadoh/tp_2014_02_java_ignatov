package DatabaseService;

import org.json.JSONException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PreparedExecutor extends SimpleExecutor {
    public static <T> T execQuery(Connection connection, TResultHandler<T> handler, String query, Object... vars) throws SQLException, JSONException {
        PreparedStatement stm = connection.prepareStatement(query);
        for (int i = 0; i < vars.length; i++) {
            stm.setObject(i + 1, vars[i]);
        }
        ResultSet resultSet = stm.executeQuery();
        T value = handler.handle(resultSet);
        resultSet.close();
        stm.close();
        return value;
    }

    public static int execUpdate(Connection connection, String query, Object... vars) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(query);

        for (int i = 0; i < vars.length; i++) {
            stm.setObject(i + 1, vars[i]);
        }
        int affected = stm.executeUpdate();
        stm.close();
        return affected;
    }
}
