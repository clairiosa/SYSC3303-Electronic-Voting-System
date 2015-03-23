    SYSC 3303 - Electronic Voting System

Authors: David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan


    UML DIAGRAMS AND COMMUNICATION PROTOCOL INFORMATION

The component diagrams can be found in INFO/
A sequence diagram for the communication protocol can be found in INFO/
A brief explanation of the communication protocol can be found in INFO/CommunicationOverview.txt



~~~~Run the following from the directory FinalProject\ resides in.~~~~


    COMPILING AND RUNNING OVERALL AUTOMATED TEST

javac FinalProject\test\AutomatedTest.java
java FinalProject\test\AutomatedTest

The output will appear as election-results.txt


    COMPILING AND RUNNING COMMUNICATION PROTOCOL AUTOMATED TEST

javac FinalProject\test\CommTest.java
java FinalProject\test\CommTest


    COMPILING AND RUNNING INDIVIDUAL PORTIONS

Master Server:
javac FinalProject\masterserver\MasterServer.java
java FinalProject\masterserver\MasterServer <Listen Port> <Voter Filename> <Candidates Filename> <Election Refresh Rate (ms)>

District Server:
javac FinalProject\districtserver\DistrictServer.java
java FinalProject\districtserver\DistrictServer <Listen Port> <Master IP> <Master Port> <Unique District ID>

Booth:
javac FinalProject\booth\Booth.java
java FinalProject\booth\Booth <District IP> <District Port> <Listen Port>