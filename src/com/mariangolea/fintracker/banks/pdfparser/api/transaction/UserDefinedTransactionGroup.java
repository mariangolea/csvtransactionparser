package com.mariangolea.fintracker.banks.pdfparser.api.transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Container of user defined associations between transaction groups. <br>
 * This is the highest level of containment. Groups from different banks can be
 * joined together under a common name which only has meaning for the user.
 * 
 * @author Marian Golea <mariangolea@gmail.com>
 *
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
		final int prime = 31;
		int result = 1;
		result = prime * result + groupName.hashCode();
		result = prime * result + transactionGroupAssociations.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDefinedTransactionGroup other = (UserDefinedTransactionGroup) obj;
		if (!groupName.equals(other.groupName))
			return false;
		return transactionGroupAssociations.equals(other.transactionGroupAssociations);
	}

}
