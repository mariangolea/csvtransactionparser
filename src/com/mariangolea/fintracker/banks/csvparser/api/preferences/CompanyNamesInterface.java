package com.mariangolea.fintracker.banks.csvparser.api.preferences;

import java.util.Collection;

public interface CompanyNamesInterface {

    public Collection<String> getAllCompanyIdentifierStrings();

    public void setCompanyDisplayName(final String company, final String displayName);

    public Collection<String> getCompanyIdentifierStrings(final String companyDisplayName);

    public void deleteCompanyName(final String company);

    public String getCompanyDisplayName(final String company);

    public Collection<String> getCompanyDisplayNames();

    public void editCompanyName(final String existingName, final String newName);

    public void editCompanyIdentifier(final String existingIdentifier, final String newIdentifier);

    public void resetCompanyIdentifierStrings(final String companyName, final Collection<String> newIdentifiers);

    public void deleteCompanyIdentifier(final String existingIdentifier);

    public void applyChanges(final UserPreferencesInterface userEdited);

}
