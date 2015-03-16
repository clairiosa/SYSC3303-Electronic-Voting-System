
package FinalProject.booth;

public class Booth {

	DistrictServerComm server;

	public Booth(){
		
	}

	public ElectionResults getElectionStatus(){
		return this.server.getElectionStatus();
	}

	public boolean register(Person p){
		return this.server.registerUser(b);
	}

	public boolean vote(Ballot b){
		return this.server.castBallot(b);
	}

	public static void main(String args[]) {

		final Comm comm = new Comm();

		if(args.length < 2)
			System.out.println("java booth <<DistrictServer IP>>");
			return;
		}

		this.server = comm.getDistrictServer(InetAddress.getByName(args[0]));
		
	}
}