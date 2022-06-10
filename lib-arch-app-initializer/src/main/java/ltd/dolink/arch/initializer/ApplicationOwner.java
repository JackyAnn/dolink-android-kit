package ltd.dolink.arch.initializer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class ApplicationOwner implements LifecycleOwner, ViewModelStoreOwner {
  private static ApplicationOwner applicationOwner;
  private final Application application;
  private final ViewModelStore viewModelStore = new ViewModelStore();

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
  @Override
  public Lifecycle getLifecycle() {
    return ProcessLifecycleOwner.get().getLifecycle();
  }
}
