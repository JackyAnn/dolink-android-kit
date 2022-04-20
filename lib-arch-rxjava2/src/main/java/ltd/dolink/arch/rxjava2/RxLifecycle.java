package ltd.dolink.arch.rxjava2;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import org.reactivestreams.Publisher;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.parallel.ParallelTransformer;

public class RxLifecycle<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>, MaybeTransformer<T, T>, SingleTransformer<T, T>, ParallelTransformer<T, T>, CompletableTransformer {
    @NonNull
    private final CompositeDisposable disposable;

    public RxLifecycle(@NonNull CompositeDisposable disposable) {
        Objects.requireNonNull(disposable);
        this.disposable = disposable;
    }

    @MainThread
    public static <T> RxLifecycle<T> from(@NonNull LifecycleOwner lifecycleOwner) {
        return from(lifecycleOwner, Event.ON_DESTROY);
    }

    @MainThread
    public static <T> RxLifecycle<T> from(@NonNull LifecycleOwner lifecycleOwner, @NonNull Event event) {
        Objects.requireNonNull(lifecycleOwner);
        Objects.requireNonNull(event);

        CompositeDisposable disposable = new CompositeDisposable();
        RxLifecycle<T> lifecycle = new RxLifecycle<>(disposable);
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Event current) {
                if (current.compareTo(event) >= 0) {
                    lifecycleOwner.getLifecycle().removeObserver(this);
                    disposable.dispose();
                }
            }
        });
        return lifecycle;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return new AutoDisposableCompletable(upstream, disposable);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return new AutoDisposableFlowable<>(upstream, disposable);
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return new AutoDisposableMaybe<>(upstream, disposable);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return new AutoDisposableObservable<>(upstream, disposable);
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return new AutoDisposableSingle<>(upstream, disposable);
    }

    @Override
    public ParallelFlowable<T> apply(ParallelFlowable<T> upstream) {
        return new AutoDisposableParallelFlowable<>(upstream, disposable);
    }
}


