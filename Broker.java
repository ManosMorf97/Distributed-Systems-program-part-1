import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


//lol
public class Broker{
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket s;
	private static int hashnumber;
	private static String hashstring;
	private static boolean recieveorsend = false;
	//we want the data from publisher
	private static ArrayList<BusAndLocation> DataFromPublisher;
	private static ArrayList<BusAndLocation> DataResponsible;
	protected static ArrayList<Consumer> consumers;
	protected static ArrayList<Publisher> registeredPublishers;
	private static HashMap<String, BusAndLocation> Keys;
	private static int PublisherID = -1;//UNDONE
	private static int ConsumerID = -1;//UNDONE



	private Broker(Socket s, DataInputStream dis, DataOutputStream dos) {
		registeredPublishers = new ArrayList<>();
		registeredPublishers.add(new Publisher());
		consumers = new ArrayList<>();
		consumers.add(new Consumer());
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	private Broker(ArrayList<Consumer> consumers, ArrayList<Publisher> registeredPublishers,int hashnumber, String hashstring) {
		this.consumers = consumers;
		this.registeredPublishers = registeredPublishers;
		this.hashnumber = hashnumber;
		this.hashstring = hashstring;
	}

	private HashMap<String,BusAndLocation> getKeys(){
		return Keys;
	}

	private static int gethashnumber(){
		return hashnumber;
	}

	private void WakeUp() throws InterruptedException {//UNDONE

	}

	public static void main(String[] args) throws IOException,InterruptedException {

		// server is listening on port 5056
		Thread [] pubthread=new Thread[registeredPublishers.size()];
		for(int i=0; i<registeredPublishers.size(); i++){
			pubthread[i]=new Thread(new PubBroker(i));
			pubthread[i].start();
		}
		for(int i=0; i<registeredPublishers.size(); i++){
			pubthread[i].join();
		}
		Thread [] conthread=new Thread[consumers.size()];
		for(int i=0; i<consumers.size(); i++){
			conthread[i]=new Thread(new ConBroker(i));
			conthread[i].start();
		}
		for(int i=0; i<consumers.size(); i++){
			conthread[i].join();
		}
	}
   public static class PubBroker implements Runnable{
		int number_of_pub;
		public PubBroker(int number_of_pub){
			this.number_of_pub=number_of_pub;
		}
		public void run(){
			try {
				Socket requestSocket = new Socket(InetAddress.getByName("127.0.0.1"), 4321); //local address

				ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());


				String message = (String) in.readObject();
				while (message != null) {
					BusAndLocation TransformedMessage = (Suitmessage(message));
					DataFromPublisher.add(TransformedMessage);//Suitmessage UNDONE
					PutData(TransformedMessage);
					message = (String) in.readObject();
					if (!Keys.containsKey(TransformedMessage.GetBusLine())) Keys.put(TransformedMessage.GetBusLine(), TransformedMessage);
				}
				requestSocket.close();
			}catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
   }
   public static class ConBroker implements Runnable {
	   int number_of_cons;

	   public ConBroker(int number_of_cons) {
		   this.number_of_cons = number_of_cons;
	   }

	   public void run() {
		   try {
			   Socket requestSocket = new Socket(InetAddress.getByName("127.0.0.1"), 4321); //local address

			   ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
			   ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());

			   out.writeObject("I am responsible for these keys:\n");
			   String message = (String) in.readObject();

			   for (String keys : Keys.keySet())
				   out.writeObject(keys + "\n");
			   //continue
			   //THE OTHER BROKERS RESPONSIBLE FOR?
			   int i = 0;
			   for (Broker br : Node.getBrokers()) {
				   if (gethashnumber() != br.gethashnumber()) {
					   i++;
					   out.writeObject("Keys responsible  broker: " + i + "\n");
					   for (String keys : br.getKeys().keySet()) out.writeObject(keys + "\n");
				   }
			   }

			   while (message != null) {
				   out.writeObject("Bus from line " + message + "\n");
				   for (BusAndLocation dr : DataResponsible) {
					   if (dr.GetBusLine().equals(message))
						   out.writeObject("Bus: " + dr.GetBusLine() + " location" + dr.GetLongitude() + " " + dr.GetLatitude() + "\n");
				   }
				   message = (String) in.readObject();

			   }
			   out.writeObject("That's all for now\n");
			   requestSocket.close();
			   out.close();
			   in.close();
		   } catch (IOException | ClassNotFoundException e) {
			   e.printStackTrace();
		   }
	   }
   }



	public static ArrayList<BusAndLocation> GetDataFromPublisher(){
    	 return  DataFromPublisher;
	}

	private static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException ignored) {}
		return null;
	}

	private static void PutData(BusAndLocation Data){//UNDONE
		String MD = MD5(Data.GetBusLine());
		int mod=hashnumber;
		if (MD != null && mod % 100 <= hashnumber && MD.equals(hashstring)) DataResponsible.add(Data);
	}

	private static BusAndLocation Suitmessage(String message){//UNDONE
		BusAndLocation BAL = new BusAndLocation();
		//BAL.SetTopic(message.charAt(index));//SEE
		//BAL.SetValue(Integer.parseInt(message.charAt(index)));
		return BAL;
	}
}