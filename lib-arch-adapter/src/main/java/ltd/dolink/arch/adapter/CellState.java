package ltd.dolink.arch.adapter;

public interface CellState {
    default int getStateType() {
        return 0;
    }
}
