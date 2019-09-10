import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ChatClient extends Frame{
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	TextArea ta = new TextArea();
	TextField tf = new TextField();
	
	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
	
	/**
	 * 运行窗口，设置窗口样式
	 */
	public void launchFrame(){		
		  add(ta,BorderLayout.NORTH);
		  add(tf,BorderLayout.SOUTH);
		  setSize(300,300);
		  setVisible(true);
		  setLocationRelativeTo(null);
		  setTitle("匿名聊天室");
		  pack();
		  //退出窗口
		  this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);
			}
			  
		  });
		  
		  //建立连接
		  connect();  
		  //监听发送框
		  tf.addActionListener(new ActionTf());
	}

	/**
	 * 与服务器建立连接
	 */
	public void connect() {
		try {
			//127.0.0.1  是本机IP，8888是程序使用的端口号
			s = new Socket("127.0.0.1", 8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			//监听接收消息
			new Thread(new receive()).start();
		} catch(SocketException e) {
			ta.setText("服务器未启动！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class receive implements Runnable {	
		String str = null;
		boolean b = true;
		
		public void run() {	
			try {
				//监听输入
				while (b) {
					//只有readUTF()函数读到输入的信息，程序才会往下执
					str = dis.readUTF();
					//设置显示消息框内容为以前的信息加刚接收到的信息
					ta.setText(ta.getText() + str + '\n');			
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 断开连接
	 */
	public void disconnect() {
		try {			
			if(dos != null) dos.close();
			if(dis != null) dis.close();
			if(s != null) s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 监听发送框，回车后发送消息
	 */
	class ActionTf implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String sendmessage = tf.getText();
			//发送消息之后，发送框内容清空
			tf.setText(null);
			try {
				dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(sendmessage);
				dos.flush();
			} catch (NullPointerException e1) {
				ta.setText("服务器未启动！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}



