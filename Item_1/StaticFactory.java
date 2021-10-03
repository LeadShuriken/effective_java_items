import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

class StaticFactory {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public static void main(String[] args) {
        valueOf(true);

        User.create("name", "email", "country");
        User.createWithDefaultCountry("name", "email");
        User.createWithLoggedInstantiationTime("name", "email", "country");
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
}