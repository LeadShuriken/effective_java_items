import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.reflect.Constructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EnumSingleton {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Serailize to file
        User instanceOne = User.INSTANCE;
        instanceOne.setName("name_1").setEmail("email_1").setCountry("country_1");
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

        // Not Reflectable
        instanceOne = User.INSTANCE;
        instanceTwo = null;

        try {
            Constructor<?> constructor = User.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            instanceTwo = (User) constructor.newInstance("name_2", "email_2", "country_2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static enum User {
        INSTANCE;

        private String name;
        private String email;
        private String country;

        public User setName(String name) {
            this.name = name;
            return this;
        }

        public User setEmail(String email) {
            this.email = email;
            return this;
        }

        public User setCountry(String country) {
            this.country = country;
            return this;
        }

        @Override
        public String toString() {
            return "User [country=" + country + ", email=" + email + ", name=" + name + "]";
        }
    }
}
