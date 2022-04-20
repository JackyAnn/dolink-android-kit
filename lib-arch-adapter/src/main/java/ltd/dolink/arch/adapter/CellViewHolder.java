package ltd.dolink.arch.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.viewbinding.ViewBinding;

public abstract class CellViewHolder<STATE extends CellState, VB extends ViewBinding> extends ViewHolder implements CellView<STATE> {

    private final VB viewBinding;
    private CellAdapter adapter;

    public CellViewHolder(@NonNull VB viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    protected CellAdapter getAdapter() {
        return adapter;
    }

    void setAdapter(@NonNull CellAdapter adapter) {
        this.adapter = adapter;
    }

    protected void onViewAttachedToWindow() {

    }

    protected void onViewDetachedFromWindow() {

    }

    protected void onViewRecycled() {
    }

    protected boolean onFailedToRecycleView() {
        return false;
    }

    public VB getViewBinding() {
        return viewBinding;
    }

    public interface Factory<STATE extends CellState, VB extends ViewBinding, VH extends CellViewHolder<STATE, VB>> {
        VB createViewBinding(@NonNull ViewGroup parent);

        VH createCellViewHolder(@NonNull VB viewBinding);
    }
}
