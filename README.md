Oregon Trail
------------
This was a CS 2340 semester-long project in Fall 2011. The project was to design and develop an Oregon Trail variant using object-oriented design patterns and other concepts learned in the class.

This project was designed and developed in a semester by 5 undergraduate team members. It was built using [Slick2D](http://www.slick2d.org/). All UI components were custom written.

Screenshots
-----------
You can view screenshots of the game in the [screenshots](/screenshots) directory.

Running the Game
----------------
### Run with ANT
1. Install Apache ANT
2. Open Oregon-Trail directory in a console
3. Run the corresponding ANT command for your OS:
	* If you are running Windows, use the command: "ant run-windows" (no quotes)
	* If you are running Mac OS X, use the command: "ant run-macosx" (no quotes)
	* If you are running Linux, use the command: "ant run-linux" (no quotes)

### Run with Eclipse
1. Add the following jars to the project build path (right click project -> Properties -> Java Build Path):
    * lib/lwjgl-2.7.1/jar/lwjgl.jar
    * lib/slick.jar
    * lib/jogg-0.0.7.jar
    * lib/jorbis-0.0.17.jar
3. Edit native library location for lwjgl.jar to path lib/lwjgl-2.8.2/native/[YOUR_OS_HERE]

Team Members
------------
* Tanner Smith
* Ryan Ashcraft
* John Turner 
* George Johnston
* Jeremy