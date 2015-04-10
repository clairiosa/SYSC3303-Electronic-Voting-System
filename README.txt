    SYSC 3303 - Electronic Voting System

Authors: David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan


    UML DIAGRAMS AND COMMUNICATION PROTOCOL INFORMATION

documents.zip Contains the UML Diagrams and Report.
FinalProject.zip Contains the code.



~~~~Run the following from the PARENT directory of FinalProject\ ~~~~


    COMPILING AND RUNNING OVERALL AUTOMATED TEST

javac -cp "FinalProject\jfrrchart\*;." FinalProject\test\BoothTest.java
java FinalProject.test.BoothTest



    COMPILING AND RUNNING MANUAL TEST

javac -cp "FinalProject\jfrrchart\*;." FinalProject\test\ManualTest.java
java FinalProject.test.ManualTest



    COMPILING AND RUNNING COMMUNICATION PROTOCOL AUTOMATED TEST

javac FinalProject\test\CommTest.java
java FinalProject\test\CommTest



    COMPILING AND RUNNING INDIVIDUAL PORTIONS


Master Server:
javac -cp "FinalProject\jfrrchart\*;." FinalProject\masterserver\MasterServer.java
java FinalProject.masterserver.MasterServer <port> <voterFilename> <CandidatesFilename> <refreshrate>

District Server:
javac FinalProject\districtserver\DistrictServer.java
java FinalProject\districtserver\DistrictServer <Listen Port> <Master IP> <Master Port> <Unique District ID>

Booth:
javac FinalProject\booth\Booth.java
java FinalProject\booth\Booth <District IP> <District Port> <Listen Port>