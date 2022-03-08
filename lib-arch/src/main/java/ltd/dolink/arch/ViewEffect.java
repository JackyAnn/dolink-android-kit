package ltd.dolink.arch;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

public abstract class ViewEffect<EFFECT extends Effect> implements Observable<EFFECT> {
    @MainThread
    protected abstract void notify(@NonNull EFFECT effect);
}
