package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AutoDisposableFlowable<T> extends Flowable<T> {
  @NonNull private final Flowable<T> source;
  @NonNull private final ListenableCloseable listenableCloseable;

  public AutoDisposableFlowable(
      @NonNull Flowable<T> source, @NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(listenableCloseable);
    this.source = source;
    this.listenableCloseable = listenableCloseable;
  }

  @Override
  protected void subscribeActual(Subscriber<? super T> subscriber) {
    source.subscribe(new AutoDisposableSubscriber<>(subscriber, listenableCloseable));
  }
}

class AutoDisposableSubscriber<T> extends AtomicReference<Subscription>
    implements Subscriber<T>, Disposable, Subscription {
  @NonNull private final Subscriber<T> sourceObserver;
  @NonNull private final ListenableCloseable listenableCloseable;

  AutoDisposableSubscriber(
      @NonNull Subscriber<T> observer, @NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(observer);
    Objects.requireNonNull(listenableCloseable);
    this.sourceObserver = observer;
    this.listenableCloseable = listenableCloseable;
    this.listenableCloseable.add(
        new AutoCloseable() {
          @Override
          public void close() {
            listenableCloseable.remove(this);
            onDispose();
          }
        });
  }

  private void onDispose() {
    if (!isDisposed()) {
      dispose();
    }
    if (!listenableCloseable.isClosed()) {
      try {
        listenableCloseable.close();
      } catch (Exception e) {
      }
    }
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    if (SubscriptionHelper.setOnce(this, subscription)) {
      try {
        sourceObserver.onSubscribe(this);
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        subscription.cancel();
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
        get().cancel();
        onError(e);
      }
    }
  }

  @Override
  public void onError(Throwable t) {
    if (!isDisposed()) {
      lazySet(SubscriptionHelper.CANCELLED);
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
      lazySet(SubscriptionHelper.CANCELLED);
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
    SubscriptionHelper.cancel(this);
  }

  @Override
  public boolean isDisposed() {
    return get() == SubscriptionHelper.CANCELLED;
  }

  @Override
  public void request(long n) {
    get().request(n);
  }

  @Override
  public void cancel() {
    dispose();
  }
}
