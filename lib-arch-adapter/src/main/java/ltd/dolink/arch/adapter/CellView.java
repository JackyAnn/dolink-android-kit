package ltd.dolink.arch.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

public abstract class CellView<DATA> extends ViewHolder implements ltd.dolink.arch.View<DATA> {

  public CellView(@NonNull View itemView) {
    super(itemView);
  }

  protected void onViewAttachedToWindow() {}

  protected void onViewDetachedFromWindow() {}

  protected void onViewRecycled() {}

  public void setState(DATA data, @NonNull List<Object> payloads) {
    setState(data);
  }

  protected boolean onFailedToRecycleView() {
    return false;
  }
}
