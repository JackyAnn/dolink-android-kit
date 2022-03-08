package ltd.dolink.arch.reflect;

public interface Invocation {
    Object invoke(Object obj, Object[] args) throws Throwable;
}
