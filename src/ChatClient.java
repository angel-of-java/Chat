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
	 * ���д��ڣ����ô�����ʽ
	 */
	public void launchFrame(){		
		  add(ta,BorderLayout.NORTH);
		  add(tf,BorderLayout.SOUTH);
		  setSize(300,300);
		  setVisible(true);
		  setLocationRelativeTo(null);
		  setTitle("����������");
		  pack();
		  //�˳�����
		  this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disconnect();
				System.exit(0);
			}
			  
		  });
		  
		  //��������
		  connect();  
		  //�������Ϳ�
		  tf.addActionListener(new ActionTf());
	}

	/**
	 * ���������������
	 */
	public void connect() {
		try {
			//127.0.0.1  �Ǳ���IP��8888�ǳ���ʹ�õĶ˿ں�
			s = new Socket("127.0.0.1", 8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			//����������Ϣ
			new Thread(new receive()).start();
		} catch(SocketException e) {
			ta.setText("������δ������");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class receive implements Runnable {	
		String str = null;
		boolean b = true;
		
		public void run() {	
			try {
				//��������
				while (b) {
					//ֻ��readUTF()���������������Ϣ������Ż�����ִ
					str = dis.readUTF();
					//������ʾ��Ϣ������Ϊ��ǰ����Ϣ�Ӹս��յ�����Ϣ
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
	 * �Ͽ�����
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
	 * �������Ϳ򣬻س�������Ϣ
	 */
	class ActionTf implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String sendmessage = tf.getText();
			//������Ϣ֮�󣬷��Ϳ��������
			tf.setText(null);
			try {
				dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(sendmessage);
				dos.flush();
			} catch (NullPointerException e1) {
				ta.setText("������δ������");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}



