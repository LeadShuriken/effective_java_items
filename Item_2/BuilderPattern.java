import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class BuilderPattern {
    public enum Feeling {
        BAD, GOOD, SOSO, MELANCHOLIC
    }

    public enum ToDo {
        MEET, PLAN, FEED
    }

    public enum ThinkOf {
        WORLD, STARS, FOOD, BULL
    }

    public static abstract class Printable {
        abstract String print();

        @Override
        public String toString() {
            return print();
        }
    }

    public static void main(String[] args) {
        new Chieftain.Builder(ToDo.FEED).addMood(Feeling.BAD).addMood(Feeling.MELANCHOLIC).build();

        new SittingBull.Builder(ThinkOf.STARS).addMood(Feeling.SOSO).addMood(Feeling.MELANCHOLIC).build();
    }

    public static abstract class User extends Printable {

        static final Logger LOGGER = Logger.getLogger(User.class.getName());

        final Set<Feeling> roles;

        public User(Builder<?> builder) {
            roles = builder.roles.clone();
        }

        abstract static class Builder<T extends Builder<T>> {
            EnumSet<Feeling> roles = EnumSet.noneOf(Feeling.class);

            public T addMood(Feeling role) {
                roles.add(Objects.requireNonNull(role));
                return self();
            }

            public String print() {
                return "User [roles=" + roles + "]";
            }

            abstract User build();

            protected abstract T self();
        }
    }

    public static class Chieftain extends User {

        private final ToDo todo;

        public static class Builder extends User.Builder<Builder> {

            private final ToDo todo;

            public Builder(ToDo todo) {
                this.todo = Objects.requireNonNull(todo);
            }

            @Override
            public Chieftain build() {
                Chieftain chieftain = new Chieftain(this);
                LOGGER.log(Level.INFO, "Creating an {0} that is a {1} instance at : {2}",
                        new Object[] { this.print(), chieftain.print(), LocalTime.now() });
                return chieftain;
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private Chieftain(Builder builder) {
            super(builder);
            todo = builder.todo;
        }

        @Override
        String print() {
            return "Chieftain [todo=" + todo + "]";
        }
    }

    public static class SittingBull extends User {

        private final ThinkOf toThink;

        public static class Builder extends User.Builder<Builder> {

            private final ThinkOf toThink;

            public Builder(ThinkOf toThink) {
                this.toThink = Objects.requireNonNull(toThink);
            }

            @Override
            public SittingBull build() {
                return new SittingBull(this);
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private SittingBull(Builder builder) {
            super(builder);
            toThink = builder.toThink;
        }

        @Override
        public String print() {
            return "SittingBull [toThink=" + toThink + "]";
        }
    }
}
