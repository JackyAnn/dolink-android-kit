package ltd.dolink.arch.initializer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.Objects;

public class ApplicationOwner implements LifecycleOwner, ViewModelStoreOwner {
    private static ApplicationOwner applicationOwner;
    private final Application application;
    private final ViewModelStore viewModelStore = new ViewModelStore();
    private ViewModelProvider.Factory factory;
    private ViewModelProvider viewModelProvider;


    public ApplicationOwner(Application application) {
        this.application = application;
        applicationOwner = this;
    }

    @NonNull
    public static ApplicationOwner getInstance() {
        return applicationOwner;
    }

    @NonNull
    public Application getApplication() {
        return application;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @NonNull
    public ViewModelProvider of() {
        if (Objects.isNull(viewModelProvider)) {
            viewModelProvider = new ViewModelProvider(this, getViewModelFactory());
        }
        return viewModelProvider;
    }

    public ViewModelProvider.Factory getViewModelFactory() {
        if (Objects.isNull(factory)) {
            factory = AndroidViewModelFactory.getInstance(application);
        }
        return factory;
    }

    public void setViewModelFactory(@NonNull ViewModelProvider.Factory viewModelFactory) {
        Objects.requireNonNull(viewModelFactory);
        this.factory = viewModelFactory;
    }


    @NonNull
    public ViewModelProvider of(@NonNull ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner);
    }


    @NonNull
    public ViewModelProvider of(@NonNull ViewModelStoreOwner owner, @NonNull ViewModelProvider.Factory factory) {
        return new ViewModelProvider(owner, factory);
    }


    @NonNull
    public ViewModelProvider of(@NonNull ViewModelStore store, @NonNull ViewModelProvider.Factory factory) {
        return new ViewModelProvider(store, factory);
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return ProcessLifecycleOwner.get().getLifecycle();
    }
}

