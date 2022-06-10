package ltd.dolink.arch.adapter;

import android.view.ViewGroup;
import androidx.annotation.NonNull;

public interface CellViewFactory<DATA> {
  default int getCellType(DATA data) {
    return 0;
  }

  <DATA, VIEW extends CellView<DATA>> VIEW createCellView(@NonNull ViewGroup parent, int cellType);
}
