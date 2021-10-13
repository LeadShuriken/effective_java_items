package package_one;

import java.util.logging.Level;

public class User {
    public final String name;
    final String email;
    final String country;

    public User(String name, String email, String country) {
        this.name = new package_one.UserChild(name, email, country).getName();
        MinimizeAccess.LOGGER.log(Level.INFO, "Protected Field Same Package...");

        this.country = new package_two.UserChild(name, email, country).getCountry();

        this.email = new PrivateUser(name, email, country).email;
        MinimizeAccess.LOGGER.log(Level.INFO, "Protected Field On Parent...");
    }
}
