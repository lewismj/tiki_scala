---
layout: docs 
title:  "Disjoint Set"
section: "datastructures"
source: "core/src/main/scala/tiki/DisjointSet.scala"
scaladoc: "#tiki.DisjointSet"
---
# Disjoint Set

`DisjointSet` is a data structure that keeps tracks of non overlapping partitions within a set.
The data structure provides the following core functions:

- `find` this returns the "representative" (or a 'parent') of the element in the set. Thus, two find operations
are used to determine if two elements belong to the same partition.
- `union` join two partitions together via two elements.
- `components` the number of partitions.

### Usage

### Constructing a disjoint set

A Disjoint-set can be created from a standard set:

```tut
import tiki._
import tiki.implicits._
import cats.implicits._

val disjoint = DisjointSet(Set[Int](1,2))
disjoint.show
```

### find

Initially, each element will belong to its own partition:

```tut
import tiki._
import tiki.implicits._
import cats.implicits._

val ds0 = DisjointSet(Set[Int](1,2))
ds0.show
ds0.find(1)
```

### union

Union is used to merge elements of the set:

```tut
import tiki._

val ds0 = DisjointSet(Set[Int](1,2))
ds0.show

val ds1 = ds0.union(1,2).getOrElse(ds0)
ds1.show
```
