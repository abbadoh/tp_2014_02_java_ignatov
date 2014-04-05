package MessageSystem.Messages;

import MessageSystem.Address;
import frontend.Frontend;

import java.io.IOException;

/**
 * Created by gumo on 04/04/14.
 */
public class MsgRegComplete extends MsgToFrontend {
    private String sessionId;
    private Integer state;

    public MsgRegComplete(Address from, Address to, String sessionId, Integer state) {
        super(from, to);
        this.state = state;
        this.sessionId = sessionId;
    }

    void exec(Frontend frontend) throws IOException {
        if(state == 0) { frontend.serverIsDown(sessionId); }
        else {frontend.regComplete(sessionId,state);}
    }
}
