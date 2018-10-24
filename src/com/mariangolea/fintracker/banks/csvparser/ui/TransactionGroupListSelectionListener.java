package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.ui.transactions.FilterableTreeView;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;

public class TransactionGroupListSelectionListener implements ChangeListener {

    public static final String LABEL_NOTHING_SELECTED = "Total Amount: ";
    public static final String LABEL_SOMETHING_SELECTED = "Selected Amount: ";

    private final FilterableTreeView treeView;
    private final Label amountArea;

    public TransactionGroupListSelectionListener(FilterableTreeView treeView, Label amountArea) {
        this.treeView = treeView;
        this.amountArea = amountArea;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        ObservableList<Integer> selectedIndices = treeView.getSelectionModel().getSelectedIndices();
        BigDecimal amount = BigDecimal.ZERO;
        if (selectedIndices == null || selectedIndices.size() < 1) {
            int size = treeView.getFilterableRoot().getInternalChildren().size();
            for (int i = 0; i < size; i++) {
                amount = amount.add(getTotalAmount(treeView.getTreeItem(i)));
            }
            amountArea.setText(LABEL_NOTHING_SELECTED + amount);
        } else {
            for (int i : selectedIndices) {
                amount = amount.add(getTotalAmount(treeView.getTreeItem(i)));
            }
            amountArea.setText(LABEL_SOMETHING_SELECTED + amount);
        }

    }

    private BigDecimal getTotalAmount(FilterableTreeItem<BankTransactionGroupInterface> item) {
        if (item.getInternalChildren() == null || item.getInternalChildren().isEmpty()) {
            return item.getValue() == null ? BigDecimal.ZERO : item.getValue().getTotalAmount();
        } else {
            BigDecimal amount = BigDecimal.ZERO;
            for (TreeItem<BankTransactionGroupInterface> child : item.getInternalChildren()){
                BankTransactionGroupInterface value = child.getValue();
                if (value != null) {
                    amount = amount.add(child.getValue().getTotalAmount());
                }
            }

            return amount;
        }
    }
}
