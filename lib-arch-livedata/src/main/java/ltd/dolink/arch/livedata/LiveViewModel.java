package ltd.dolink.arch.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import ltd.dolink.arch.State;
import ltd.dolink.arch.ViewModel;
import ltd.dolink.arch.ViewState;

public abstract class LiveViewModel<STATE extends State> extends androidx.lifecycle.ViewModel implements ViewModel<STATE> {
    @NonNull
    private final LiveViewState<STATE> viewState = initializeViewState();

    public LiveViewModel() {
    }


    @NonNull
    protected LiveViewState<STATE> initializeViewState() {
        return new LiveViewState<>(initializeLiveData());
    }

    protected MutableLiveData<STATE> initializeLiveData() {
        return new DistinctLiveData<>();
    }


    @NonNull
    @Override
    public ViewState<STATE> viewState() {
        return viewState;
    }


    @MainThread
    protected void setState(@NonNull STATE state) {
        viewState.notify(state);
    }

}
