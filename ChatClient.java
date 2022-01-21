import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable {
	Socket socket;
	JTextArea chatArea;
	JButton send_notes,shutdown,shutdown_all,join,leave;
	JTextField chatField;
	Thread thread;
	public Boolean already_joined=false;
	DataInputStream din;
	DataOutputStream dout;
	String LoginName;
	
	public ChatClient (String login,Boolean joined) throws UnknownHostException, IOException {
		super(login);
		LoginName = login;
		this.already_joined=joined;
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					dout.writeUTF(LoginName + " " + "SHUTDOWN");
					System.exit(1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		chatArea = new JTextArea(18,50);
		chatField = new JTextField(50);
		chatField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() ==  KeyEvent.VK_ENTER){
					if(already_joined){
						try {
							if(chatField.getText().length()>0)
							dout.writeUTF(LoginName + " " + "DATA " + chatField.getText().toString());
							chatField.setText("");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					}else{
						try{
							chatField.setText("JOIN FIRST!!");
							// chatField.setText("");
						}catch(Exception e1){
							e1.printStackTrace();
						}
						
					}				
			}
			}
		});
		join = new JButton("JOIN");
		leave = new JButton("LEAVE");
		send_notes = new JButton("send NOTES");
		shutdown = new JButton("SHUTDOWN");
		shutdown_all=new JButton("SHUTDOWN ALL");
		send_notes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(already_joined){
					try {
						if(chatField.getText().length()>0)
						dout.writeUTF(LoginName + " " + "DATA " + chatField.getText().toString());
						chatField.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}else{
					try{
						// dout.writeUTF(LoginName + " " + "DATA " + "JOIN FIRST");
					chatField.setText("JOIN FIRST!!");
					}catch(Exception e1){
						e1.printStackTrace();
					}
					
				}
			}
		});
		shutdown_all.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
					try {
						dout.writeUTF(LoginName + " " + "DATA " + "shutting down!");
						// chatArea.append("\n"+"shutting down!");
						dout.writeUTF(LoginName + " " + "SHUTDOWN");
						chatField.setText("");
						System.exit(1);
						try{
								Thread.sleep(2000);
							}catch(InterruptedException ex){
								Thread.currentThread().interrupt();
							}

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
		});
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!already_joined){
					try {
						dout.writeUTF(LoginName + " " + "JOIN");
						already_joined=true;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					try {
						// dout.writeUTF(LoginName + " " + "DATA " + "You Need to leave before joining!");
						chatField.setText("You need to leave before joining!");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		leave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(already_joined){
					try {
						
						dout.writeUTF(LoginName + " " + "LEAVE");
						already_joined=false;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					try {
						// dout.writeUTF(LoginName + " " + "DATA " + "You Need to join first before leaving!");
						chatField.setText("You need to join before leaving!");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		shutdown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					dout.writeUTF(LoginName + " " + "SHUTDOWN");
					System.exit(1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		socket = new Socket("localhost",5217);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(LoginName);
		dout.writeUTF(LoginName +" " +"JOIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	private void setup(){
		setSize(600,400);
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(chatArea));
		panel.add(chatField);
		panel.add(join);
		panel.add(leave);
		panel.add(send_notes);
		panel.add(shutdown);
		panel.add(shutdown_all);
		add(panel);
		setVisible(true);
		
	}
	public void run(){
		while(true){
			try {
				chatArea.append("\n"+ din.readUTF());

			} catch (IOException e) {
				e.printStackTrace();
			}

				
		}
	}

}
