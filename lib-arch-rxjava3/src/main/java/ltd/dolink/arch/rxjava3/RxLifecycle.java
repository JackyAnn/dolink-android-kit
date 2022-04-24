package ltd.dolink.arch.rxjava3;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import org.reactivestreams.Publisher;

import java.util.Objects;

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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.parallel.ParallelTransformer;

public class RxLifecycle<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>, MaybeTransformer<T, T>, SingleTransformer<T, T>, ParallelTransformer<T, T>, CompletableTransformer {
    @NonNull
    private final CompositeDisposable disposable;

    public RxLifecycle(@NonNull CompositeDisposable disposable) {
        Objects.requireNonNull(disposable);
        this.disposable = disposable;
    }

    @MainThread
    public static <T> RxLifecycle<T> from(@NonNull View view) {
        Objects.requireNonNull(view);

        CompositeDisposable disposable = new CompositeDisposable();
        RxLifecycle<T> lifecycle = new RxLifecycle<>(disposable);
        view.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                view.removeOnAttachStateChangeListener(this);
                disposable.dispose();
            }
        });
        return lifecycle;
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


