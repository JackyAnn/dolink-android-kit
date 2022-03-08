package ltd.dolink.arch.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Reflects {
    private static final Map<Key, Invocation> CACHE = new HashMap<>();

    public static Invocation findInvocation(Class<?> invokerClass, Class<?>[] parameterTypes, Class<?> returnClass) {
        return CACHE.computeIfAbsent(new Key(invokerClass, parameterTypes, returnClass), key -> {
            Method[] declaredMethods = invokerClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                Class<?>[] methodParameterTypes = declaredMethod.getParameterTypes();
                Class<?> methodReturnType = declaredMethod.getReturnType();
                if (Objects.equals(methodReturnType, returnClass) && Arrays.equals(methodParameterTypes, parameterTypes)) {
                    return new MethodInvocation(declaredMethod);
                }
            }
            throw new RuntimeException(String.format("Can not find matched method for %s", key));
        });


    }
}
