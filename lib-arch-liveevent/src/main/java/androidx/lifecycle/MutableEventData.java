package androidx.lifecycle;

import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class MutableEventData<T> extends MutableLiveData<T> {
  public MutableEventData(T value) {
    super(value);
  }

  public MutableEventData() {}

  @Override
  protected int getVersion() {
    return super.getVersion();
  }
}
