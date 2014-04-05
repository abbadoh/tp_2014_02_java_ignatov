package MessageSystem.Messages;

import DatabaseService.DatabaseService;
import MessageSystem.Address;

import java.sql.SQLException;

/**
 * Created by gumo on 04/04/14.
 */
public class MsgGetUserid extends MsgToDBS {
    private String login;
    private String password;
    private String sessionId;

    public MsgGetUserid(Address from, Address to, String login, String password, String sessionId) {
        super(from, to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }

    public void exec(DatabaseService databaseService) throws SQLException {
        Long id = databaseService.getUserId(login, password);
        databaseService.getMessageSystem().sendMessage(new MsgUpdateUserid(getTo(), getFrom(), id, sessionId));
    }
}
