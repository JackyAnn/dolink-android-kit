package ltd.dolink.arch.livedata;

import androidx.lifecycle.Observer;

import java.util.Objects;

public class DistinctObserver<T> implements Observer<T> {
    private final Observer<T> observer;
    boolean firstTime;
    private T previousValue;

    public DistinctObserver(Observer<T> observer) {
        this.observer = observer;
        firstTime = true;
    }

    @Override
    public void onChanged(T currentValue) {
        if (firstTime || !Objects.equals(currentValue, previousValue)) {
            firstTime = false;
            previousValue = currentValue;
            observer.onChanged(currentValue);
        }
    }
}
