package ltd.dolink.arch.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ltd.dolink.arch.adapter.CellViewHolder.Factory;

public abstract class CellBinder {
    private final Map<Integer, Factory> viewHolderFactoryMap = new HashMap<>();


    public CellBinder() {
    }

    public abstract CellState getItem(int position);

    public abstract int getItemCount();


    public <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> void link(int cellType, @NonNull Factory<STATE, VB, VH> factory) {
        Objects.requireNonNull(factory);
        viewHolderFactoryMap.put(cellType, factory);
    }


    protected <STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> VH createViewHolder(@NonNull ViewGroup parent, int cellType) {
        @SuppressWarnings("unchecked") Factory<STATE, VB, VH> viewHolderFactory = viewHolderFactoryMap.get(cellType);
        Objects.requireNonNull(viewHolderFactory);
        VB viewBinding = viewHolderFactory.createViewBinding(parent);
        Objects.requireNonNull(viewBinding);
        return viewHolderFactory.createCellViewHolder(viewBinding);
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


}
