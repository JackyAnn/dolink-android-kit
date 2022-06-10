package ltd.dolink.arch.initializer;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public interface ApplicationInitializer {
  @NonNull
  Class<? extends Initializer<?>> initializer();
}
