package ltd.dolink.arch.adapter;

import ltd.dolink.arch.State;

public interface CellState extends State {
    default int getStateType() {
        return 0;
    }
}
