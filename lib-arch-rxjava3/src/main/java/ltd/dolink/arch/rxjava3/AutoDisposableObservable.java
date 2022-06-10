package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AutoDisposableObservable<T> extends Observable<T> {
  @NonNull private final Observable<T> source;
  @NonNull private final CompositeDisposable disposable;

  public AutoDisposableObservable(
      @NonNull Observable<T> source, @NonNull CompositeDisposable disposable) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(disposable);
    this.source = source;
    this.disposable = disposable;
  }

  @Override
  protected void subscribeActual(Observer<? super T> observer) {
    source.subscribe(new AutoDisposableObserver<>(observer, disposable));
  }
}

class AutoDisposableObserver<T> extends AtomicReference<Disposable>
    implements Observer<T>, Disposable {
  @NonNull private final Observer<T> sourceObserver;

  AutoDisposableObserver(@NonNull Observer<T> observer, @NonNull CompositeDisposable disposable) {
    Objects.requireNonNull(observer);
    Objects.requireNonNull(disposable);
    this.sourceObserver = observer;
    disposable.add(
        new Disposable() {
          private final AtomicReference<Disposable> disposableAtomicReference =
              new AtomicReference<>();

          @Override
          public void dispose() {
            DisposableHelper.dispose(disposableAtomicReference);
            onDispose();
          }

          @Override
          public boolean isDisposed() {
            return DisposableHelper.isDisposed(disposableAtomicReference.get());
          }
        });
  }

  private void onDispose() {
    dispose();
  }

  @Override
  public void onSubscribe(Disposable disposable) {
    if (DisposableHelper.setOnce(this, disposable)) {
      try {
        sourceObserver.onSubscribe(this);
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        disposable.dispose();
        onError(ex);
      }
    }
  }

  @Override
  public void onNext(T t) {
    if (!isDisposed()) {
      try {
        sourceObserver.onNext(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        get().dispose();
        onError(e);
      }
    }
  }

  @Override
  public void onError(Throwable t) {
    if (!isDisposed()) {
      lazySet(DisposableHelper.DISPOSED);
      try {
        sourceObserver.onError(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        RxJavaPlugins.onError(new CompositeException(t, e));
      }
    } else {
      RxJavaPlugins.onError(t);
    }
  }

  @Override
  public void onComplete() {
    if (!isDisposed()) {
      lazySet(DisposableHelper.DISPOSED);
      try {
        sourceObserver.onComplete();
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        RxJavaPlugins.onError(e);
      }
    }
  }

  @Override
  public void dispose() {
    DisposableHelper.dispose(this);
  }

  @Override
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(get());
  }
}
