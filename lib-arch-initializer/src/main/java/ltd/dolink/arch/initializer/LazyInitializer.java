package ltd.dolink.arch.initializer;

public abstract class LazyInitializer<T> extends Initializer<T> {

    private static final Object NO_INIT = new Object();
    private final boolean isSynchronized;
    @SuppressWarnings("unchecked")
    protected T object = (T) NO_INIT;

    public LazyInitializer() {
        this(false);
    }

    public LazyInitializer(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    protected boolean isSynchronized() {
        return isSynchronized;
    }

    @Override
    public T get() {
        if (isSynchronized) {
            synchronized (this) {
                if (object == NO_INIT) {
                    object = initialize();
                }
            }
            return object;
        }
        if (object == NO_INIT) {
            object = initialize();
        }
        return object;
    }
}
