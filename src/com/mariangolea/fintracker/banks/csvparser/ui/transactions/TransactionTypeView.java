package com.mariangolea.fintracker.banks.csvparser.ui.transactions;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.ui.BankTransactionGroupContextMenu;
import com.mariangolea.fintracker.banks.csvparser.ui.TransactionGroupListSelectionListener;
import com.mariangolea.fintracker.banks.csvparser.ui.edit.BankTransactionGroupEditHandler;
import com.mariangolea.fintracker.banks.csvparser.ui.renderer.TransactionGroupCellRenderer;
import com.mariangolea.fintracker.banks.csvparser.ui.transactions.FilterableTreeView.FilterableTreeItem;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class TransactionTypeView extends GridPane {

    private Label responseLabel;
    private final BankTransaction.Type type;
    protected final ObservableList<BankTransactionGroupInterface> model;
    FilterableTreeView treeView;
    private final BankTransactionGroupEditHandler editHandler = new BankTransactionGroupEditHandler();
    private final BankTransactionGroupContextMenu contextMenu = new BankTransactionGroupContextMenu(editHandler);

    public TransactionTypeView(BankTransaction.Type type, ObservableList<BankTransactionGroupInterface> model) {
        this.type = type;
        this.model = model;
        constructView();
    }

    protected final void constructView() {
        responseLabel = new Label(TransactionGroupListSelectionListener.LABEL_NOTHING_SELECTED);

        TextField search = new TextField();
        search.setPromptText("Enter company name to filter on");
        search.textProperty().addListener(text -> {
            String filter = search.getText();
            if (filter == null || filter.length() == 0) {
                treeView.getFilterableRoot().predicateProperty().setValue(s -> true);
            } else {
                treeView.getFilterableRoot().predicateProperty().setValue(s -> s.getUserDefinedCategory() != null && s.getUserDefinedCategory().toLowerCase().contains(filter.toLowerCase()));
            }
            treeView.getFilterableRoot().getChildren().clear();
            treeView.getFilterableRoot().getChildren().addAll(treeView.getFilterableRoot().getFilteredChildren());
        });

        treeView = constructTreeView();

        this.add(responseLabel, 0, 0, 3, 1);
        this.add(search, 0, 1, 1, 1);
        this.add(treeView, 0, 2, 3, 20);
    }

    protected final FilterableTreeView constructTreeView() {
        FilterableTreeItem root = new FilterableTreeItem(new BankTransactionCompanyGroup("fake", "fake", BankTransaction.Type.IN));
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

        return treeView;
    }

    public void updateTreeModel() {
        FilterableTreeItem root = treeView.getFilterableRoot();
        root.getChildren().clear();
        model.forEach((group) -> {
            root.getChildren().add(new FilterableTreeItem(group));
        });
        treeView.refresh();
    }
}
