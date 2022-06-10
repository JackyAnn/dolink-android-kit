package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AutoDisposableSingle<T> extends Single<T> {
  @NonNull private final Single<T> source;
  @NonNull private final CompositeDisposable disposable;

  public AutoDisposableSingle(@NonNull Single<T> source, @NonNull CompositeDisposable disposable) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(disposable);
    this.source = source;
    this.disposable = disposable;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    source.subscribe(new AutoDisposableSingleObserver<>(observer, disposable));
  }
}

class AutoDisposableSingleObserver<T> extends AtomicReference<Disposable>
    implements SingleObserver<T>, Disposable {
  @NonNull private final SingleObserver<T> sourceObserver;

  AutoDisposableSingleObserver(
      @NonNull SingleObserver<T> observer, @NonNull CompositeDisposable disposable) {
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
  public void onSuccess(T t) {
    if (!isDisposed()) {
      lazySet(DisposableHelper.DISPOSED);
      try {
        sourceObserver.onSuccess(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
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
  public void dispose() {
    DisposableHelper.dispose(this);
  }

  @Override
  public boolean isDisposed() {
    return DisposableHelper.isDisposed(get());
  }
}