import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class OverridingHashCode {
    private static final Logger LOGGER = Logger.getLogger(OverridingHashCode.class.getName());

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Tests.class);
        for (Failure failure : result.getFailures()) {
            LOGGER.log(Level.WARNING, "Test Fail : {0}", failure.toString());
        }

        LOGGER.log(Level.INFO, "Test Passed : {0}", result.wasSuccessful());
    }

    public static class User {

        final String name;
        final String email;
        final String country;

        public User(String name, String email, String country) {
            this.name = name;
            this.email = email;
            this.country = country;
        }
    }

    public static class UserHash extends User {

        public UserHash(String name, String email, String country) {
            super(name, email, country);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + name.hashCode();
            result = 31 * result + email.hashCode();
            result = 31 * result + country.hashCode();
            return result;
        }
    }

    public static class UserHashAndEquals extends UserHash {

        public UserHashAndEquals(String name, String email, String country) {
            super(name, email, country);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof User)) {
                return false;
            }

            User c = (User) o;

            return name.equals(c.name) && email.equals(c.email) && country.equals(c.country);
        }
    }

    public static class Tests {

        @Test
        public void no_equals_no_hash() throws Exception {
            Set<User> users = new HashSet<User>();
            users.add(new User("a", "b", "c"));
            users.add(new User("a", "b", "c"));
            assertFalse(users.size() == 1);

            users.add(new User("a", "b", "d"));
            users.add(new User("a", "b", "d"));
            assertTrue(users.size() == 4);

            users.add(new UserHash("a", "b", "d"));
            users.add(new UserHash("a", "b", "d"));
            assertTrue(users.size() == 6);

            // Same Hash as UserHash
            users.add(new UserHashAndEquals("a", "b", "d"));
            users.add(new UserHashAndEquals("a", "b", "d"));
            assertTrue(users.size() == 6);
        }

        @Test
        public void no_equals_hash() throws Exception {
            Set<User> users = new HashSet<User>();
            users.add(new UserHash("a", "b", "c"));
            users.add(new UserHash("a", "b", "c"));
            assertFalse(users.size() == 1);

            users.add(new UserHash("a", "b", "d"));
            users.add(new UserHash("a", "b", "d"));
            assertTrue(users.size() == 4);
        }

        @Test
        public void hash_assert() throws Exception {
            Map<User, User> users = new HashMap<User, User>();
            User a = new UserHash("a", "b", "c");
            User b = new UserHash("a", "b", "c");

            users.put(a, a);
            users.put(b, b);

            assertTrue(users.size() == 2);
            assertTrue(users.get(a).equals(a));
            assertTrue(users.get(b).equals(b));
            assertFalse(users.get(a).equals(b));
            assertFalse(users.get(b).equals(a));
        }

        @Test
        public void hash_and_equals() throws Exception {
            Set<User> users = new HashSet<User>();
            users.add(new UserHashAndEquals("a", "b", "c"));
            users.add(new UserHashAndEquals("a", "b", "c"));

            assertTrue(users.size() == 1);
            assertTrue(users.contains(new UserHashAndEquals("a", "b", "c")));
        }
    }
}
