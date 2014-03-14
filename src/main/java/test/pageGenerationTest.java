package test;

import junit.framework.Assert;
import org.junit.Test;
import templater.PageGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gumo on 13/03/14.
 */
public class pageGenerationTest {
    Map<String, Object> pageVariables = new HashMap<>();

    @Test
    public void successAuthformTest() throws Exception {
        pageVariables.put("login", "login");
        Assert.assertNotNull(PageGenerator.getPage("authform.tml", pageVariables));
        Assert.assertFalse(PageGenerator.getPage("authform.tml", pageVariables).equals(""));
    }

    @Test
    public void unsuccessAuthformTest() throws Exception {
        pageVariables.put("alert", "Wrong username or password");
        Assert.assertNotNull(PageGenerator.getPage("authform.tml", pageVariables));
        Assert.assertFalse(PageGenerator.getPage("authform.tml", pageVariables).equals(""));
    }
}
