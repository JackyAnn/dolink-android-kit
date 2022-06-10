package ltd.dolink.arch.rxjava3;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.parallel.ParallelTransformer;
import org.reactivestreams.Publisher;

import java.util.Objects;

public class RxLifecycle<T>
    implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        ParallelTransformer<T, T>,
        CompletableTransformer {
  @NonNull private final ListenableCloseable listenableCloseable;

  private RxLifecycle(@NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(listenableCloseable);
    this.listenableCloseable = listenableCloseable;
  }

  public static <T> RxLifecycle<T> from(@NonNull ListenableFuture<Void> listenableFuture) {
    Objects.requireNonNull(listenableFuture);
    ListenableCloseable closeable = new ListenableCloseable();
    RxLifecycle<T> lifecycle = new RxLifecycle<>(closeable);
    listenableFuture.addListener(
        () -> {
          try {
            closeable.close();
          } catch (Exception e) {
          }
        },
        Runnable::run);
    return lifecycle;
  }

  public static <T> RxLifecycle<T> from(@NonNull ListenableCloseable listenableCloseable) {
    Objects.requireNonNull(listenableCloseable);

    ListenableCloseable closeable = new ListenableCloseable();
    RxLifecycle<T> lifecycle = new RxLifecycle<>(closeable);
    listenableCloseable.add(
        new AutoCloseable() {
          @Override
          public void close() {
            listenableCloseable.remove(this);
            try {
              closeable.close();
            } catch (Exception e) {
            }
          }
        });
    return lifecycle;
  }

  @MainThread
  public static <T> RxLifecycle<T> from(@NonNull LifecycleOwner lifecycleOwner) {
    Objects.requireNonNull(lifecycleOwner);

    ListenableCloseable closeable = new ListenableCloseable();
    RxLifecycle<T> lifecycle = new RxLifecycle<>(closeable);
    lifecycleOwner
        .getLifecycle()
        .addObserver(
            new LifecycleEventObserver() {
              @Override
              public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Event current) {
                if (current.compareTo(Event.ON_DESTROY) >= 0) {
                  lifecycleOwner.getLifecycle().removeObserver(this);
                  try {
                    closeable.close();
                  } catch (Exception e) {
                  }
                }
              }
            });
    return lifecycle;
  }

  @Override
  public CompletableSource apply(Completable upstream) {
    return new AutoDisposableCompletable(upstream, listenableCloseable);
  }

  @Override
  public Publisher<T> apply(Flowable<T> upstream) {
    return new AutoDisposableFlowable<>(upstream, listenableCloseable);
  }

  @Override
  public MaybeSource<T> apply(Maybe<T> upstream) {
    return new AutoDisposableMaybe<>(upstream, listenableCloseable);
  }

  @Override
  public ObservableSource<T> apply(Observable<T> upstream) {
    return new AutoDisposableObservable<>(upstream, listenableCloseable);
  }

  @Override
  public SingleSource<T> apply(Single<T> upstream) {
    return new AutoDisposableSingle<>(upstream, listenableCloseable);
  }

  @Override
  public ParallelFlowable<T> apply(ParallelFlowable<T> upstream) {
    return new AutoDisposableParallelFlowable<>(upstream, listenableCloseable);
  }
}
