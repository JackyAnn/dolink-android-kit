package ltd.dolink.arch.rxjava3;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ListenableCloseable implements AutoCloseable {
  private final Set<AutoCloseable> listeners = new HashSet<>();
  private volatile boolean closed = false;

  public synchronized boolean isClosed() {
    return closed;
  }

  @Override
  public synchronized void close() throws Exception {
    closed = true;
    for (AutoCloseable closeable : listeners) {
      closeable.close();
    }
    listeners.clear();
  }

  public synchronized void add(@NonNull AutoCloseable closeable) {
    if (closed) {
      try {
        closeable.close();
      } catch (Exception e) {
      }
      return;
    }

    listeners.add(closeable);
  }

  public synchronized void remove(@NonNull AutoCloseable closeable) {
    listeners.remove(closeable);
  }
}
