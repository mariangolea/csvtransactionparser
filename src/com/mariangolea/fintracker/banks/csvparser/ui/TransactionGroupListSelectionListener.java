package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.ui.transactions.FilterableTreeView;
import com.mariangolea.fintracker.banks.csvparser.ui.transactions.TransactionTypeView;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 * Updates amount based on existing selections. If none are made, all
 * transactions are summed up.
 *
 * @author mariangolea@gmail.com
 */
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
            int size = treeView.getFilterableRoot().getFilteredChildren().size();
            for (int i = 0; i < size; i++) {
                amount = amount.add(treeView.getFilterableRoot().getFilteredChildren().get(i).getTotalAmount());
            }
            amountArea.setText(LABEL_NOTHING_SELECTED + amount);
        } else {
            for (int index : selectedIndices) {
                amount = amount.add(treeView.getFilterableRoot().getFilteredChildren().get(index).getTotalAmount());
            }
            amountArea.setText(LABEL_SOMETHING_SELECTED + amount);
        }
        
    }
}
