package package_one;

@SuppressWarnings("unused")
class PrivateUser {
    private final String name;
    protected final String email;
    private final String country;

    public PrivateUser(String name, String email, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
    }
}
