package com.mariangolea.fintracker.banks.csvparser.ui.transactions;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.ui.BankTransactionGroupContextMenu;
import com.mariangolea.fintracker.banks.csvparser.ui.TransactionGroupListSelectionListener;
import com.mariangolea.fintracker.banks.csvparser.ui.edit.BankTransactionGroupEditHandler;
import com.mariangolea.fintracker.banks.csvparser.ui.renderer.TransactionGroupCellRenderer;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class TransactionTreeView extends GridPane {

    private Label responseLabel;
    protected final ObservableList<BankTransactionGroupInterface> model;
    FilterableTreeView treeView;
    private final BankTransactionGroupEditHandler editHandler = new BankTransactionGroupEditHandler();
    private final BankTransactionGroupContextMenu contextMenu = new BankTransactionGroupContextMenu(editHandler);
    private TextField searchField;

    public TransactionTreeView(ObservableList<BankTransactionGroupInterface> model) {
        this.model = model;
        constructView();
    }

    protected final void constructView() {
        responseLabel = new Label(TransactionGroupListSelectionListener.LABEL_NOTHING_SELECTED);

        searchField = new TextField();
        searchField.setPromptText("Enter company name to filter on");
        treeView = constructTreeView();

        this.add(responseLabel, 0, 0, 3, 1);
        this.add(searchField, 0, 1, 1, 1);
        this.add(treeView, 0, 2, 3, 20);
    }

    protected final FilterableTreeView constructTreeView() {
        FilterableTreeItem root = new FilterableTreeItem(new BankTransactionCompanyGroup("fake"));
        treeView = new FilterableTreeView(root);
        treeView.getSelectionModel().selectedItemProperty().addListener(new TransactionGroupListSelectionListener(treeView, responseLabel));
        treeView.setEditable(true);
        treeView.setCellFactory(param -> {
            TreeCell<BankTransactionGroupInterface> cell = new TransactionGroupCellRenderer();
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                    contextMenu.setBankTransactionGroup(cell.getItem());
                }
            });
            cell.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        editHandler.editGroup(cell.getItem());
                    }
                }
            });
            return new TransactionGroupCellRenderer();
        });
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getFilterableRoot().predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (searchField.getText() == null || searchField.getText().isEmpty()) {
                return null;
            }
            return TreeItemPredicate.create(group -> group.toString().toLowerCase().contains(searchField.getText().toLowerCase()));
        }, searchField.textProperty()));

        return treeView;
    }

    public void updateTreeModel() {
        FilterableTreeItem root = treeView.getFilterableRoot();
        root.getInternalChildren().clear();
        model.forEach((group) -> {
            root.getInternalChildren().add(new FilterableTreeItem(group));
        });
        treeView.refresh();
    }
}
