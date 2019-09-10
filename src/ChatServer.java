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
		add(new Label("服务器已启动"));
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
			//程序端口号
			ss = new ServerSocket(8888);
			started = true;
//测试服务器是否启动，可删除			
System.out.println("服务器启动");
			while(started) {
				//监听连接，函数特性收到连接后才往下之执行
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
	
	//多线程实现多个多个客户端的独立连接，处理
	class Client implements Runnable{
		//用于是判断是否有用户连接
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
		 * 发送消息
		 * @param str 要发送的消息
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
					//接收到消息后向所有客户端转发消息
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
