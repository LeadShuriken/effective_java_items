import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Telescoping {

    private static final Logger LOGGER = Logger.getLogger(Telescoping.class.getName());

    public static void main(String[] args) {
        new User("name");
    }

    @SuppressWarnings("unused")
    public static class User {

        private final String name;
        private final String email;
        private final String country;

        public User(String name) {
            this(name, "email");
        }

        public User(String name, String email) {
            this(name, email, "country");
        }

        public User(String name, String email, String country) {
            LOGGER.log(Level.INFO, "Creating User instance at : {0}", LocalTime.now());
            this.name = name;
            this.email = email;
            this.country = country;
        }
    }
}
