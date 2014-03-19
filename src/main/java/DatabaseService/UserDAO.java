package DatabaseService;

import java.sql.Connection;
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
    public UserDataSet getByName(String login) throws SQLException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login + "'", new TResultHandler<UserDataSet>(){

            public UserDataSet handle(ResultSet result) throws SQLException {
                result.next();
                return new UserDataSet(result.getLong(1), result.getString(2));
            }

        });
    }
    public void add(UserDataSet dataSet) throws SQLException{
        SimpleExecutor exec = new SimpleExecutor();
        exec.execUpdate(con, "insert into users (login, password) values ('"+dataSet.getLogin()+"', '"+dataSet.getPassword()+"')");
    }
    public void delete(UserDataSet dataSet) throws SQLException{
        SimpleExecutor exec = new SimpleExecutor();
        exec.execUpdate(con, "delete from users where login ='" + dataSet.getLogin() + "'");
    }
    public boolean isUserExists(Connection con, String login) throws  SQLException{
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