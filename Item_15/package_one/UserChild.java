package package_one;

import java.util.logging.Level;

import package_two.User;

public class UserChild extends User {

    protected String getName() {
        MinimizeAccess.LOGGER.log(Level.INFO, "Protected Field Another Package...");
        return this.name;
    }

    protected String getCountry() {
        return this.country;
    }

    protected String getEmail() {
        return this.email;
    }

    public UserChild(String name, String email, String country) {
        super(name, email, country);
    }
}
