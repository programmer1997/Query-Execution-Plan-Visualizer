# CZ4031-DatabaseSystemPrinciples

## Relational Schema


### Author Table
AID(primary), Name

### Publication table
PUBID (primary), pub key, title, year, mdate


### Author_Publication (many to many relationship)
PUBID, AID (both primary)



ER Approach for subclasses

### Incollection
PUBID, book title

### Inproceedings
PUBID, book title

###Article
PUBID Journal
