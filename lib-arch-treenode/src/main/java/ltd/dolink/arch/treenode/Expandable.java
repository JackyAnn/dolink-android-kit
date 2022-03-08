package ltd.dolink.arch.treenode;

public interface Expandable {
    boolean canExpand();

    boolean isExpanded();

    void setExpand(boolean expand);
}
