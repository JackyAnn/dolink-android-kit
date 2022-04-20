package ltd.dolink.arch.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ltd.dolink.arch.View;
import ltd.dolink.arch.adapter.CellAdapter;
import ltd.dolink.arch.adapter.CellBinder;
import ltd.dolink.arch.adapter.CellBinder.ListCellBinder;
import ltd.dolink.arch.adapter.CellState;
import ltd.dolink.arch.adapter.CellViewHolder;
import ltd.dolink.arch.adapter.CellViewHolder.Factory;
import ltd.dolink.arch.app.databinding.ActivityMainBinding;
import ltd.dolink.arch.app.databinding.ItemViewBinding;
import ltd.dolink.arch.viewbinding.ViewBindingFactory;


public class MainActivity extends AppCompatActivity implements View<LifecycleState> {
    private static final String TAG = "MainActivity";
    private final List<LifecycleState> list = new ArrayList<>();
    private ActivityMainBinding viewBinding;
    private final CellAdapter cellAdapter = new CellAdapter() {
        @Override
        protected CellBinder initializeCellBinder() {
            return new ListCellBinder(list);
        }
    };


    @Override
    public void setState(@NonNull LifecycleState state) {
        int count = list.size();
        list.add(state);
        cellAdapter.notifyItemRangeInserted(count, list.size() - count);
        viewBinding.list.scrollToPosition(cellAdapter.getItemCount() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ViewBindingFactory.of(ActivityMainBinding.class).inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewBinding.list.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.list.setAdapter(cellAdapter);
        cellAdapter.getCellBinder().link(0x0, new Factory<LifecycleState, ItemViewBinding, ItemViewHolder>() {
            @Override
            public ItemViewBinding createViewBinding(@NonNull ViewGroup parent) {
                return ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            }

            @Override
            public ItemViewHolder createCellViewHolder(@NonNull ItemViewBinding viewBinding) {
                return new ItemViewHolder(viewBinding);
            }
        });


        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            setState(new LifecycleState(event));
        });


    }


    static class ItemViewHolder extends CellViewHolder<LifecycleState, ItemViewBinding> {

        public ItemViewHolder(@NonNull ItemViewBinding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void setState(@NonNull LifecycleState state) {
            getViewBinding().text.setText(state.toString());
        }
    }

}


class LifecycleState implements CellState {
    @NonNull
    private final Event event;

    LifecycleState(@NonNull Event event) {
        this.event = event;
    }

    @NonNull
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LifecycleState{");
        sb.append("event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}











