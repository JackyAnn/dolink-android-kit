package ltd.dolink.arch.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class CellViewHolder<STATE extends CellState, VB extends ViewBinding> extends ViewHolder implements CellView<STATE> {

    private final VB viewBinding;
    private final LifecycleOwner lifecycleOwner = new LifecycleOwner() {
        private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycleRegistry;
        }
    };
    private CellAdapter adapter;

    public CellViewHolder(@NonNull VB viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
        if (lifecycleOwner.getLifecycle() instanceof LifecycleRegistry) {
            LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) lifecycleOwner.getLifecycle();
            lifecycleRegistry.handleLifecycleEvent(Event.ON_CREATE);
        }
    }

    protected CellAdapter getAdapter() {
        return adapter;
    }

    void setAdapter(@NonNull CellAdapter adapter) {
        this.adapter = adapter;
    }

    protected void onViewAttachedToWindow() {
        if (lifecycleOwner.getLifecycle() instanceof LifecycleRegistry) {
            LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) lifecycleOwner.getLifecycle();
            lifecycleRegistry.handleLifecycleEvent(Event.ON_START);
        }
    }

    protected void onViewDetachedFromWindow() {
        if (lifecycleOwner.getLifecycle() instanceof LifecycleRegistry) {
            LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) lifecycleOwner.getLifecycle();
            lifecycleRegistry.handleLifecycleEvent(Event.ON_DESTROY);
        }
    }

    protected void onViewRecycled() {

    }

    protected boolean onFailedToRecycleView() {
        return false;
    }

    public VB getViewBinding() {
        return viewBinding;
    }

    @Override
    public final void setState(@NonNull STATE state) {
        setState(state, null);
    }

    @NonNull
    @Override
    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public interface Factory<STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> {
        VH create(@NonNull VB viewBinding);
    }

    public static class DefaultFactory<STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> implements Factory<STATE, VB, VH> {
        @NonNull
        private final Class<VH> viewHolderClass;

        public DefaultFactory(@NonNull Class<VH> viewHolderClass) {
            Objects.requireNonNull(viewHolderClass);
            this.viewHolderClass = viewHolderClass;
        }


        @Override
        public VH create(@NonNull VB viewBinding) {
            try {
                Constructor<VH> constructor = viewHolderClass.getConstructor(viewBinding.getClass());
                constructor.setAccessible(true);
                return constructor.newInstance(viewBinding);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
