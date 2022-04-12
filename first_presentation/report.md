# Peaceland

## Preliminary questions

### 1) What technical/business constraints should the data storage component of the program architecture meet to fulfill the requirement described by the customer in paragraph «Statistics» ?

Statistics are computed based on information that comes from everywhere in the country, which implies that the database needs to be distributed and partition tolerant. Also, it needs to comprehend every information gathered so far to be meaningful and avoid biases, hence it also needs to be consistent. We thus need a CP database.

SQL cannot be a solution, as it relies on a master-slave system and is thus not fully partition tolerant. We need a NoSQL database.

Additionally, statistics often require large data aggregation, and so we should favor colum-oriented solutions.

Taking these points into account, we chose to use Apache HBase, for it is a column-oriented NoSQL CP datastorage solution.

### 2) What business constraint should the architecture meet to fulfill the requirement describe in the paragraph «Alert»? Which component to choose?

The business constraint described in the paragraph "Alert" is that of time. It is mandatory that an agitated individual is "handled" as soon as possible to avoid the spread of their agitation. This constraint has two implications on the design:

* Upon receiving the initial report from peacewatchers, the gateway must immediately send an alert to the peacemakers;
* To allow peacewatchers to do their job as quickly as possible, they need to be able to fetch data about the individuals surrounding them at any time. It means that we need an AP datastorage to handle the data needed for day to day operations. Here, we chose to use Scylla, a column-oriented and fast database;
