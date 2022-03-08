package ltd.dolink.arch.treenode;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SimpleTreeNode<T extends Expandable & Selectable> implements TreeNode<T> {
    @IntRange(from = 0)
    private final int depth;
    @NonNull
    private final T data;
    private final TreeNode<?> parent;
    private final List<TreeNode<?>> children;

    public SimpleTreeNode(@IntRange(from = 0) int depth, @NonNull T data, TreeNode<?> parent) {
        Objects.requireNonNull(data);
        this.depth = depth;
        this.data = data;
        this.parent = parent;
        if (Objects.isNull(parent)) {
            if (depth < 0) {
                throw new IllegalArgumentException(String.format("depth must >= 0, current dept %s:", depth));
            }
        } else {
            if (depth <= parent.getDepth()) {
                throw new IllegalArgumentException(String.format("dept must < parent dept, current dept:%s, parent dept:%s", depth, parent.getDepth()));
            }
        }
        this.children = new LinkedList<>();
    }

    @Override
    public boolean canExpand() {
        return getData().canExpand();
    }

    @Override
    public boolean isExpanded() {
        return getData().isExpanded();
    }

    @Override
    public void setExpand(boolean expand) {
        getData().setExpand(expand);
    }

    @Override
    public boolean canSelect() {
        return getData().canSelect();
    }

    @Override
    public boolean isSelected() {
        return getData().isSelected();
    }

    @Override
    public void setSelect(boolean select) {
        getData().setSelect(select);
    }

    @IntRange(from = 0)
    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    @Override
    public TreeNode<?> getParent() {
        return parent;
    }

    @Override
    public List<TreeNode<?>> getChildren() {
        return children;
    }

    @NonNull
    @Override
    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + getData();
    }
}
