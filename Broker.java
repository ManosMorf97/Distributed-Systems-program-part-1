import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Broker {
	private static int hashnumber;
	private static String hashstring;
	//we want the data from publisher
	private static ArrayList<BusAndLocation> DataFromPublisher;
	private static ArrayList<BusAndLocation> DataResponsible;
	private static HashMap<String, BusAndLocation> Keys;

	private HashMap<String, BusAndLocation> getKeys() {
		return Keys;
	}

	private static int gethashnumber() {
		return hashnumber;
	}

	public static ArrayList<BusAndLocation> GetDataFromPublisher() {
		return DataFromPublisher;
	}

	private static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException ignored) {
		}
		return null;
	}

	private static void PutData(BusAndLocation Data) {//UNDONE
		String MD = MD5(Data.GetBusLine());
		int mod = hashnumber;
		if (MD != null && mod % 100 <= hashnumber && MD.equals(hashstring)) DataResponsible.add(Data);
	}

	private static BusAndLocation Suitmessage(String message) {//UNDONE
		BusAndLocation BAL = new BusAndLocation();
		//BAL.SetTopic(message.charAt(index));//SEE
		//BAL.SetValue(Integer.parseInt(message.charAt(index)));
		return BAL;
	}

	public static class ComunicationWithPublisherThread implements Runnable {
		private Socket socket;

		public ComunicationWithPublisherThread (Socket socket) {
			this.socket = socket;
		}

		public void run() {

			try {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject("I am responsible for these keys:\n");
				for (String keys : Keys.keySet())
					out.writeObject(keys + "\n");
				String message = (String) in.readObject();
				while (!message.equals("bye")) {
					BusAndLocation TransformedMessage = (Suitmessage(message));
					DataFromPublisher.add(TransformedMessage);//Suitmessage UNDONE
					PutData(TransformedMessage);
					message = (String) in.readObject();
					if (!Keys.containsKey(TransformedMessage.GetBusLine()))
						Keys.put(TransformedMessage.GetBusLine(), TransformedMessage);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
    public static class ComunicationWithConsumerThread implements  Runnable{
		private Socket socket;

		public ComunicationWithConsumerThread(Socket socket){
			this.socket=socket;
		}
		public void run(){
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				out.writeObject("I am responsible for these keys:\n");
				//String message = (String) in.readObject();
				for (String keys : Keys.keySet())
					out.writeObject(keys + "\n");
				//the other brokers



				//
				try {
					String message = (String) in.readObject();
					out.writeObject("Bus from line " + message + "\n");
					for (BusAndLocation dr : DataResponsible) {
						if (dr.GetBusLine().equals(message))
							out.writeObject("Bus: " + dr.GetBusLine() + " location" + dr.GetLongitude() + " " + dr.GetLatitude() + "\n");
					}
				}catch(ClassNotFoundException e){
					e.printStackTrace();;
				}
			}catch(IOException  e){
				e.printStackTrace();
			}

		}
	}
	public static void main(String[] args) throws IOException{
		new Broker().openServer();
	}

	public void openServer() throws IOException {
		ArrayList<Thread> threads = new ArrayList<>();
		ServerSocket providerSocket = null;
		Socket connection = null;
		providerSocket = new ServerSocket(4321);
		while (true) {
              for(int i=0; i<10; i++) {
				  connection = providerSocket.accept();
				  ComunicationWithPublisherThread CWPT = new ComunicationWithPublisherThread (connection);
				  Thread t1 = new Thread(CWPT);
				  t1.start();
				  threads.add(t1);
			  }
              for(Thread thr : threads  ){
              	try {
					thr.join();
				}catch(InterruptedException e){
              		e.printStackTrace();
				}
			  }
			   providerSocket=null;
              providerSocket=new ServerSocket(5056);
              connection=null;
              connection=providerSocket.accept();
			for(int i=0; i<10; i++) {
				connection = providerSocket.accept();
				ComunicationWithConsumerThread CWCT = new ComunicationWithConsumerThread (connection);
				Thread t1 = new Thread(CWCT);
				t1.start();
				threads.add(t1);
			}
			for(Thread thr : threads  ){
				try {
					thr.join();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}


		}
	}
}
