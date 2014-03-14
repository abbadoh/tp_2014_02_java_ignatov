package test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

import javax.persistence.Entity;
import java.sql.SQLException;

/**
 * Created by gumo on 13/03/14.
 */
public class sampleTest {
    int a;
    @Before
    public  void setA(){
        a = 100;
    }
//    @After
//    @Rule

    @Test
    public void gayTest() throws Exception {
    Assert.assertEquals(a,100);
    }

    @Test
    public void naturalTest() throws Exception {
        Assert.assertEquals(a,100);
    }

}
