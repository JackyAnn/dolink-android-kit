package ltd.dolink.arch;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.io.Serializable;

public interface Observable<T> extends Serializable {
    @MainThread
    void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<T> observer);
}
