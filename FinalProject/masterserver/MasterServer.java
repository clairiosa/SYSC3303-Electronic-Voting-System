/*
 *		SYSC 3303 - Electronic Voting System
 *	David Bews, Jonathan Oommen, Nate Bosscher, Damian Polan
 *
 *	MasterServer.java
 *
 */



//get data from each district saved in a file 
//get the file chunk by chunk
//read file and and add results to master table 

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class MasterServer {
	 
	
	public MasterServer(){
	
	}
	
    public static void main(String args[]) {
        System.out.println("MasterServer Started\n");
        Hashtable<String, Candidate> candidates;
        
        final Comm comm = new Comm();


        if (args.length < 2){
            System.out.println("java MasterServer <ListenPort> <DestinationPort> <refreshrate>");
            System.out.println("java MasterServer 8000 8001 10000");
            System.exit(1);
        }

        
        

        

        try {
            int serverPort = Integer.valueOf(args[0]);
            int destinationPort = Integer.valueOf(args[1]);
            int refreshRate = Integer.valueOf(args[2]);
            comm.StartMasterServer(serverPort, destinationPort);
            
                /*************************************/
                /*   Receiving Distric Server info   */
                /*************************************/
            ReceiveMasterServerInfo receiveThread = new ReceiveMasterServerInfo(comm);	
            receiveThread.start();
            Thread.sleep(10000); //make sure at least some results are in 
            candidates = receiveThread.getCandidates();
            
            //periodically update displayed results and send preliminary election results 
            ElectionResults electionUpdateThread = new ElectionResults(candidates, refreshRate, comm);	
            electionUpdateThread.start();
            	

            

        } 
        
        
    
        
        
        
        
        catch (Exception e) {
            System.out.println(e.getMessage());
        }  finally {
            comm.CloseMasterServer();
        }
    }



}
