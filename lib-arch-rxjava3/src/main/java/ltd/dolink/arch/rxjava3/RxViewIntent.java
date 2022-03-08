package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.subjects.Subject;
import ltd.dolink.arch.Intent;
import ltd.dolink.arch.Observer;
import ltd.dolink.arch.ViewIntent;

public class RxViewIntent<INTENT extends Intent> extends ViewIntent<INTENT> {
    @NonNull
    private final Subject<INTENT> intent;

    public RxViewIntent(@NonNull Subject<INTENT> intent) {
        this.intent = intent;
    }


    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<INTENT> observer) {
        intent.compose(RxLifecycle.from(lifecycleOwner))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(intent -> observer.onChanged(intent));
    }


    @Override
    public void notify(@NonNull INTENT intent) {
        this.intent.onNext(intent);
    }
}
