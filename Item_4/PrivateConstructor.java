import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.reflect.Constructor;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class PrivateConstructor {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Serailize to file
        Class<User> instanceOne = User.class;
        Class<User> instanceTwo = null;
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("user.data"));) {
            out.writeObject(instanceOne);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Deserailize from file to object
        try (ObjectInput in = new ObjectInputStream(new DeleteOnCloseFileInputStream("user.data"));) {
            instanceTwo = (Class<User>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceOne, instanceOne.hashCode() });
        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceTwo, instanceTwo.hashCode() });

        // Destroyed by reflection
        instanceOne = User.class;
        instanceTwo = null;

        try {
            Constructor<?> constructor = User.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            instanceTwo = (Class<User>) constructor.newInstance();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error: {0}", e.getCause().getMessage());
        }

        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceOne, instanceOne.hashCode() });
        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { User.print(), instanceOne.hashCode() });
    }

    public static class User {

        private final static String name = "Static Name";
        private final static String email = "Static Email";
        private final static String country = "Static Country";

        private User() {
            throw new UnsupportedOperationException("Don't do this!");
        }

        private static String print() {
            return "User [country=" + country + ", email=" + email + ", name=" + name + "]";
        }
    }
}
