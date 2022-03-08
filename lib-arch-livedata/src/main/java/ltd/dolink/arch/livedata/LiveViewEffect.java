package ltd.dolink.arch.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import ltd.dolink.arch.Effect;
import ltd.dolink.arch.Observer;
import ltd.dolink.arch.ViewEffect;

public class LiveViewEffect<EFFECT extends Effect> extends ViewEffect<EFFECT> {
    @NonNull
    private final MutableLiveData<EFFECT> effect;

    public LiveViewEffect(@NonNull MutableLiveData<EFFECT> effect) {
        this.effect = effect;
    }


    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<EFFECT> observer) {
        effect.observe(lifecycleOwner, observer);
    }

    @MainThread
    @Override
    protected void notify(@NonNull EFFECT effect) {
        this.effect.setValue(effect);
    }
}
