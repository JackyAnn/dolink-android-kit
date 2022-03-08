package ltd.dolink.arch.rxjava2;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.Subject;
import ltd.dolink.arch.Observer;
import ltd.dolink.arch.State;
import ltd.dolink.arch.ViewState;

public class RxViewState<STATE extends State> extends ViewState<STATE> {
    @NonNull
    private final Subject<STATE> state;

    public RxViewState(@NonNull Subject<STATE> state) {
        this.state = state;
    }


    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<STATE> observer) {
        state.compose(RxLifecycle.from(lifecycleOwner))
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(state -> observer.onChanged(state));
    }

    @Override
    @MainThread
    protected void notify(@NonNull STATE state) {
        this.state.onNext(state);
    }
}
