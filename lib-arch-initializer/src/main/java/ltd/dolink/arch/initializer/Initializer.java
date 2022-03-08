package ltd.dolink.arch.initializer;

public abstract class Initializer<T> {
    protected abstract T initialize();

    public abstract T get();

}
