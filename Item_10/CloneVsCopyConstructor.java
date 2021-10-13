import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class CloneVsCopyConstructor {
    private static final Logger LOGGER = Logger.getLogger(CloneVsCopyConstructor.class.getName());

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Tests.class);
        for (Failure failure : result.getFailures()) {
            LOGGER.log(Level.WARNING, "Test Fail : {0}", failure.toString());
        }

        LOGGER.log(Level.INFO, "Test Passed : {0}", result.wasSuccessful());
    }

    public static class User {

        private final String name;
        private final String email;
        private final String country;

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

            return name.equals(c.name) && email.equals(c.email) && country.equals(c.country);
        }
    }

    public static class UserCopyConst extends User {

        public UserCopyConst(User user) {
            super(user.name, user.email, user.country);
        }

        public UserCopyConst copy() {
            return new UserCopyConst(this);
        }
    }

    public static class UserClone extends User implements Cloneable {

        public UserClone(String name, String email, String country) {
            super(name, email, country);
        }

        @Override
        public UserClone clone() {
            UserClone foo;
            try {
                foo = (UserClone) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new Error();
            }

            return foo;
        }
    }

    public static class Tests {

        @Test
        public void copy_constructor() throws Exception {
            User a = new User("a", "b", "c");
            User b = new UserCopyConst(new User("a", "b", "c"));
            User c = new UserCopyConst(b);
            assertTrue(a.equals(b));
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));
        }

        @Test
        public void copy_method() throws Exception {
            User a = new User("a", "b", "c");
            UserCopyConst b = new UserCopyConst(a);
            UserCopyConst c = b.copy();
            assertTrue(a.equals(b));
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));
        }

        @Test
        public void clone_copy() throws Exception {
            UserClone a = new UserClone("a", "b", "c");
            UserClone b = a.clone();
            UserClone c = b.clone();
            assertTrue(a.equals(b));
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));
        }
    }
}
