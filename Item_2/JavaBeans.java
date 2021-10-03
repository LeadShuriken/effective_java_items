public class JavaBeans {

    public static void main(String[] args) {
        User user = new User();
        user.setName("name");
        user.setName("email");
        user.setCountry("country");
    }

    @SuppressWarnings("unused")
    public static class User {

        private String name;
        private String email;
        private String country;

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
