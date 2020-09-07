# 2D-Ideal-Gas Tutorial

This is the interface of 2D Ideal Gas. It can be broken down into 4 main components.
![screenshot](https://i.imgur.com/JZA4u3W.png)

1. **The visualizer** displays the current state of the gas.
   ![visualizer](https://i.imgur.com/1VBAZaG.png)

2. **Options** control the gas. Most of them requerie a restart of the simulation.
   ![options](https://i.imgur.com/ZzyfAWT.png)
   There's a total of 6 options:

    a. **Particle Count** controls the number of particles simulated. The slider
       ranges 0 to 2500. The more particles there are, the slower the simulation
       runs.
       ![count](https://i.imgur.com/cPodjB5.png)

    b. **Big Particle Count** controls the number of big red particles simulated.
       The big particles represent dust and visualize Brownian motion. Ranges 0
       to 2500.
       ![big](https://i.imgur.com/ChNcMz4.png)

    c. **Particle Mass** controls the mass of normal particles. Ranges 0 to 20.
       A larger mass requires the simulator to scan a bigger area for collisions
       which may cause the simulation to slow down.
       ![mass](https://i.imgur.com/skd2mqO.png)

    d. **Particle Speed** controls the starting speed of particles. The slider
       ranges 0 to 600 px/s.
       ![speed](https://i.imgur.com/3rQOYMT.png)

    e. **Show Vectors** toggles vector display and **Vector Size** controls the
       scale. These 2 options *do not* require a restart to apply. May slow the
       simulation down due to having to draw twice as many elements.
       ![vectors](https://i.imgur.com/oijhLqH.png)

    f. **The Pause button** pauses or resumes the simulation and **the Restart
       button** restarts the simulation entirely, applying all changes to the options.
       ![buttons](https://i.imgur.com/qJjJuuq.png)

3. **Piston Controls** control the movement of the piston. These apply
   immediately.
   ![controls](https://i.imgur.com/GYh87Kp.png)
   There's a total of 3 piston controls:

    a. **Piston Speed** controls the movement speed of the piston. Ranges 0 to
       300 px/s.
       ![piston](https://i.imgur.com/Qupfgpw.png)

    b. **Piston Limit** sets the endpoint of piston movement. Ranges 0 (pushes
       all particles into the wall) to 240 px (moves 1/4 of the visualizer, then
       stops).
       ![limit](https://i.imgur.com/2iHIc6q.png)

    c. **The Move button* starts or stops the piston.
       ![move](https://i.imgur.com/nOxv8p6.png)

4. **Graphs** show the current properties of the gas realtime. The blue graph
   displays the pressure, the purple graph displays the speed distribution
   (which should approximate the Maxwell distribution), the red graph displays
   the temperature of the gas (which, unless the piston is moving, should be
   constant) and the green graph displays the gas volume (should also stay constant).
   ![graphs](https://i.imgur.com/l1oWXRg.png)
