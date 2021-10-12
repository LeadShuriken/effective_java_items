import static org.junit.Assert.*;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class OverridingEquals {
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Tests.class);
        for (Failure failure : result.getFailures()) {
            LOGGER.log(Level.WARNING, "Test Fail : {0}", failure.toString());
        }

        LOGGER.log(Level.INFO, "Test Passed : {0}", result.wasSuccessful());
    }

    public static class User {

        protected final String name;
        protected final String email;
        protected final String country;

        public User(String name, String email, String country) {
            this.name = name;
            this.email = email;
            this.country = country;
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

            return name.equals(c.name) && email.equals(c.email) && country.equals(c.country)
                    && country.equals(c.country);
        }
    }

    public static class Client extends User {
        private final String account;

        public Client(String name, String email, String country, String account) {
            super(name, email, country);
            this.account = account;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Client)) {
                return false;
            }

            Client c = (Client) o;

            return name.equals(c.name) && email.equals(c.email) && country.equals(c.country)
                    && account.equals(c.account);
        }
    }

    public static class Admin {
        private final String pass;
        private final User user;

        public Admin(User user, String pass) {
            this.user = Objects.requireNonNull(user);
            this.pass = pass;
        }

        public User getUser() {
            return user;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Admin)) {
                return false;
            }

            Admin c = (Admin) o;

            return pass.equals(c.pass) && user.equals(c.user);
        }
    }

    public static class Tests {

        @Test
        public void equals_is_reflexive() throws Exception {
            User user = new User("a", "b", "c");
            assertTrue(user.equals(user));
        }

        @Test
        public void equals_is_consistent() throws Exception {
            User user1 = new User("a", "b", "c");
            User user2 = new User("a", "b", "c");
            assertTrue(user1.equals(user2));
            assertTrue(user1.equals(user2));
        }

        @Test
        public void equals_with_null_argument_returns_false() throws Exception {
            User user = new User("a", "b", "c");
            assertFalse(user.equals(null));
        }

        @Test
        public void propar_equals_handling() {
            assertEquals(new User("a", "b", "c").equals(new User("a", "b", "c")), true);
        }

        @Test
        public void equals_is_symmetric() throws Exception {
            User user1 = new User("a", "b", "c");
            User user2 = new User("a", "b", "c");
            assertTrue(user1.equals(user2));
            assertTrue(user2.equals(user1));
        }

        @Test
        public void equals_is_transive() throws Exception {
            User user1 = new User("a", "b", "c");
            User user2 = new User("a", "b", "c");
            User user3 = new User("a", "b", "c");
            assertTrue(user1.equals(user2));
            assertTrue(user2.equals(user3));
            assertTrue(user1.equals(user3));
        }

        @Test
        public void equals_not_inherited_with_consistancy() {
            User user1 = new User("a", "b", "c");
            Client user2 = new Client("a", "b", "c", "d");
            assertTrue(user1.equals(user2));
            assertFalse(user2.equals(user1));
        }

        @Test
        public void equals_extended_with_composition_view() {
            User user1 = new User("a", "b", "c");
            Admin user2 = new Admin(user1, "d");
            assertTrue(user2.getUser().equals(user1));
            assertTrue(user1.equals(user2.getUser()));
        }
    }
}
