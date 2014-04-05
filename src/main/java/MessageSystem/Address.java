package MessageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gumo on 03/04/14.
 */
public class Address {
    static private AtomicInteger subscriberIdCreator = new AtomicInteger();
    final private int subscriberId;

    public Address(){
        this.subscriberId = subscriberIdCreator.incrementAndGet();
    }

    public int hashCode(){
        return subscriberId;
    }
}

