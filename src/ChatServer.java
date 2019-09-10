import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class ChatServer extends Frame{

	ServerSocket ss = null;
	Socket s = null;
	List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	ChatServer() {
		add(new Label("������������"));
		setVisible(true);
		setSize(200,100);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void start() {
		boolean started = false;
		try {
			//����˿ں�
			ss = new ServerSocket(8888);
			started = true;
//���Է������Ƿ���������ɾ��			
System.out.println("����������");
			while(started) {
				//�������ӣ����������յ����Ӻ������ִ֮��
				s = ss.accept();
				Client c = new Client(s);
				new Thread(c).start();
				clients.add(c);			
			}
		}catch(BindException e) {
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(s != null) s.close();
				if(ss != null) ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//���߳�ʵ�ֶ������ͻ��˵Ķ������ӣ�����
	class Client implements Runnable{
		//�������ж��Ƿ����û�����
		boolean bConnect = false;
		
		Socket s = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		
		Client(Socket s){
			this.s = s;
			try {
				bConnect = true;
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * ������Ϣ
		 * @param str Ҫ���͵���Ϣ
		 */
		public void send(String str) {
			try {
				dos.writeUTF(str);
				dos.flush();
			} catch (SocketException e) {
				clients.remove(this);
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				while(bConnect) {
					String receive;
					receive = dis.readUTF();
					//���յ���Ϣ�������пͻ���ת����Ϣ
					for(int i = 0; i<clients.size(); i++) {
						Client c = clients.get(i);
						c.send(receive);
					}
				} 
			}catch(EOFException e) {
				System.out.println("Client closed!");
			}catch (IOException e) {
					e.printStackTrace();
			}finally {
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null) s.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
