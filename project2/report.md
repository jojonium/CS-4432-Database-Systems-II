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
article](https://en.wikipedia.org/wiki/Extendible_hashing).

## Extensible Hash Index Testing
To test our implementation of the extensible hash, we tested both on the createStudentDB files contained
in the SimpleDB distribution, and also created our own files. The testing output can be found in the file named 
"Extensible_hash_testing_scenario", and be produced by running CreateTestTablesOnline.java (after running Startup).

We also do the timing experiment in the CreateTestTablesOnline2.java file.
