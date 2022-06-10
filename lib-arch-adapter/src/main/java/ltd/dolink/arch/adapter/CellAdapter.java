package ltd.dolink.arch.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.LinkedList;
import java.util.List;

public class CellAdapter<DATA, VIEW extends CellView<DATA>> extends Adapter<VIEW> {
  @NonNull private final CellViewFactory<DATA> cellViewFactory;
  @NonNull private final List<DATA> data = new LinkedList<>();

  public CellAdapter(@NonNull CellViewFactory<DATA> cellViewFactory) {
    this.cellViewFactory = cellViewFactory;
  }

  protected CellAdapter(@NonNull CellViewFactory<DATA> cellViewFactory, @NonNull List<DATA> data) {
    this.cellViewFactory = cellViewFactory;
    this.data.addAll(data);
  }

  @NonNull
  public List<DATA> getCurrentList() {
    return data;
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public DATA getItem(int position) {
    return data.get(position);
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
