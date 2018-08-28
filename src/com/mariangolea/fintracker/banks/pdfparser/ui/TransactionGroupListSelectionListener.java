package com.mariangolea.fintracker.banks.pdfparser.ui;

import com.mariangolea.fintracker.banks.pdfparser.api.BankTransactionGroup;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Updates amount based on existing selections. If none are made, all
 * transactions are summed up.
 *
 * @author mariangolea@gmail.com
 */
public class TransactionGroupListSelectionListener implements ListDataListener, ListSelectionListener {

    private static final String LABEL_NOTHING_SELECTED = "Total Amount: ";
    private static final String LABEL_SOMETHING_SELECTED = "Selected Amount: ";

    private final JList<BankTransactionGroup> jList;
    private final JTextPane amountArea;

    public TransactionGroupListSelectionListener(JList<BankTransactionGroup> jList, JTextPane amountArea) {
        this.jList = jList;
        this.amountArea = amountArea;
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        updateAmount();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        updateAmount();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        updateAmount();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        updateAmount();
    }

    private void updateAmount() {
        int[] selectedIndices = jList.getSelectedIndices();
        double amount = 0;
        if (selectedIndices == null || selectedIndices.length < 1) {
            int size = jList.getModel().getSize();
            for (int i = 0; i < size; i++) {
                amount += jList.getModel().getElementAt(i).getTotalAmount();
            }
            amountArea.setText(LABEL_NOTHING_SELECTED + amount);
        } else {
            for (int index : selectedIndices) {
                amount += jList.getModel().getElementAt(index).getTotalAmount();
            }
            amountArea.setText(LABEL_SOMETHING_SELECTED + amount);
        }

    }

}
