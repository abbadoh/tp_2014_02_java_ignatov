package MessageSystem.Messages;

import MessageSystem.Subscriber;
import MessageSystem.Address;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by gumo on 03/04/14.
 */
public abstract class Msg {
    private Address from;
    private Address to;

    public Msg(Address from, Address to){
        this.from = from;
        this.to = to;
    }

    public Address getFrom(){
        return from;
    }

    public Address getTo(){
        return to;
    }

    public abstract void exec(Subscriber subscriber) throws SQLException, IOException;

}
