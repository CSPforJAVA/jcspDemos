Ray Trace - ray tracing example using distributed workers

**net**

run Main
The process will start by creating a CNS if one is not already running.  
The console output will indicate with the message, Main: waiting for initial worker,
that it will only start once a worker has been initiated.
You will need to find the IP address of the server which is near the top of the console
output, in a message like, 

TCP/IP V4 LinkServer listening on 192.168.1.69:7890 Started

run Worker
Copy the IP address into the splash screen as indicated, without the port number (:7890)
A window will open and a moving set of balls will be seen.  The Image can be rotated 
by moving the mouse in the window.
The Worker console will indicate the number of frames per second being generated.

Further instances of the Worker process can be started and the frame rate should increase!

**net2**

run Main
The console output will indicate with the message, Main: waiting for initial worker,
that it will only start once a worker has been initiated.
The console output also indicates the IP address of the node on which the Main process
is running.

run Worker
Copy the IP address into the console as indicated.
If you are running the Worker on the same machine then choose a port number 2000 upwards, 
otherwise any port number can be chosen.  If more than one worker is run on a machine then 
ensure they each have different port numbers.
A window will open and a moving set of balls will be seen.  The Image can be rotated 
by moving the mouse in the window.
The Worker console will indicate the number of frames per second being generated.

Further instances of the Worker process can be started and the frame rate should increase!

