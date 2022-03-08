package ltd.dolink.arch.rxjava2;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import ltd.dolink.arch.State;
import ltd.dolink.arch.ViewModel;
import ltd.dolink.arch.ViewState;

public abstract class RxViewModel<STATE extends State> extends androidx.lifecycle.ViewModel implements ViewModel<STATE> {
    @NonNull
    private final RxViewState<STATE> viewState = initializeViewState();

    public RxViewModel() {
    }


    @NonNull
    protected RxViewState<STATE> initializeViewState() {
        return new RxViewState<>(initializeSubject());
    }

    @NonNull
    protected Subject<STATE> initializeSubject() {
        return BehaviorSubject.create();
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
