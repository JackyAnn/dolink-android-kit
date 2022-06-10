package ltd.dolink.arch.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableEventData;
import androidx.lifecycle.Observer;

public class MutableLiveEvent<T> extends MutableEventData<T> {
  public MutableLiveEvent(T value) {
    super(value);
  }

  public MutableLiveEvent() {}

  @Override
  protected int getVersion() {
    return super.getVersion();
  }

  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    super.observe(owner, new ObserverWrapper<>(observer));
  }

  @Override
  public void observeForever(@NonNull Observer<? super T> observer) {
    super.observeForever(new ObserverWrapper<>(observer));
  }

  private class ObserverWrapper<T> implements Observer<T> {
    private final Observer<? super T> observer;
    private int lastVersion = getVersion();

    private ObserverWrapper(Observer<? super T> observer) {
      this.observer = observer;
    }

    @Override
    public void onChanged(T data) {
      int version = getVersion();
      if (version > lastVersion) {
        lastVersion = version;
        observer.onChanged(data);
      }
    }
  }
}
