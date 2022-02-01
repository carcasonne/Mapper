# Mapper

This project was developed as part of the 2nd semester, first year, project at the IT University of Copenhagen during the spring of 2021. 
This project was developed in a group with 4 other developers, and I do not claim sole ownership.

# Features

Mapper is a java program which is able  to visualize OpenStreetMaps data in the form of .OSM files. 

- Visualize maps with regards to oceans, islands, motorways, urban areas, and much more.
- Only visualize the data points visible to the user (implemented by a KD-tree)
- Route navigation between any two connected points on the map (implemented by Dijkstra A* search)
- Route navigation with different weights (driving, walking, biking)
- Set markers for your favorite locations, and use them in route navigation
- Route navigation between specific addresses
- You can at any time load a new file without having to close the program
