# tiki
<p align="left">
<img src="https://travis-ci.org/lewismj/tiki.svg?branch=master"/>
<a href="https://codecov.io/gh/lewismj/tiki"><img src="https://codecov.io/gh/lewismj/tiki/branch/master/graph/badge.svg" alt="Codecov"/></a>
<a href="https://www.codacy.com/app/lewismj/tiki?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lewismj/tiki&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/eb7241d325fa432c982487c412f910cb"/></a>
<img src="https://img.shields.io/waffle/label/lewismj/tiki/master.svg"/>
</p>


## Summary
A library for functional graph algorithms (_soon_)

Graph algorithms work well on simple data structures such as `DisjointSet` and `AdjacencyList`. 
The library aims to implement the core abstractions and algorithms in a simple, clear and functional way.

### Documentation

Tiki information and documentation is available on the [website](https://lewismj.github.io/tiki/).

### Introduction

A graph is simply the combination of:

- Vertices (or nodes).
- Edges connecting pairs of vertices.

To compute things about graphs, we need to represent them. There are different ways to do
this. 

_Different operations are faster in different representations._ 

Therefore the library aims to provide fundamental representations of graphs, rather than a fixed representation,
building algorithms around the core data structures.

### Further Reading

1. Koh Khee Meng et al., _Introduction to Graph Theory, H3 Mathematics_ (World Scientific, 2013)
2. Harary, _A seminar on graph theory_ (Dover, 2015)

