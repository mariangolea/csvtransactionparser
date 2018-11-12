package com.mariangolea.fintracker.banks.csvparser.api.preferences;

import java.util.Collection;

public interface CompanyNamesInterface {

    public Collection<String> getAllCompanyIdentifierStrings();

    public Collection<String> getCompanyIdentifierStrings(final String companyDisplayName);

    public Collection<String> getMatchingIdentifierStrings(final String transactionDescription);

    public String getCompanyDisplayName(final String companyIdentifier);

    public Collection<String> getCompanyDisplayNames();

    public void deleteCompanyName(final String company);

    public void editCompanyName(final String existingName, final String newName);

    public void editCompanyIdentifier(final String existingIdentifier, final String newIdentifier);

    public void resetCompanyIdentifierStrings(final String displayname, final Collection<String> newIdentifiers);

    public void applyChanges(final UserPreferencesInterface userEdited);

    public boolean hasCompanyDisplayName(final String companyDisplayName);

}
