package ltd.dolink.arch.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import ltd.dolink.arch.Intent;
import ltd.dolink.arch.Observer;
import ltd.dolink.arch.ViewIntent;

public class LiveViewIntent<INTENT extends Intent> extends ViewIntent<INTENT> {
    @NonNull
    private final MutableLiveData<INTENT> intent;

    public LiveViewIntent(@NonNull MutableLiveData<INTENT> intent) {
        this.intent = intent;
    }


    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<INTENT> observer) {
        intent.observe(lifecycleOwner, observer);
    }

    @MainThread
    @Override
    public void notify(@NonNull INTENT intent) {
        this.intent.setValue(intent);
    }
}
