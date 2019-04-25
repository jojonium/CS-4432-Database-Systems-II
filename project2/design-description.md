# Project 2 Report
Griffin Bishop, Joseph Petitti

## Installation Instructions

1. Extract the ExtendedDBSimple.zip file 
2. Open Eclipse and import the unzipped directory as a new project
3. To run the server normally, create a new configuration on simpledb.server.Startup.java with argument "cs4432DB"
4. To run our tests, run CreateTestTablesOnline.java


## Extensible Hash Index Design

The most challenging part of task 2 was correctly writing the algorithm to split
the buckets when they are full. This algorithm is relatively complex, and we
encountered several bugs while writing and testing it. Buckets are individual
objects, stored in their own files. The Extensible Hash Index class implements
Index, and all of its methods were originally based off of the existing Hash
Index class. insert() and beforeFirst() were modified the most from HashIndex.
The specifics of the ExtensibleHashIndex were based on the [Wikipedia's
article](https://en.wikipedia.org/wiki/Extendible_hashing). To insert a new
record, the key column is hashed to find the appropriate bucket. If that bucket
is full, it is split evenly into two separate buckets, and the directory
pointers are updated. If the directory depth is the same as the local depth of
the buckets, the directory also needs to be split. It is doubled in size, with
duplicate values pointing to each bucket. This way, if new buckets are added in
the future there will be room for them in the directory.
