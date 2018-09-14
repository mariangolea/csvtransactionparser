package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The process of parsing a set of CSV report files produces a list of
 * {@link BankTransactionDefaultGroup} objects.
 * <br> This class offers support for decomposing, where possible, these large
 * groups into more specific ones (like for example into groups of transactions
 * belonging to a single company or entity).
 *
 * @author mariangolea@gmail.com
 */
public class BankTransactionUtils {

    /**
     * Process a parse operation results into more specific transaction groups.
     * <br> Each argument is inspected for supported company descriptors, and a
     * new more specific group is created based on that, each containing
     * transactions from multiple input arguments.
     *
     * @param list parse resulted groups.
     * @return
     */
    public List<BankTransactionAbstractGroup> processTransactions(List<BankTransactionDefaultGroup> list) {
        List<BankTransactionAbstractGroup> results = new ArrayList<>();
        if (list == null) {
            return results;
        }

        Map<String, BankTransactionCompanyGroup> companies = new HashMap<>();
        list.forEach((group) -> {
            List<String> companyDescriptions = group.getCompanyDescriptions();
            companyDescriptions.forEach((companyDesc) -> {
                BankTransactionCompanyGroup companyGroup = companies.get(companyDesc);
                if (companyGroup == null) {
                    companyGroup = new BankTransactionCompanyGroup(group.getGroupIdentifier(), companyDesc, group.getType());
                    companies.put(companyDesc, companyGroup);
                }
                companyGroup.addTransactions(group.getTransactionsForCompanyDesc(companyDesc));
            });
        });
        results.addAll(companies.values());
        return results;
    }
}
