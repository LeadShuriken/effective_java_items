package package_two;

public class UserChild extends User {

    protected String getName() {
        return this.name;
    }

    public String getCountry() {
        return this.country;
    }

    protected String getEmail() {
        return this.email;
    }

    public UserChild(String name, String email, String country) {
        super(name, email, country);
    }
}
