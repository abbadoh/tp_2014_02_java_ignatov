package MessageSystem.Messages;

import MessageSystem.Address;
import frontend.Frontend;

import java.io.IOException;


/**
 * Created by gumo on 04/04/14.
 */
public class MsgUpdateUserid extends MsgToFrontend {
    private String sessionId;
    private Long id;

    public MsgUpdateUserid(Address from, Address to, Long id, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
        this.id = id;
    }

    void exec(Frontend frontend) throws IOException {
        
        if(id == 0) { frontend.serverIsDown(sessionId); }
        if(id != -1) { frontend.setId(sessionId, id); }
        else {frontend.doLogout(sessionId);}
    }
}
