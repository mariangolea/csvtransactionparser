package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows merging various bank transaction groups.
 *
 * @author mariangolea@gmail.com
 */
public class BankTransactionUtils {

    public List<BankTransactionAbstractGroup> processTransactions(List<BankTransactionDefaultGroup> list) {
        List<BankTransactionAbstractGroup> results = new ArrayList<>();
        Map<String, BankTransactionCompanyGroup> companies = new HashMap<>();
        for (BankTransactionDefaultGroup group : list) {
            List<String> companyDescriptions = group.getCompanyDescriptions();
            for (String companyDesc : companyDescriptions){
                BankTransactionCompanyGroup companyGroup = companies.get(companyDesc);
                if (companyGroup == null){
                    companyGroup = new BankTransactionCompanyGroup(group.getGroupIdentifier(), companyDesc, group.getType());
                    companies.put(companyDesc, companyGroup);
                }
                companyGroup.addTransactions(group.getTransactionsForCompanyDesc(companyDesc));
            }
        }
        results.addAll(companies.values());
        return results;
    }
}
