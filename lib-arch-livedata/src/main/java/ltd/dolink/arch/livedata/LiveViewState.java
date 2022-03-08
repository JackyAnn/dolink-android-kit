package ltd.dolink.arch.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import ltd.dolink.arch.Observer;
import ltd.dolink.arch.State;
import ltd.dolink.arch.ViewState;

public class LiveViewState<STATE extends State> extends ViewState<STATE> {
    @NonNull
    private final MutableLiveData<STATE> state;

    public LiveViewState(@NonNull MutableLiveData<STATE> state) {
        this.state = state;
    }

    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<STATE> observer) {
        state.observe(lifecycleOwner, observer);
    }

    @MainThread
    @Override
    protected void notify(@NonNull STATE state) {
        this.state.setValue(state);
    }
}
