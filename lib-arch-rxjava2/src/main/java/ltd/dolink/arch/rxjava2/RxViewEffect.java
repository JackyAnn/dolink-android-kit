package ltd.dolink.arch.rxjava2;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.Subject;
import ltd.dolink.arch.Effect;
import ltd.dolink.arch.Observer;
import ltd.dolink.arch.ViewEffect;

public class RxViewEffect<EFFECT extends Effect> extends ViewEffect<EFFECT> {
    @NonNull
    private final Subject<EFFECT> effect;

    public RxViewEffect(@NonNull Subject<EFFECT> effect) {
        this.effect = effect;
    }


    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<EFFECT> observer) {
        effect.compose(RxLifecycle.from(lifecycleOwner))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(effect -> observer.onChanged(effect));
    }


    @Override
    protected void notify(@NonNull EFFECT effect) {
        this.effect.onNext(effect);
    }
}
