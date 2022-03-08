package ltd.dolink.arch.treenode;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface TreeNode<T> extends Expandable, Selectable {
    @IntRange(from = 0)
    int getDepth();

    int getChildCount();

    TreeNode<?> getParent();

    List<TreeNode<?>> getChildren();

    T getData();

    default void expandToList(boolean expand, @NonNull Consumer<List<TreeNode<?>>> action) {
        Objects.requireNonNull(action);
        List<TreeNode<?>> result = new LinkedList<>();
        expand(expand, child -> result.add(child));
        action.accept(result);
    }

    default void expand(boolean expand, @NonNull Consumer<TreeNode<?>> action) {
        Objects.requireNonNull(action);
        forEach(this, expand, Expandable::isExpanded, action);
    }

    default void forEach(@NonNull TreeNode<?> node, boolean expand, @NonNull Predicate<TreeNode<?>> predicate, @NonNull Consumer<TreeNode<?>> action) {
        Objects.requireNonNull(node);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);
        List<TreeNode<?>> children = node.getChildren();
        if (!children.isEmpty()) {
            for (TreeNode<?> child : children) {
                if (expand) {
                    action.accept(child);
                }
                if (predicate.test(child)) {
                    forEach(child, expand, predicate, action);
                }
                if (!expand) {
                    action.accept(child);
                }
            }
        }
    }

}
