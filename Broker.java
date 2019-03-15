import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//lol
public class Broker implements Runnable{
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket s;
	private int hashnumber;
	private String hashstring;
	private static boolean recieveorsend = false;
	//we want the data from publisher
	private static ArrayList<BusAndLocation> DataFromPublisher;
	private static ArrayList<BusAndLocation> DataResponsible;
	private static ArrayList<Consumer> consumers;
	private static ArrayList<Publisher> registeredPublishers;
	private Iterator registeredPublishersIterator = null;
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

	private int gethashnumber(){
		return hashnumber;
	}

	private void WakeUp() throws InterruptedException {//UNDONE

	}

	public static void main(String[] args) throws IOException {

		// server is listening on port 5056
		ServerSocket serverSocket = new ServerSocket(5056);
		while (true)
		{
			Socket socket = null;

			try
			{
				// socket object to receive incoming client requests
				socket = serverSocket.accept();

				System.out.println("A new client is connected : " + socket);

				// obtaining input and out streams
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				Thread thread = new Thread(socket, in, out);

				// Invoking the start() method
				thread.start();

			}
			catch (Exception e){
				socket.close();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		this.startBroker();
		//We will get the data and we 'll decide if we want
		//UNDONE
	}

	private void startBroker() {
		Socket requestSocket; //arxikopoihsh
		ObjectOutputStream out;
		ObjectInputStream in;
		String message;
		while (true) {
			if (!recieveorsend)
				try {
					requestSocket = new Socket(InetAddress.getByName("127.0.0.1"), 4321); //local address

					out = new ObjectOutputStream(requestSocket.getOutputStream());
					in = new ObjectInputStream(requestSocket.getInputStream());


					message = (String) in.readObject();
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
			else {
				try {
					requestSocket = new Socket(InetAddress.getByName("127.0.0.1"), 4321); //local address

					out = new ObjectOutputStream(requestSocket.getOutputStream());
					in = new ObjectInputStream(requestSocket.getInputStream());

					out.writeObject("I am responsible for these keys:\n");
					message = (String) in.readObject();

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
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					System.err.println("data received in unknown format");
				}
			}
		}
	}

	public static ArrayList<BusAndLocation> GetDataFromPublisher(){
    	 return  DataFromPublisher;
	}

	private String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException ignored) {}
		return null;
	}

	private void PutData(BusAndLocation Data){//UNDONE
		String MD = MD5(Data.GetBusLine());
		int mod=hashnumber;
		if (MD != null && mod % 100 <= hashnumber && MD.equals(hashstring)) DataResponsible.add(Data);
	}

	private BusAndLocation Suitmessage(String message){//UNDONE
		BusAndLocation BAL = new BusAndLocation();
		//BAL.SetTopic(message.charAt(index));//SEE
		//BAL.SetValue(Integer.parseInt(message.charAt(index)));
		return BAL;
	}
}