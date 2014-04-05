package DatabaseService;

import MessageSystem.Address;
import MessageSystem.MessageSystem;
import MessageSystem.Messages.MsgSererIsDown;
import MessageSystem.Messages.MsgUpdateUserid;
import MessageSystem.Subscriber;
import Utilities.TimeHelper;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.net.ConnectException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by gumo on 03/04/14.
 */
public class DatabaseService implements Runnable, Subscriber {
    private Address address;
    private MessageSystem ms;
    public DatabaseService(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setDatabaseService(address);
    }


    public Long getUserId(String login, String password) throws SQLException {
        try {
            Connection con = getConnection();  // add to dbservconst
            TimeHelper.sleep(5000);
            UserDAO dao = new UserDAO(con);

            if (dao.isUserExists(con, login, password)) {
                UserDataSet user = dao.getByName(login);
                return user.getId();
            }
        } catch (SQLException | SocketException e) {
            return (long) 0;
        }
        return (long) -1;
    }

    public Integer doRegistration(String login, String password) {
        try {
            Connection con = getConnection();
            UserDAO dao = new UserDAO(con);
                if (!dao.isUserExists(con, login)) {
                    UserDataSet user = new UserDataSet(login, password);
                    dao.add(user);
                    con.close();
                    return 1;
                } else {con.close();}

            } catch (SocketException | SQLException e) {
               return 0;
            }
        return -1;
    }

    public MessageSystem getMessageSystem(){
        return ms;
    }

    public void run(){
        while(true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(100);
        }
    }

    public Address getAddress(){
        return this.address;
    }

    public static Connection getConnection() throws SQLException{
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            String url = "jdbc:mysql://127.0.0.1:3306/java_test?user=user&password=user";
            return DriverManager.getConnection(url);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
