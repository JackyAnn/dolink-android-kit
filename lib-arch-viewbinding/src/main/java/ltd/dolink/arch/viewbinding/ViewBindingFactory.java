package ltd.dolink.arch.viewbinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.util.Objects;

public interface ViewBindingFactory<VB extends ViewBinding> {
    static <VB extends ViewBinding> ViewBindingFactory<VB> of(@NonNull Class<VB> viewBindingClass) {
        return new DefaultViewBindingFactory<>(viewBindingClass);
    }

    @NonNull
    Class<VB> getViewBindingClass();

    @NonNull
    VB inflate(@NonNull LayoutInflater inflater);

    @NonNull
    VB inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent);

    @NonNull
    VB bind(@NonNull View rootView);

}

class DefaultViewBindingFactory<VB extends ViewBinding> implements ViewBindingFactory<VB> {
    @NonNull
    private final Class<VB> viewBindingClass;

    public DefaultViewBindingFactory(@NonNull Class<VB> viewBindingClass) {
        Objects.requireNonNull(viewBindingClass);
        this.viewBindingClass = viewBindingClass;
    }

    @NonNull
    @Override
    public Class<VB> getViewBindingClass() {
        return viewBindingClass;
    }

    @NonNull
    @Override
    public VB inflate(@NonNull LayoutInflater inflater) {
        return ViewBindings.inflate(getViewBindingClass(), inflater);
    }

    @NonNull
    @Override
    public VB inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        return ViewBindings.inflate(getViewBindingClass(), inflater, parent, attachToParent);
    }

    @NonNull
    @Override
    public VB bind(@NonNull View rootView) {
        return ViewBindings.bind(getViewBindingClass(), rootView);
    }
}
