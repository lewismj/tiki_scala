# tiki

## Summary
<p align="left">
<img src="https://travis-ci.org/lewismj/tiki.svg?branch=master"/>
<a href="https://www.codacy.com/app/lewismj/tiki?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lewismj/tiki&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/eb7241d325fa432c982487c412f910cb"/></a>
</p>

## Summary

A library for functional graph algorithms.

The goal is to implement standard set of graph algorithms (e.g. Bellman-Ford, Kruskal's etc.) in a clear, concise and simple fashion.

### Issues

Waffle [board](https://waffle.io/lewismj/tiki).

### Documentation

Tiki information and documentation is available on the [website](https://lewismj.github.io/tiki/).

Scaladoc [index](https://lewismj.github.io/tiki/api/tiki/index.html).



### Algorithms

### Basic Graph Algorithms

Depth First and Breadth First Search, using `unfold`.

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.dfs.png)

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.bfs.png)


Topological sort using __Kahn's algorithm__.

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.topologicalSort.png)


Negative cycle detection using __Bellman-Ford__.

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.negativeCycle.png)


Minimum spanning tree using __Kruskal's algorithm__.

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.minimumSpanningTree.png)


Strongly connected components using __Kosaraju's algorithm__.

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.scc.png)


### Cluster algorithms for graphs

Point space triangulation (__Delaunay triangulation__).

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.triangulation.png)

Euclidean distance minimum spanning tree

![graph](https://raw.github.com/lewismj/tiki/master/docs/src/main/resources/microsite/img/thumb.emst.png)


### Todo

- K-Means clustering

### References
- _The Under-Appreciated Unfold_ [1](http://www.cs.ox.ac.uk/people/jeremy.gibbons/publications/unfold.ps.gz).
- _Zippers and Data Type Derivaties_ [2](https://www21.in.tum.de/teaching/fp/SS15/papers/11.pdf)
