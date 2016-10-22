# COMP3421 Assignment 2

This assignment was done by Thomas Moffet (z5061905) and Ben Phipps (z5019713). We have completed the sections below.

Several parts of the code utilised parts of lecture code and brief resources off the internet. These are referenced and cited where used.

The Github repository for this assignment can be found [here](https://github.com/Biendeo/COMP3421-Assignment-2).

### Controls
- **WASD** - Move forward, backwards, and strafe side to side without turning (the FPS control scheme)
- **Arrow keys** - Move forward, backwards, and turns left and right (the specified control scheme (tank controls))
- **Space** - Switches the light to a point light
- **TG** - Looks up or down
- **RF** - Flies directly up or down (noclip only)
- **V** - Toggles noclip mode (while in noclip, moving forward and backwards depends on the viewing angle)
- **M** - Toggles mouselook for turning left and right, and looking up and down; mouselook does not lock to the window, but the sensitivity should be enough for now
- **,** *(comma)* - Toggles third person mode
- **ESCAPE** - Quits the program
- **;** *(semicolon)* - Displays the light source as a model
- **IJKLOP** - Modifies the orientation or position of the light source (depending if it's a directional or point light)

### Features Implemented
*Ticked elements were completed, and respective classes are listed*

** REQUIRED **
- [✔] **Terrain - mesh generation** *(Terrain)*
- [✔] **Terrain - interpolating values** *(Terrain)*
- [✔] **Trees - mesh generation (part of L-system extension)** *(Tree, LSystemTrees)*
- [✔] **Road - mesh generation** *(Road)*
- [✔] **Camera - perspective camera** *(Camera)*
- [✔] **Camera - movement** *(Camera, PlayerController)*
- [✔] **Lighting** *(Light, LightType)*
- [✔] **Textures** *(Terrain, Monster, Road)*
- [✔] **Avatar** *(PlayerController)*
- [✔] **The Others** *(Monster)*

** EXTENSION **
- [✘] Complex model
- [✘] Night mode
- [✘] Moving sun
- [✘] Rain particle effects
- [✘] Ponds
- [✔] **L-system fractal trees** *(LSystemTrees)*
- [✘] Road extrusion
- [✘] Shadows to trees and terrain
- [✔] **Normal mapping shaders** *(Monster)*
- [✘] NPR shading
- [✘] BSP trees for terrain
- [✘] LOD support
- [?] **Portals - walk-through** *(Portal)*
- [?] **Portals - see-through** *(Portal)*
- [✘] Bezier / NURBS terrain

*Note*: The portals component was attempted but was not completed to a demonstratable level, so most of it is commented out.

*Note*: (Thomas): I couldn't quite figure out how to set up index buffers properly, so I've purposefully repeated vertices on the terrain and monster buffers. It achieves the same goal, but of course is not as memory efficient.