package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single;

import java.util.Collection;
import java.util.Objects;

public class EditCompanyNameResult {
    public final String companyDisplayName;
    public final Collection<String> identifierStrings;

    public EditCompanyNameResult(final String companyName, final Collection<String> identifierStrings) {
        this.companyDisplayName = companyName;
        this.identifierStrings = Objects.requireNonNull(identifierStrings);
    }
}
