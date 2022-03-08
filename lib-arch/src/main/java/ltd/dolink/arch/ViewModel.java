package ltd.dolink.arch;

import androidx.annotation.NonNull;

public interface ViewModel<STATE extends State> {
    @NonNull
    ViewState<STATE> viewState();
}
