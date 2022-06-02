package ltd.dolink.arch.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;

import java.util.List;

public class CellLoadStateAdapter<VIEW extends CellView<LoadState>> extends LoadStateAdapter<VIEW> {
    @NonNull
    private final CellViewFactory cellViewFactory;

    public CellLoadStateAdapter(@NonNull CellViewFactory cellViewFactory) {
        this.cellViewFactory = cellViewFactory;
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW view, @NonNull LoadState loadState) {
        view.setState(loadState);
    }

    @NonNull
    @Override
    public VIEW onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        return cellViewFactory.createCellView(viewGroup, getStateViewType(loadState));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW view, int position, @NonNull List<Object> payloads) {
        view.setState(getLoadState(), payloads);
    }

    @Override
    public int getStateViewType(@NonNull LoadState loadState) {
        return cellViewFactory.getCellType(loadState);
    }

    @Override
    public void onViewRecycled(@NonNull VIEW view) {
        view.onViewRecycled();
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VIEW view) {
        return view.onFailedToRecycleView();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VIEW view) {
        view.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VIEW view) {
        view.onViewDetachedFromWindow();
    }
}
