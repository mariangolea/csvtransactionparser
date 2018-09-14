[![CircleCI](https://circleci.com/gh/mariangolea/csvtransactionparser/tree/master.svg?style=svg)](https://circleci.com/gh/mariangolea/csvtransactionparser/tree/master)
[![SonarCloud](https://sonarcloud.io/api/project_badges/quality_gate?project=BankCsvParser%3ABankCsvParser)](https://sonarcloud.io/dashboard?id=BankCsvParser%3ABankCsvParser)
[![JavaDoc](/docs/javadoc.png)](https://mariangolea.github.io/csvtransactionparser/)


# csvtransactionparser
Desktop app allowing users to read bank csv transaction report files and store on their drive only.

If you are trying to keep track of a simple family budget AND you make most of your payments and receive most of your income using a bank account, you are probably used to generating CSV transaction reports, or using a third party app (spread sheets) to manually add different types of spending categories.

This APP minimizes the time spent doing that to 1-2 minutes. Users of the application only need to generate the CSV transaction files on their own, even once per year. Application knows how to parse these files, categorize transactions automatically, and show basic reports.

It also allows further grouping of transactions into higher categories (like for example you may want to create a group "Existential" where utilities,
education and others will be grouped. Once a customization is done, it is stored by the app for future runs).

This application is solely for desktop and OFFLINE use:
1. No data is transfered online at any point in time.
2. Users can opt in for a local "account", meaning that the password they can define will be used to encrypt the persistence of their own data. Default behaviour does not require it though.
3. Due to the low amount of transactions (family, not small company needs), data layer does not require anything fancy. Data will be stored on a set of files on disk.
