package ltd.dolink.arch;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

public abstract class ViewIntent<INTENT extends Intent> implements Observable<INTENT> {
    @MainThread
    public abstract void notify(@NonNull INTENT intent);
}
