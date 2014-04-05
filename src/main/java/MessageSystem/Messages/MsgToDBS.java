package MessageSystem.Messages;

import DatabaseService.DatabaseService;
import MessageSystem.Address;
import MessageSystem.Subscriber;

import java.sql.SQLException;

/**
 * Created by gumo on 04/04/14.
 */
public abstract class MsgToDBS extends Msg {
    public MsgToDBS(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) throws SQLException {
        if(subscriber instanceof DatabaseService){
            exec((DatabaseService) subscriber);
        }
    }

    public abstract void exec(DatabaseService accountService) throws SQLException;

}
