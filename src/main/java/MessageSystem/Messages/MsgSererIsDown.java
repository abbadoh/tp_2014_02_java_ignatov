package MessageSystem.Messages;

import MessageSystem.Address;
import frontend.Frontend;

import java.io.IOException;

/**
 * Created by gumo on 05/04/14.
 */
public class MsgSererIsDown extends MsgToFrontend{

    public MsgSererIsDown(Address from, Address to) {
        super(from, to);
    }

    void exec(Frontend frontend) throws IOException {
        System.out.println("DOWN");
//        frontend.serverIsDown();
    }
}
