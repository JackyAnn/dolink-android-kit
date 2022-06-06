package ltd.dolink.arch.app;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Random;

import ltd.dolink.arch.View;
import ltd.dolink.arch.adapter.CellAdapter;
import ltd.dolink.arch.adapter.CellView;
import ltd.dolink.arch.adapter.CellViewFactory;
import ltd.dolink.arch.app.databinding.ActivityMainBinding;
import ltd.dolink.arch.app.databinding.ItemViewBinding;
import ltd.dolink.arch.treenode.Expandable;
import ltd.dolink.arch.treenode.Selectable;
import ltd.dolink.arch.treenode.SimpleTreeNode;
import ltd.dolink.arch.treenode.TreeNode;


public class MainActivity extends AppCompatActivity implements View<LifecycleEvent> {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding viewBinding;
    private final ConcatAdapter concatAdapter = new ConcatAdapter();
    private final CellAdapter<TreeNode<TextNode>, TextNodeCellView> treeAdapter = new CellAdapter<>(new CellViewFactory<>() {
        @Override
        public <DATA, VIEW extends CellView<DATA>> VIEW createCellView(@NonNull ViewGroup parent, int cellType) {
            return (VIEW) new TextNodeCellView(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    });
    private final CellAdapter<LifecycleEvent, LifecycleCellView> cellAdapter = new CellAdapter<>(new CellViewFactory<>() {

        @Override
        public <DATA, VIEW extends CellView<DATA>> VIEW createCellView(@NonNull ViewGroup parent, int cellType) {
            return (VIEW) new LifecycleCellView(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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


        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            setState(new LifecycleEvent(event));
        });

        for (int i = 0; i < 3; i++) {

            if (i % 3 == 1) {
                SimpleTreeNode<TextNode> node0 = new SimpleTreeNode<>(0, new TextNode(), null);
                treeAdapter.getCurrentList().add(node0);
                for (int j = 0; j < 2; j++) {
                    SimpleTreeNode<TextNode> node1 = new SimpleTreeNode<>(1, new TextNode(), node0);
                    node0.getChildren().add(node1);
                    treeAdapter.getCurrentList().add(node1);
                    for (int k = 0; k < 2; k++) {
                        SimpleTreeNode<TextNode> node2 = new SimpleTreeNode<>(2, new TextNode(), node1);
                        treeAdapter.getCurrentList().add(node2);
                        node1.getChildren().add(node2);
                        for (int l = 0; l < 2; l++) {
                            SimpleTreeNode<TextNode> node3 = new SimpleTreeNode<>(3, new TextNode(), node2);
                            treeAdapter.getCurrentList().add(node3);
                            node2.getChildren().add(node3);
                        }
                    }

                }
            } else {
                treeAdapter.getCurrentList().add(new SimpleTreeNode<>(0, new TextNode(), null));
            }


        }
        concatAdapter.addAdapter(treeAdapter);
        concatAdapter.addAdapter(cellAdapter);
        viewBinding.list.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.list.setAdapter(concatAdapter);
        viewBinding.send.setOnClickListener(v -> {
            int type = new Random().nextInt(3);
            int count = treeAdapter.getCurrentList().size();
            if (type == 1) {
                SimpleTreeNode<TextNode> node0 = new SimpleTreeNode<>(0, new TextNode(), null);
                treeAdapter.getCurrentList().add(node0);
                for (int j = 0; j < 2; j++) {
                    SimpleTreeNode<TextNode> node1 = new SimpleTreeNode<>(1, new TextNode(), node0);
                    node0.getChildren().add(node1);
                    treeAdapter.getCurrentList().add(node1);
                    for (int k = 0; k < 2; k++) {
                        SimpleTreeNode<TextNode> node2 = new SimpleTreeNode<>(2, new TextNode(), node1);
                        treeAdapter.getCurrentList().add(node2);
                        node1.getChildren().add(node2);

                        for (int l = 0; l < 2; l++) {
                            SimpleTreeNode<TextNode> node3 = new SimpleTreeNode<>(3, new TextNode(), node2);
                            treeAdapter.getCurrentList().add(node3);
                            node2.getChildren().add(node3);
                        }
                    }

                }
            } else {
                treeAdapter.getCurrentList().add(new SimpleTreeNode<>(0, new TextNode(), null));
            }
            treeAdapter.notifyItemRangeInserted(count, treeAdapter.getCurrentList().size() - count);
            viewBinding.list.scrollToPosition(treeAdapter.getItemCount() - 1);

        });

    }


    class LifecycleCellView extends CellView<LifecycleEvent> {
        private final ItemViewBinding viewBinding;

        public LifecycleCellView(@NonNull ItemViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        @Override
        public void setState(@NonNull LifecycleEvent state) {
            viewBinding.text.setText(state.toString());
        }
    }

    class TextNodeCellView extends CellView<TreeNode<TextNode>> {
        private final ItemViewBinding viewBinding;

        public TextNodeCellView(@NonNull ItemViewBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            viewBinding.getRoot().setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                TreeNode<TextNode> data = treeAdapter.getItem(position);
                if (data.isExpanded()) {
                    data.expandToList(false, nodes -> {
                        data.setExpand(false);
                        treeAdapter.getCurrentList().removeAll(nodes);
                        treeAdapter.notifyItemRangeRemoved(position + 1, nodes.size());
                    });
                } else {
                    data.expandToList(true, nodes -> {
                        data.setExpand(true);
                        treeAdapter.getCurrentList().addAll(position + 1, nodes);
                        treeAdapter.notifyItemRangeInserted(position + 1, nodes.size());
                    });
                }
            });
        }

        @Override
        public void setState(TreeNode<TextNode> node) {
            TextView textView = viewBinding.text;
            String prefix = "";
            for (int i = 0; i < node.getDepth(); i++) {
                prefix = prefix + "----";
            }
            textView.setText(prefix + node.getData() + ", child:" + node.getChildCount());
            if (node.getDepth() == 0) {
                textView.setTextColor(getColor(android.R.color.holo_orange_light));
            } else if (node.getDepth() == 1) {
                textView.setTextColor(getColor(android.R.color.holo_blue_light));
            } else if (node.getDepth() == 2) {
                textView.setTextColor(getColor(android.R.color.holo_green_light));
            } else if (node.getDepth() == 3) {
                textView.setTextColor(getColor(android.R.color.holo_red_light));
            }
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

class TextNode implements Selectable, Expandable {

    private final long id;
    private boolean expanded = true;
    private boolean selected = false;

    public TextNode() {
        this.id = SystemClock.elapsedRealtimeNanos();
    }

    public long getId() {
        return id;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + id;
    }

    @Override
    public boolean canExpand() {
        return true;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpand(boolean expand) {
        this.expanded = expand;
    }

    @Override
    public boolean canSelect() {
        return id % 2 == 0;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelect(boolean select) {
        this.selected = select;
    }
}









