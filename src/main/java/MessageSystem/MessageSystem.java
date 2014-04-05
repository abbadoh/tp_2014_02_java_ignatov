package MessageSystem;

import MessageSystem.Messages.Msg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by gumo on 03/04/14.
 */
public class MessageSystem {
    private Map<Address, ConcurrentLinkedQueue<Msg>> messages = new HashMap<>();
    private AddressService addressService = new AddressService();

    public void addService(Subscriber subscriber){
        messages.put(subscriber.getAddress(), new ConcurrentLinkedQueue<Msg>());
        Queue<Msg> messageQueue = messages.get(subscriber.getAddress());
        }

    public void sendMessage(Msg message){
        Queue<Msg> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);
    }

    public void execForAbonent(Subscriber subscriber) {
        Queue<Msg> messageQueue = messages.get(subscriber.getAddress());
        if(messageQueue == null){
            return;
        }
        while(!messageQueue.isEmpty()){
            Msg message = messageQueue.poll();
            try {
                message.exec(subscriber);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AddressService getAddressService(){
        return addressService;
    }

}
