package ltd.dolink.arch.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class DistinctLiveData<T> extends MutableLiveData<T> {
  public DistinctLiveData(T value) {
    super(value);
  }

  public DistinctLiveData() {}

  @Override
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    super.observe(owner, new DistinctObserver<>(observer));
  }

  @Override
  public void observeForever(@NonNull Observer<? super T> observer) {
    super.observeForever(new DistinctObserver<>(observer));
  }
}
