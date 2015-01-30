# CliqueDetector
A simple clique detector of specific size for Gephi. This simple plugin calculate cliques of size k in the graph and lists them.
The algorithm is NP-Hard and it takes time for finding specific size cliques in the graph.
# Algorithm
Firstly, the algorithm removes nodes with degree lower than k-1 because these nodes can not take place iin making cliques of size k.

Secondly, the algorithm uses a queue for finding cliques of size 1 and expanding it to 2, 3, ..., k

Main reference of the algorithm is taken from Chapter 3, Section 3.1.1 of "Community Detection and Mining in Social Media" by  Lei Tang and Huan Liu:

http://dmml.asu.edu/cdm/
