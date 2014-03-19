/**
 * Created by gumo on 19/03/14.
 */

import DatabaseService.AuthTest;
import DatabaseService.RegistrationTest;
import frontend.PageGenerationTest;
import frontend.RoutingTest;
import FunctionalTests.SeleniumTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {
        public static void main(String[] args) {
            Result result = JUnitCore.runClasses(AuthTest.class, RegistrationTest.class, PageGenerationTest.class, RoutingTest.class, SeleniumTest.class);
            if (result.wasSuccessful()) {
                System.out.println("All tests passed successful");
            } else {
                for (Failure failure : result.getFailures()) {
                    System.out.println("Failed test:" + failure.toString());
                }
            }
        }
}
