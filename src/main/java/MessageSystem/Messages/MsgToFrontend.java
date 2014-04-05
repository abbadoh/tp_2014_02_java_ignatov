package MessageSystem.Messages;

import MessageSystem.Address;
import MessageSystem.Subscriber;
import frontend.Frontend;

import java.io.IOException;

/**
 * Created by gumo on 04/04/14.
 */
public abstract class MsgToFrontend extends Msg {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) throws IOException {
        if(subscriber instanceof Frontend){
            exec((Frontend)subscriber);
        }
    }

    abstract void exec(Frontend frontend) throws IOException;
}
