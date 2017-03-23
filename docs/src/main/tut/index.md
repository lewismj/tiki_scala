---
layout: home
title: "Home"
section: "home"
---

tiki is a library for functional graph algorithms. It is a new project, currently under development
(pre-alpha).


## Introduction

A graph is simply the combination of vertices (or nodes) and edges connecting pairs of vertices.
To compute things about graphs, we need to represent them. There are different ways to do
this. 

_Different operations are faster in different representations._ 

The choice of representation can be based on factors such as the density (number of edges 
in terms of number of vertices).

The traversal (a generic `unfold`) is independent of the graph representation.

It may be useful to have a generic `Graph` object that holds a Rose-tree, but it can be useful
to have other implementations and swap out the underlying data structure.

## Further Reading

1. Koh Khee Meng et al., _Introduction to Graph Theory, H3 Mathematics_ (World Scientific, 2013)
2. Harary, _A seminar on graph theory_ (Dover, 2015)
