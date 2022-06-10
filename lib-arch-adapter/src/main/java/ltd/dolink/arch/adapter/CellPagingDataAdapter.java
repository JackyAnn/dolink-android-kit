package ltd.dolink.arch.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import kotlin.coroutines.CoroutineContext;

import java.util.List;

public class CellPagingDataAdapter<DATA, VIEW extends CellView<DATA>>
    extends PagingDataAdapter<DATA, VIEW> {

  @NonNull private final CellViewFactory<DATA> cellViewFactory;

  public CellPagingDataAdapter(
      @NonNull CellViewFactory<DATA> cellViewFactory,
      @NonNull ItemCallback<DATA> diffCallback,
      @NonNull CoroutineContext mainDispatcher) {
    super(diffCallback, mainDispatcher);
    this.cellViewFactory = cellViewFactory;
  }

  public CellPagingDataAdapter(
      @NonNull CellViewFactory<DATA> cellViewFactory, @NonNull ItemCallback<DATA> diffCallback) {
    super(diffCallback);
    this.cellViewFactory = cellViewFactory;
  }

  public CellPagingDataAdapter(
      @NonNull CellViewFactory<DATA> cellViewFactory,
      @NonNull ItemCallback<DATA> diffCallback,
      @NonNull CoroutineContext mainDispatcher,
      @NonNull CoroutineContext workerDispatcher) {
    super(diffCallback, mainDispatcher, workerDispatcher);
    this.cellViewFactory = cellViewFactory;
  }

  @Override
  public int getItemViewType(int position) {
    return cellViewFactory.getCellType(getItem(position));
  }

  @Override
  public void onBindViewHolder(@NonNull VIEW view, int position, @NonNull List<Object> payloads) {
    view.setState(getItem(position), payloads);
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

  @NonNull
  @Override
  public VIEW onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return cellViewFactory.createCellView(parent, viewType);
  }

  @Override
  public void onBindViewHolder(@NonNull VIEW view, int position) {
    view.setState(getItem(position));
  }
}
