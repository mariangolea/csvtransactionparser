package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import java.math.BigDecimal;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Updates amount based on existing selections. If none are made, all
 * transactions are summed up.
 *
 * @author mariangolea@gmail.com
 */
public class TransactionGroupListSelectionListener implements ListChangeListener {

    public static final String LABEL_NOTHING_SELECTED = "Total Amount: ";
    public static final String LABEL_SOMETHING_SELECTED = "Selected Amount: ";

    private final ListView<BankTransactionCompanyGroup> list;
    private final Label amountArea;

    public TransactionGroupListSelectionListener(ListView<BankTransactionCompanyGroup> list, Label amountArea) {
        this.list = list;
        this.amountArea = amountArea;
    }

    @Override
    public void onChanged(Change c) {
        ObservableList<Integer> selectedIndices = list.getSelectionModel().getSelectedIndices();
        BigDecimal amount = BigDecimal.ZERO;
        if (selectedIndices == null || selectedIndices.size() < 1) {
            int size = list.getItems().size();
            for (int i = 0; i < size; i++) {
                amount = amount.add(list.getItems().get(i).getTotalAmount());
            }
            amountArea.setText(LABEL_NOTHING_SELECTED + amount);
        } else {
            for (int index : selectedIndices) {
                amount = amount.add(list.getItems().get(index).getTotalAmount());
            }
            amountArea.setText(LABEL_SOMETHING_SELECTED + amount);
        }
    }
}
