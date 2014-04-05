package DatabaseService;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.net.ConnectException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
	private Connection con;
	public UserDAO(Connection con){
		this.con = con;
	}
	public UserDataSet get(long id) throws SQLException{
		TExecutor exec = new TExecutor();
		return exec.execQuery(con, "select * from users where id='" + id + "'", new TResultHandler<UserDataSet>(){

			public UserDataSet handle(ResultSet result) throws SQLException {
				result.next();
                return new UserDataSet(result.getLong(1), result.getString(2));
			}
			
		});
	}
    public UserDataSet getByName(String login) throws SQLException, SocketException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login + "'", new TResultHandler<UserDataSet>(){

            public UserDataSet handle(ResultSet result) throws SQLException {
                result.next();
                return new UserDataSet(result.getLong(1), result.getString(2));
            }

        });
    }
    public boolean add(UserDataSet dataSet) throws SQLException, SocketException {
        PreparedStatement stm = con.prepareStatement("insert into users (login, password) values (?,?)");
        stm.setString(1, dataSet.getLogin());
        stm.setString(2, dataSet.getPassword());
        return stm.execute();

    }
    public void delete(UserDataSet dataSet) throws SQLException{
        SimpleExecutor exec = new SimpleExecutor();
        exec.execUpdate(con, "delete from users where login ='" + dataSet.getLogin() + "'");
    }
    public boolean isUserExists(Connection con, String login) throws  SQLException, SocketException {
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login + "'", new TResultHandler<Boolean>(){
            public Boolean handle(ResultSet result) throws SQLException {
                return result.next();
            }
        });
    }
    public boolean isUserExists(Connection con, String login, String password) throws  SQLException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login +
                "' and password='" + password + "'", new TResultHandler<Boolean>(){
            public Boolean handle(ResultSet result) throws SQLException {
                return result.next();
            }
        });
    }

}
