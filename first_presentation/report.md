# Peaceland

## Preliminary questions

### 1) What technical/business constraints should the data storage component of the program architecture meet to fulfill the requirement described by the customer in paragraph «Statistics» ?

Statistics are computed based on information that comes from everywhere in the country, which implies that the database needs to be distributed and partition tolerant. Also, it needs to comprehend every information gathered so far to be meaningful and avoid biases, hence it also needs to be consistent. We thus need a CP database.

SQL cannot be a solution, as it relies on a master-slave system and is thus not fully partition tolerant. We need a NoSQL database.

Additionally, statistics often require large data aggregation, and so we should favor colum-oriented solutions.

Taking these points into account, we chose to use Apache HBase, for it is a column-oriented NoSQL CP datastorage solution.