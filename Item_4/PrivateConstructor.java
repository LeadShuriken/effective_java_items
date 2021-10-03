import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.reflect.Constructor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class PrivateConstructor {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        // Serailize to file
        Class<User> instanceOne = User.class;
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("user.data"));
        out.writeObject(instanceOne);
        out.close();

        // Deserailize from file to object
        ObjectInput in = new ObjectInputStream(new DeleteOnCloseFileInputStream("user.data"));
        Class<User> instanceTwo = (Class<User>) in.readObject();
        in.close();

        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceOne, instanceOne.hashCode() });
        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceTwo, instanceTwo.hashCode() });

        // Destroyed by reflection
        instanceOne = User.class;
        instanceTwo = null;

        try {
            Constructor<?>[] constructors = User.class.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                // Below code will destroy the singleton pattern
                constructor.setAccessible(true);
                instanceTwo = (Class<User>) constructor.newInstance();
                break;
            }
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
