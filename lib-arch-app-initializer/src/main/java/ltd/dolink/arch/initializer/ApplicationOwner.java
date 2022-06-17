package ltd.dolink.arch.initializer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.Factory;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class ApplicationOwner implements LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
  private static ApplicationOwner applicationOwner;
  private final Application application;
  private final ViewModelStore viewModelStore;
  private final ViewModelProvider.Factory factory;
  public ApplicationOwner(Application application) {
    this.application = application;
    applicationOwner = this;
    viewModelStore = new ViewModelStore();
    factory = (Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(application);
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
  @Override
  public Lifecycle getLifecycle() {
    return ProcessLifecycleOwner.get().getLifecycle();
  }

  @NonNull
  @Override
  public Factory getDefaultViewModelProviderFactory() {
    return factory;
  }
}
