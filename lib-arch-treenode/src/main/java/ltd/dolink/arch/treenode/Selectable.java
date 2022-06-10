package ltd.dolink.arch.treenode;

public interface Selectable {
  boolean canSelect();

  boolean isSelected();

  void setSelect(boolean select);
}
