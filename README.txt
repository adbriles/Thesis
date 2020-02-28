Adam Briles

To build: Enter
        gradle build

To Run: I've included three scripts to start controllers, chunk servers, and clients.
The controller should be running on Boise for everything to go smoothly. The host can be
configured, but the included scripts are set up for a controller running on boise.
To use the scripts enter:
./startController
./startClient
./startChunkServer

Upon starting the client, user input will be accepted. To put a file Enter:
put <absolute path to this directory>/Thesis/TestFiles/TestWrite.txt

To get a file:

get <absolute path to this directory>/Thesis/TestFiles/TestWrite.txt

All retrieved chunks and files will end up in the systems /tmp directory.
Be carefule getting the same file twice without deleting it after the first get.
I have append mode on to merge the chunks. When you're done getting a file, feel
free to use the cmp command in the /tmp directory against what I have in my
TestFiles directory.

To see current status of Controller records:
Go to controllers terminal and enter:
printAll
