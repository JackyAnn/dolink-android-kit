package ltd.dolink.arch.reflect;

import java.util.Arrays;
import java.util.Objects;

public class Key {
  final Class<?> invokerClass;
  final Class<?>[] parameterTypes;
  final Class<?> returnType;

  public Key(Class<?> invokerClass, Class<?>[] parameterTypes, Class<?> returnType) {
    Objects.requireNonNull(invokerClass);
    Objects.requireNonNull(parameterTypes);
    Objects.requireNonNull(returnType);
    this.invokerClass = invokerClass;
    this.parameterTypes = parameterTypes;
    this.returnType = returnType;
  }

  public Class<?> getInvokerClass() {
    return invokerClass;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Key key = (Key) o;
    return invokerClass.equals(key.invokerClass)
        && Arrays.equals(parameterTypes, key.parameterTypes)
        && returnType.equals(key.returnType);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(invokerClass, returnType);
    result = 31 * result + Arrays.hashCode(parameterTypes);
    return result;
  }

  @Override
  public String toString() {
    String sb =
        "Key{"
            + "invokerClass="
            + invokerClass
            + ", parameterTypes="
            + Arrays.toString(parameterTypes)
            + ", returnType="
            + returnType
            + '}';
    return sb;
  }
}
