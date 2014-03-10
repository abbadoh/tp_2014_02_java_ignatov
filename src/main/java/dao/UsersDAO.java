package dao;

import handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataSet.UserDataSet;
import executor.TExecutor;

public class UsersDAO {
	private Connection con;
	public UsersDAO(Connection con){
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
    public UserDataSet get(String login) throws SQLException{
        TExecutor exec = new TExecutor();
        return exec.execQuery(con, "select * from users where login='" + login + "'", new TResultHandler<UserDataSet>(){

            public UserDataSet handle(ResultSet result) throws SQLException {
                result.next();
                return new UserDataSet(result.getLong(1), result.getString(2));
            }

        });
    }

}
