package com.mariangolea.fintracker.banks.csvparser.preferences;

import java.util.*;

/**
 * Container of user defined associations between transaction groups. <br>
 * This is the highest level of containment. Groups from different banks can be
 * joined together under a common name which only has meaning for the user.
 *
 * @author Marian Golea <mariangolea@gmail.com>
 */
public class UserDefinedTransactionGroup {

    public final String groupName;
    private final Map<String, Set<String>> transactionGroupAssociations = new HashMap<>();

    public UserDefinedTransactionGroup(final String groupName) {
        Objects.requireNonNull(groupName);
        this.groupName = groupName;
    }

    public void addAssociations(final String swiftCode, final Set<String> bankOperationDescriptors) {
        Objects.requireNonNull(swiftCode);
        Objects.requireNonNull(bankOperationDescriptors);

        Set<String> existing = transactionGroupAssociations.get(swiftCode);
        if (existing == null) {
            existing = bankOperationDescriptors;
            transactionGroupAssociations.put(swiftCode, existing);
        }

        existing.addAll(bankOperationDescriptors);

    }

    public void addAssociation(final String swiftCode, final String bankOperationDescriptor) {
        Objects.requireNonNull(swiftCode);
        Objects.requireNonNull(bankOperationDescriptor);

        Set<String> existing = transactionGroupAssociations.get(swiftCode);
        if (existing == null) {
            existing = new HashSet<>();
            transactionGroupAssociations.put(swiftCode, existing);
        }

        existing.add(bankOperationDescriptor);
    }

    public boolean removeAssociation(final String swiftCode) {
        if (transactionGroupAssociations.containsKey(swiftCode)) {
            transactionGroupAssociations.remove(swiftCode);
            return true;
        }
        return false;
    }

    public Set<String> getTransactionsFor(final String swiftCode) {
        return transactionGroupAssociations.get(swiftCode);
    }

    public Set<String> getSwiftCodes() {
        return transactionGroupAssociations.keySet();
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, transactionGroupAssociations);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        
        UserDefinedTransactionGroup other = (UserDefinedTransactionGroup) obj;
        return Objects.equals(groupName, other.groupName) 
                && Objects.equals(transactionGroupAssociations, other.transactionGroupAssociations);
    }

}
