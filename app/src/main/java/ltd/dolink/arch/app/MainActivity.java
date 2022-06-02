package ltd.dolink.arch.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.recyclerview.widget.LinearLayoutManager;

import ltd.dolink.arch.View;
import ltd.dolink.arch.adapter.CellAdapter;
import ltd.dolink.arch.adapter.CellView;
import ltd.dolink.arch.adapter.CellViewFactory;
import ltd.dolink.arch.app.databinding.ActivityMainBinding;
import ltd.dolink.arch.app.databinding.ItemViewBinding;


public class MainActivity extends AppCompatActivity implements View<LifecycleEvent> {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding viewBinding;
    private final CellAdapter<LifecycleEvent, ItemViewHolder> cellAdapter = new CellAdapter(new CellViewFactory() {
        @Override
        public <DATA, VIEW extends CellView<DATA>> VIEW createCellView(@NonNull ViewGroup parent, int cellType) {
            return (VIEW) new ItemViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    });

    @Override
    public void setState(@NonNull LifecycleEvent state) {
        int count = cellAdapter.getItemCount();
        cellAdapter.getCurrentList().add(state);
        cellAdapter.notifyItemRangeInserted(count, cellAdapter.getItemCount() - count);
        viewBinding.list.scrollToPosition(cellAdapter.getItemCount() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.list.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.list.setAdapter(cellAdapter);


        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            setState(new LifecycleEvent(event));
        });


    }


    static class ItemViewHolder extends CellView<LifecycleEvent> {
        private final ItemViewBinding viewBinding;

        public ItemViewHolder(@NonNull ItemViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        @Override
        public void setState(@NonNull LifecycleEvent state) {
            viewBinding.text.setText(state.toString());
        }
    }

}


class LifecycleEvent {
    @NonNull
    private final Event event;

    LifecycleEvent(@NonNull Event event) {
        this.event = event;
    }

    @NonNull
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LifecycleEvent{");
        sb.append("event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}









