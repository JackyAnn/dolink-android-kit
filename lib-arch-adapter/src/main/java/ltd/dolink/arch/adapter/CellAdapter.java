package ltd.dolink.arch.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.viewbinding.ViewBinding;

import java.util.List;
import java.util.Objects;

public abstract class CellAdapter extends Adapter<CellViewHolder<CellState, ViewBinding>> {
    @NonNull
    private final CellBinder cellBinder;

    protected CellAdapter() {
        this.cellBinder = initializeCellBinder();
        Objects.requireNonNull(cellBinder);
    }

    @NonNull
    public CellBinder getCellBinder() {
        return cellBinder;
    }

    protected abstract CellBinder initializeCellBinder();

    @Override
    public int getItemCount() {
        return cellBinder.getItemCount();
    }

    public CellState getItem(int position) {
        return cellBinder.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getStateType();
    }

    @NonNull
    @Override
    public CellViewHolder<CellState, ViewBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellViewHolder<CellState, ViewBinding> viewHolder = cellBinder.createViewHolder(parent, viewType);
        viewHolder.setAdapter(this);
        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CellViewHolder<CellState, ViewBinding> holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CellViewHolder<CellState, ViewBinding> holder) {
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(@NonNull CellViewHolder<CellState, ViewBinding> holder) {
        holder.onViewRecycled();
    }


    @Override
    public boolean onFailedToRecycleView(@NonNull CellViewHolder<CellState, ViewBinding> holder) {
        return holder.onFailedToRecycleView();
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder<CellState, ViewBinding> holder, int position) {
        holder.setState(getItem(position));
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder<CellState, ViewBinding> holder, int position, @NonNull List<Object> payloads) {
        holder.setState(getItem(position));
    }

}
