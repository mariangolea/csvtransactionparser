package com.mariangolea.fintracker.banks.pdfparser.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mariangolea.fintracker.banks.pdfparser.api.transaction.BankTransactionGroup;

/**
 * Allows merging various bank transaction groups.
 *
 * @author mariangolea@gmail.com
 */
public class BankTransactionMergeUtils {

    public List<BankTransactionGroup> mergeGroups(List<BankTransactionGroup> list) {
        Map<String, BankTransactionGroup> results = new HashMap<>();
        for (BankTransactionGroup group : list) {
            BankTransactionGroup existing = results.get(group.getGroupIdentifier());
            if (existing == null) {
                results.put(group.getGroupIdentifier(), group);
            } else {
                //merge groups.
                existing.addTransactions(group.getTransactions());
            }

        }

        return new ArrayList<>(results.values());
    }
}
