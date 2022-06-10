package ltd.dolink.arch.reflect;

import java.lang.reflect.Method;
import java.util.Objects;

public class MethodInvocation implements Invocation {
  private final Method method;

  public MethodInvocation(Method method) {
    Objects.requireNonNull(method);
    this.method = method;
  }

  public Method getMethod() {
    return method;
  }

  @Override
  public Object invoke(Object obj, Object[] args) throws Throwable {
    return method.invoke(obj, args);
  }
}
