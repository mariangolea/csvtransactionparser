/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.api.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Marian
 */
public final class BankTransactionCompanyGroup extends BankTransactionAbstractGroup {

    private final List<BankTransaction> list = new ArrayList<>();
    private final String companyDesc;

    public BankTransactionCompanyGroup(String transactionGroupIdentifier, String companyDesc, BankTransaction.Type type) {
        super(transactionGroupIdentifier, type);
        Objects.requireNonNull(companyDesc);

        this.companyDesc = companyDesc;
    }

    @Override
    public void addTransactionImpl(final BankTransaction transaction) {
        list.add(transaction);
    }

    @Override
    public boolean matchesTransaction(final BankTransaction transaction) {
        return super.matchesTransaction(transaction) && Objects.equals(companyDesc, transaction.getDescription());
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    @Override
    public String toString() {
        return companyDesc + "\n" + super.toString(); 
    }
    
    
}
