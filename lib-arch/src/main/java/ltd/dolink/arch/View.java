package ltd.dolink.arch;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.Objects;

public interface View<STATE extends State> {

    default void setViewModel(@NonNull ViewModel<STATE> viewModel) {

        ViewState<STATE> viewState = viewModel.viewState();

        LifecycleOwner lifecycleOwner = getLifecycleOwner();
        Objects.requireNonNull(lifecycleOwner);

        viewState.observe(lifecycleOwner, View.this::setState);
    }

    void setState(@NonNull STATE state);

    @NonNull
    LifecycleOwner getLifecycleOwner();
}
