package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AutoDisposableCompletable extends Completable {
  @NonNull private final Completable source;
  @NonNull private final ListenableCloseable listenableCloseable;

  public AutoDisposableCompletable(
      @NonNull Completable source, @NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(listenableCloseable);
    this.source = source;
    this.listenableCloseable = listenableCloseable;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
    source.subscribe(new AutoDisposableCompletableObserver(observer, listenableCloseable));
  }
}

class AutoDisposableCompletableObserver extends AtomicReference<Disposable>
    implements CompletableObserver, Disposable {
  @NonNull private final CompletableObserver sourceObserver;

  AutoDisposableCompletableObserver(
      @NonNull CompletableObserver observer, @NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(observer);
    Objects.requireNonNull(listenableCloseable);
    this.sourceObserver = observer;
    listenableCloseable.add(
        new AutoCloseable() {
          @Override
          public void close() {
            listenableCloseable.remove(this);
            onDispose();
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
