import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class StaticFactorySingleton {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Serailize to file
        User instanceOne = User.getInstance("name_1", "email_1", "country_1");
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("user.data"));
        out.writeObject(instanceOne);
        out.close();

        // Deserailize from file to object
        ObjectInput in = new ObjectInputStream(new DeleteOnCloseFileInputStream("user.data"));
        User instanceTwo = (User) in.readObject();
        in.close();

        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceOne, instanceOne.hashCode() });
        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceTwo, instanceTwo.hashCode() });

        // Destroyed by reflection
        instanceOne = User.getInstance();
        instanceTwo = null;
        try {
            Constructor<?>[] constructors = User.class.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                // Below code will destroy the singleton pattern
                constructor.setAccessible(true);
                instanceTwo = (User) constructor.newInstance("name_2", "email_2", "country_2");
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceOne, instanceOne.hashCode() });
        LOGGER.log(Level.INFO, "Created an {0} instance with hash : {1}",
                new Object[] { instanceTwo, instanceTwo.hashCode() });

    }

    public static final class User implements Serializable {

        private static final long serialVersionUID = -7604766932017737115L;

        private final String name;
        private final String email;
        private final String country;

        private User(String name, String email, String country) {
            this.name = name;
            this.email = email;
            this.country = country;
        }

        @Override
        public String toString() {
            return "User [country=" + country + ", email=" + email + ", name=" + name + "]";
        }

        private static User instance;

        public static User getInstance() {
            if (instance == null) {
                throw new IllegalArgumentException();
            }
            return instance;
        }

        public static User getInstance(String name, String email, String country) {
            if (instance == null) {
                synchronized (User.class) {
                    instance = new User(name, email, country);
                }
            }
            return instance;
        }

        private Object readResolve() {
            return instance;
        }
    }
}
