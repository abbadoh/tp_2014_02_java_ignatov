package MessageSystem.Messages;

import DatabaseService.DatabaseService;
import MessageSystem.Address;
import java.sql.SQLException;

/**
 * Created by gumo on 04/04/14.
 */
public class MsgRegUser extends MsgToDBS {
    private String password;
    private String login;
    private String sessionId;

    public MsgRegUser(Address from, Address to, String login, String password, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public void exec(DatabaseService databaseService) throws SQLException {
        Integer success  = databaseService.doRegistration(login, password);
        databaseService.getMessageSystem().sendMessage(new MsgRegComplete(getTo(), getFrom(), sessionId, success));
    }
}
