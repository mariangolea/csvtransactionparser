# pdftransactionparser
Desktop app allowing users to read bank pdf transaction report files and store on their drive only.

If you are trying to keep track of a simple family budget AND you make most of your payments and receive most of your income using a bank account, you are probably used to generating PDF transaction reports, or using a third party app (spread sheets) to manually add different types of spending categories.

This APP minimizes the time spent doing that to 1-2 minutes. Users of the application only need to generate the PDF transaction files on their own, even once per year. Application knows how to parse these files, categorize transactions automatically, and eventually show basic reports. If certain PDF files are password protected, the application will require you to feed that password every time, and will NEVER store the password.

It also allows further grouping of transactions into higher categories (like for example you may want to create a group "Existential" where utilities,
education and others will be grouped. Once a customization is done, it is stored by the app for future runs).

This application is solely for desktop and OFFLINE use:
1. No data is transfered online at any point in time.
2. Users can opt in for a local "account", meaning that the password they can define will be used to encrypt the persistence of their own data. Default behaviour does not require it though.
3. Due to the low amount of transactions (family, not small company needs), data layer does not require anything fancy. Data will be stored on a set of files on disk.
