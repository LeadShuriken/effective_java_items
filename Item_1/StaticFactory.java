import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static org.junit.Assert.assertEquals;

class StaticFactory {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(Tests.class);
        for (Failure failure : result.getFailures()) {
            LOGGER.log(Level.WARNING, "Test Fail : {0}", failure.toString());
        }

        LOGGER.log(Level.INFO, "Test Passed : {0}", result.wasSuccessful());
    }

    public static Boolean valueOf(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    @SuppressWarnings("unused")
    public static class User {

        private final String name;
        private final String email;
        private final String country;

        private User(String name, String email, String country) {
            this.name = name;
            this.email = email;
            this.country = country;
        }

        public static User create(String name, String email, String country) {
            return new User(name, email, country);
        }

        public static User createWithDefaultCountry(String name, String email) {
            return new User(name, email, "Argentina");
        }

        public static User createWithLoggedInstantiationTime(String name, String email, String country) {
            LOGGER.log(Level.INFO, "Creating User instance at : {0}", LocalTime.now());
            return new User(name, email, country);
        }
    }

    public static class Tests {

        @Test
        public void valueOfStaticTrue() {
            Boolean a = valueOf(true);
            assertEquals(a, Boolean.TRUE);
        }

        @Test
        public void valueOfStaticFalse() {
            Boolean a = valueOf(false);
            assertEquals(a, Boolean.FALSE);
        }

        @Test
        public void UserDefault() {
            User a = User.create("name", "email", "country");
            assertEquals(a.name, "name");
            assertEquals(a.email, "email");
            assertEquals(a.country, "country");
        }

        @Test
        public void UserDefaultCountry() {
            User a = User.createWithDefaultCountry("name", "email");
            assertEquals(a.name, "name");
            assertEquals(a.email, "email");
            assertEquals(a.country, "Argentina");
        }
    }
}