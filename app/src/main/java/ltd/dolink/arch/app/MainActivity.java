package ltd.dolink.arch.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ltd.dolink.arch.Effect;
import ltd.dolink.arch.Intent;
import ltd.dolink.arch.View;
import ltd.dolink.arch.adapter.CellAdapter;
import ltd.dolink.arch.adapter.CellBinder;
import ltd.dolink.arch.adapter.CellBinder.ListCellBinder;
import ltd.dolink.arch.adapter.CellState;
import ltd.dolink.arch.adapter.CellViewHolder;
import ltd.dolink.arch.app.databinding.ActivityMainBinding;
import ltd.dolink.arch.app.databinding.ItemView0Binding;
import ltd.dolink.arch.initializer.ApplicationOwner;
import ltd.dolink.arch.livedata.LiveViewModel;
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

    private LifecycleViewModel viewModel;

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
        cellAdapter.getCellBinder().link(0x0, LifecycleState.class, ItemView0Binding.class, LifecycleViewHolder.class);

        viewModel = ApplicationOwner.getInstance().of(this).get(LifecycleViewModel.class);
        setViewModel(viewModel);
        getLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            viewModel.handleIntent(new ShowLifeCycleEventIntent(event));
        });


    }


    @NonNull
    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    public static class LifecycleViewModel extends LiveViewModel<LifecycleState> {


        public void handleIntent(@NonNull ShowLifeCycleEventIntent intent) {
            setState(new LifecycleState(intent.getEvent()));
        }
    }

    static class LifecycleViewHolder extends CellViewHolder<LifecycleState, ItemView0Binding> {

        public LifecycleViewHolder(@NonNull ItemView0Binding viewBinding) {
            super(viewBinding);
        }

        @Override
        public void setState(@NonNull LifecycleState state, List<Object> payloads) {
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


class LifecycleEffect implements Effect {
    @NonNull
    private final Event event;

    LifecycleEffect(@NonNull Event event) {
        this.event = event;
    }

    @NonNull
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LifecycleEffect{");
        sb.append("event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}

class ShowLifeCycleEventIntent implements Intent {
    @NonNull
    private final Event event;

    ShowLifeCycleEventIntent(@NonNull Event event) {
        this.event = event;
    }

    @NonNull
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShowLifeCycleEventIntent{");
        sb.append("event=").append(event);
        sb.append('}');
        return sb.toString();
    }
}

class ClientEventIntent implements Intent {
    @NonNull
    private final long time;

    public ClientEventIntent() {
        this.time = System.currentTimeMillis();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientEventIntent{");
        sb.append("time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}







