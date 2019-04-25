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
