# tiki

## Summary
<p align="left">
<img src="https://travis-ci.org/lewismj/tiki.svg?branch=master"/>
<a href="https://codecov.io/gh/lewismj/tiki"><img src="https://codecov.io/gh/lewismj/tiki/branch/master/graph/badge.svg" alt="Codecov"/></a>
<a href="https://www.codacy.com/app/lewismj/tiki?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lewismj/tiki&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/eb7241d325fa432c982487c412f910cb"/></a>
<a href="https://waffle.io/lewismj/tiki"><img src="https://img.shields.io/waffle/label/lewismj/tiki/master.svg"/></a>
</p>

## Summary

A library for functional graph algorithms.

The goal is to implement standard set of graph algorithms (e.g. Bellman-Ford, Kruskal's etc.) in a clear, concise and simple fashion.


### Tree

A different approach is to base graph algorithms on a multiway tree representation. The Multiway tree will be a minimal (and _rough_) copy of Haskell's [Data.Tree](http://hackage.haskell.org/package/containers-0.5.10.2/docs/Data-Tree.html).

#### Background

- _The Under-Appreciated Unfold_ [1](http://www.cs.ox.ac.uk/people/jeremy.gibbons/publications/unfold.ps.gz).

- _Zippers and Data Type Derivaties_ [2](https://www21.in.tum.de/teaching/fp/SS15/papers/11.pdf)

### Issues

Waffle [board](https://waffle.io/lewismj/tiki).

### Documentation

Tiki information and documentation is available on the [website](https://lewismj.github.io/tiki/).

Scaladoc [index](https://lewismj.github.io/tiki/api/tiki/index.html).

Tiki depends on the [shapeless](https://github.com/milessabin/shapeless) and [cats](https://github.com/typelevel/cats) libraries.

