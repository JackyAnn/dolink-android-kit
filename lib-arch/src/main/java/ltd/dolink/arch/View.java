package ltd.dolink.arch;

import androidx.annotation.NonNull;

public interface View<STATE> {
    void setState(@NonNull STATE state);
}
