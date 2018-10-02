/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.api.transaction;

/**
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class UserDefinedBankTransactionCompanyGroup extends BankTransactionCompanyGroup{
    
    public UserDefinedBankTransactionCompanyGroup(String transactionGroupIdentifier, String userDefinedGroupName, BankTransaction.Type type) {
        super(transactionGroupIdentifier, userDefinedGroupName, type);
    }
    
}
