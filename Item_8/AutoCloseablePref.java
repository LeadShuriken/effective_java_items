import java.lang.ref.Cleaner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoCloseablePref {
    private enum CurrentState {
        LOGGED, UNLOGGED, DELETED
    }

    private static final Logger LOGGER = Logger.getLogger(AutoCloseablePref.class.getName());

    public static void main(String[] args) {
        try (User a = new User(CurrentState.LOGGED)) {
            // pass
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class User implements AutoCloseable {

        private static final Cleaner cleaner = Cleaner.create();

        @SuppressWarnings("unused")
        private static class State implements Runnable {
            CurrentState state;

            State(CurrentState state) {
                LOGGER.log(Level.INFO, "Moving User to: {0}", state.toString());
                this.state = state;
            }

            @Override
            public void run() {
                LOGGER.log(Level.INFO, "Returning User to: {0}", CurrentState.UNLOGGED.toString());
                state = CurrentState.UNLOGGED;
            }

        }

        private final State state;
        private final Cleaner.Cleanable cleanable;

        @Override
        public void close() {
            cleanable.clean();
        }

        public User(CurrentState initState) {
            state = new State(initState);
            cleanable = cleaner.register(this, state);
        }
    }
}
