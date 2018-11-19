package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import java.util.Objects;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import javafx.util.Pair;

public class CategoriesTreeCellFactory implements Callback<TreeView<Pair<String, Boolean>>, TreeCell<Pair<String, Boolean>>> {

    private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");
    private static final String DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3";
    private TreeCell<Pair<String, Boolean>> dropZone;
    private TreeItem<Pair<String, Boolean>> draggedItem;
    private final UserPreferencesInterface prefs;
    
    public CategoriesTreeCellFactory(final UserPreferencesInterface prefs){
        this.prefs = Objects.requireNonNull(prefs);
    }

    @Override
    public TreeCell<Pair<String, Boolean>> call(final TreeView<Pair<String, Boolean>> treeView) {
        TreeCell<Pair<String, Boolean>> treeCell = new TreeCell<Pair<String, Boolean>>() {
            @Override
            protected void updateItem(Pair<String, Boolean> item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.getKey());
                    setGraphic(getTreeItem().getGraphic());
                    setStyle(item.getValue() ? "-fx-background-color: mediumorchid;" : null);
                } else {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                }
            }
        };

        treeCell.setOnDragDetected(event -> dragDetected(event, treeCell, treeView));
        treeCell.setOnDragOver(event -> dragOver(event, treeCell, treeView));
        treeCell.setOnDragDropped(event -> dragDropped(event, treeCell, treeView));
        treeCell.setOnDragDone(event -> clearDropLocation());
        return treeCell;
    }

    protected void dragDetected(MouseEvent event, TreeCell<Pair<String, Boolean>> treeCell, TreeView<Pair<String, Boolean>> treeView) {
        draggedItem = treeCell.getTreeItem();

        // root can't be dragged
        if (draggedItem.getParent() == null) {
            return;
        }
        Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(JAVA_FORMAT, draggedItem.getValue());
        db.setContent(content);
        db.setDragView(treeCell.snapshot(null, null));
        event.consume();
    }

    protected void dragOver(DragEvent event, TreeCell<Pair<String, Boolean>> treeCell, TreeView<Pair<String, Boolean>> treeView) {
        if (!event.getDragboard().hasContent(JAVA_FORMAT)) {
            return;
        }
        TreeItem<Pair<String, Boolean>> thisItem = treeCell.getTreeItem();

        if(thisItem == null || thisItem.getValue().getValue()){
            //company names cannot have children.
            return;
        }
        
        // can't drop on itself
        if (draggedItem == null || thisItem == draggedItem) {
            return;
        }
        // ignore if this is the root
        if (draggedItem.getParent() == null) {
            clearDropLocation();
            return;
        }

        event.acceptTransferModes(TransferMode.MOVE);
        if (!Objects.equals(dropZone, treeCell)) {
            clearDropLocation();
            this.dropZone = treeCell;
            dropZone.setStyle(DROP_HINT_STYLE);
        }
    }

    private void dragDropped(DragEvent event, TreeCell<Pair<String, Boolean>> treeCell, TreeView<Pair<String, Boolean>> treeView) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (!db.hasContent(JAVA_FORMAT)) {
            return;
        }

        TreeItem thisItem = treeCell.getTreeItem();
        TreeItem droppedItemParent = draggedItem.getParent();

        // remove from previous location
        droppedItemParent.getChildren().remove(draggedItem);

        // dropping on parent node makes it the first child
        if (Objects.equals(droppedItemParent, thisItem)) {
            thisItem.getChildren().add(0, draggedItem);
            treeView.getSelectionModel().select(draggedItem);
        } else {
            // add to new location
            int indexInParent = thisItem.getParent().getChildren().indexOf(thisItem);
            thisItem.getParent().getChildren().add(indexInParent + 1, draggedItem);
        }
        treeView.getSelectionModel().select(draggedItem);
        event.setDropCompleted(success);
    }

    private void clearDropLocation() {
        if (dropZone != null) {
            dropZone.setStyle("");
        }
    }
}
