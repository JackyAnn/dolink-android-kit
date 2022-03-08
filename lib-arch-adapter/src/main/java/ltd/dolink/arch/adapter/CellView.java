package ltd.dolink.arch.adapter;

import androidx.annotation.NonNull;

import java.util.List;

import ltd.dolink.arch.View;

public interface CellView<STATE extends CellState> extends View<STATE> {

    void setState(@NonNull STATE state, List<Object> payloads);
}
