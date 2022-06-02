package ltd.dolink.arch.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface CellViewFactory {
    default int getCellType(Object object) {
        return 0;
    }

    <DATA, VIEW extends CellView<DATA>> VIEW createCellView(@NonNull ViewGroup parent, int cellType);
}
