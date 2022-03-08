package ltd.dolink.arch;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

public abstract class ViewState<STATE extends State> implements Observable<STATE> {
    @MainThread
    protected abstract void notify(@NonNull STATE state);
}
