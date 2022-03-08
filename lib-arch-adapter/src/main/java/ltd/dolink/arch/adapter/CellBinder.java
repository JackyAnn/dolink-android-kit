package ltd.dolink.arch.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ltd.dolink.arch.adapter.CellViewHolder.DefaultFactory;
import ltd.dolink.arch.adapter.CellViewHolder.Factory;
import ltd.dolink.arch.viewbinding.ViewBindingFactory;

public abstract class CellBinder {
    private final Map<Integer, ViewHolderFactoryBoundViewBinding> viewHolderFactoryMap = new HashMap<>();


    public CellBinder() {
    }

    public abstract CellState getItem(int position);

    public abstract int getItemCount();


    public <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> void link(int cellType, @NonNull Class<STATE> stateClass, @NonNull Class<VB> viewBindingClass, @NonNull Class<VH> viewHolderClass) {
        link(cellType, stateClass, ViewBindingFactory.of(viewBindingClass), new DefaultFactory<>(viewHolderClass));
    }

    public <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> void link(int cellType, @NonNull Class<STATE> stateClass, @NonNull ViewBindingFactory<VB> viewBindingFactory, @NonNull Class<VH> viewHolderClass) {
        link(cellType, stateClass, viewBindingFactory, new DefaultFactory<>(viewHolderClass));
    }

    public <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> void link(int cellType, @NonNull Class<STATE> stateClass, @NonNull Class<VB> viewBindingClass, @NonNull Factory<STATE, VB, VH> viewHolderFactory) {
        link(cellType, stateClass, ViewBindingFactory.of(viewBindingClass), viewHolderFactory);
    }

    public <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> void link(int cellType, @NonNull Class<STATE> stateClass, @NonNull ViewBindingFactory<VB> viewBindingFactory, @NonNull Factory<STATE, VB, VH> viewHolderFactory) {
        viewHolderFactoryMap.put(cellType, new ViewHolderFactoryBoundViewBinding<>(stateClass, viewBindingFactory, viewHolderFactory));
    }

    protected <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> VH createViewHolder(@NonNull ViewGroup parent, int cellType) {
        @SuppressWarnings("unchecked") ViewHolderFactoryBoundViewBinding<STATE, VB, VH> viewHolderFactory = viewHolderFactoryMap.get(cellType);
        Objects.requireNonNull(viewHolderFactory);
        VB viewBinding = viewHolderFactory.viewBindingFactory.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return viewHolderFactory.create(viewBinding);
    }

    public static class ListCellBinder<STATE extends CellState> extends CellBinder {
        @NonNull
        private final List<STATE> data;

        public ListCellBinder(@NonNull List<STATE> data) {
            super();
            this.data = data;
        }

        @NonNull
        public List<STATE> getData() {
            return data;
        }

        @Override
        public STATE getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public static class ArrayBinder<STATE extends CellState> extends CellBinder {
        @NonNull
        private final STATE[] data;

        public ArrayBinder(@NonNull STATE[] data) {
            super();
            this.data = data;
        }

        @NonNull
        public STATE[] getData() {
            return data;
        }

        @Override
        public STATE getItem(int position) {
            return data[position];
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }

    static class ViewHolderFactoryBoundViewBinding<STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> implements Factory<STATE, VB, VH> {
        @NonNull
        private final ViewBindingFactory<VB> viewBindingFactory;
        @NonNull
        private final Class<STATE> stateClass;
        @NonNull
        private final Factory<STATE, VB, VH> viewHolderFactory;

        ViewHolderFactoryBoundViewBinding(@NonNull Class<STATE> stateClass, @NonNull ViewBindingFactory<VB> viewBindingFactory, @NonNull Factory<STATE, VB, VH> viewHolderFactory) {
            this.stateClass = stateClass;
            this.viewBindingFactory = viewBindingFactory;
            this.viewHolderFactory = viewHolderFactory;
        }


        @Override
        public VH create(@NonNull VB viewBinding) {
            return viewHolderFactory.create(viewBinding);
        }
    }
}
