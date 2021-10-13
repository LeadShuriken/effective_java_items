package package_one;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MinimizeAccess {

    public static final Logger LOGGER = Logger.getLogger(MinimizeAccess.class.getName());

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Package Private Class ...");
        new PrivateUser("a", "b", "c");
        new User("a", "b", "c");
    }
}
