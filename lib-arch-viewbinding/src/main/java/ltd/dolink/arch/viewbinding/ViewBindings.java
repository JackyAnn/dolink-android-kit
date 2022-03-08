package ltd.dolink.arch.viewbinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import ltd.dolink.arch.reflect.Invocation;
import ltd.dolink.arch.reflect.Reflects;


public class ViewBindings {

    private static <VB extends ViewBinding> VB invoke(@NonNull Class<VB> viewbindingClass, @NonNull Class<?>[] parameterTypes, Object obj, @NonNull Object[] args) {
        Invocation invocation = Reflects.findInvocation(viewbindingClass, parameterTypes, viewbindingClass);
        try {
            @SuppressWarnings("unchecked") VB viewBinding = (VB) invocation.invoke(obj, args);
            return viewBinding;
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Can not invoke %s for %s", invocation, viewbindingClass), e.getCause());
        }
    }


    public static <VB extends ViewBinding> VB inflate(@NonNull Class<VB> viewbindingClass, @NonNull LayoutInflater inflater) {
        return invoke(viewbindingClass, new Class<?>[]{LayoutInflater.class}, null, new Object[]{inflater});
    }


    public static <VB extends ViewBinding> VB inflate(@NonNull Class<VB> viewbindingClass, @NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        return invoke(viewbindingClass, new Class<?>[]{LayoutInflater.class, ViewGroup.class, boolean.class}, null, new Object[]{inflater, parent, attachToParent});
    }


    public static <VB extends ViewBinding> VB bind(@NonNull Class<VB> viewbindingClass, @NonNull View rootView) {
        return invoke(viewbindingClass, new Class<?>[]{View.class}, null, new Object[]{rootView});
    }


    public static <VB extends ViewBinding> Class<VB> getViewBindingType(@NonNull Class<?> clazzType) {
        Type type = clazzType.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            for (Type actualType : actualTypes) {
                if (actualType instanceof Class) {
                    if (ViewBinding.class.isAssignableFrom((Class<VB>) actualType)) {
                        return (Class<VB>) actualType;
                    }
                }

            }
        }
        throw new RuntimeException("Can not find view binding.");
    }

}



