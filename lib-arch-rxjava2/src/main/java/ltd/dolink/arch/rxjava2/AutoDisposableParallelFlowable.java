package ltd.dolink.arch.rxjava2;

import androidx.annotation.NonNull;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;


public class AutoDisposableParallelFlowable<T> extends ParallelFlowable<T> {
    @NonNull
    private final ParallelFlowable<T> source;
    @NonNull
    private final CompositeDisposable disposable;

    public AutoDisposableParallelFlowable(@NonNull ParallelFlowable<T> source, @NonNull CompositeDisposable disposable) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(disposable);
        this.source = source;
        this.disposable = disposable;
    }

    @Override
    public void subscribe(Subscriber<? super T>[] subscribers) {
        int length = subscribers.length;
        @SuppressWarnings("unchecked") Subscriber<? super T>[] proxy = new AutoDisposableParallelSubscriber[length];

        for (int i = 0; i < length; i++) {
            proxy[i] = new AutoDisposableParallelSubscriber<>(subscribers[i], disposable);
        }
        source.subscribe(proxy);
    }

    @Override
    public int parallelism() {
        return source.parallelism();
    }
}

class AutoDisposableParallelSubscriber<T> extends AtomicReference<Subscription> implements Subscriber<T>, Disposable, Subscription {
    @NonNull
    private final Subscriber<T> sourceObserver;

    AutoDisposableParallelSubscriber(@NonNull Subscriber<T> observer, @NonNull CompositeDisposable disposable) {
        Objects.requireNonNull(observer);
        Objects.requireNonNull(disposable);
        this.sourceObserver = observer;
        disposable.add(new Disposable() {
            private final AtomicReference<Disposable> disposableAtomicReference = new AtomicReference<>();

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
