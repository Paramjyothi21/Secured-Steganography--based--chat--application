//package jdbcpkg;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
 public class Clin extends JFrame{
    private JLabel statuslabel,chatlabel,instruction,instruction1;
    private JTextArea statusarea;
    private JPanel chatbox;
    private JScrollPane statusscroll;
    private JButton Embed,Send,Decode;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private JCheckBox []checks;
    private BufferedImage[] images;
    private ButtonGroup grp;
    private String ServerIP;
    public BufferedImage Bufim;
    public Clin cli;
    public JLabel setlabel;
    private int JC;    
    private String security=null;
    
    public Clin(String host){
        super("Client");
        Bufim=null;
        cli=this;
        JC=-1;       
        ServerIP=host;
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(720,20,700,700);
        this.setResizable(false);   
        this.setLayout(null);
        this.getContentPane().setBackground(Color.DARK_GRAY);
        
        
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
        
        initcomponents();
    }
    private void initcomponents(){
	    statuslabel=new JLabel("STATUS :");
            statuslabel.setForeground(Color.WHITE);
            statuslabel.setBounds(50,5,70,50);
            add(statuslabel);
            
            statusarea=new JTextArea();
            statusscroll=new JScrollPane(statusarea);
            statusscroll.setBounds(115,5,300,100);
            add(statusscroll);
            
	    instruction=new JLabel("Please 'EMBED' to prepare message !");
            instruction.setForeground(Color.WHITE);
            instruction.setBounds(420,5,250,50);
            add(instruction);  
            
	    instruction1=new JLabel("Please select a message to 'READ' !");
            instruction1.setForeground(Color.WHITE);
            instruction1.setBounds(420,60,270,50);
            add(instruction1);             
            
            chatlabel=new JLabel("CHAT WINDOW :");
            chatlabel.setForeground(Color.WHITE);
            chatlabel.setBounds(50,110,100,50);
            add(chatlabel);            
            
            checks=new JCheckBox[100];
            grp=new ButtonGroup();            
            images=new BufferedImage[100];
            
            Embed=new JButton("EMBED");
            Embed.setBounds(50,610,100,40);
            Embed.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    new EmbedMessage(cli,security);
                }
            });
            add(Embed); 

	    setlabel=new JLabel("No image is set!");
            setlabel.setForeground(Color.WHITE);
            setlabel.setBounds(150,610,150,40);
            add(setlabel);            
            
            Decode=new JButton("READ");
            Decode.setBounds(550,610,100,40);
         
            Decode.setEnabled(false);
            Decode.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    BufferedImage BB=null;
                    for(int i=0;i<=JC;i++){
                        if(checks[i].isSelected()){
                            BB=images[i];
                            break;
                        }
                    }
                    new DecodeMessage(BB);
                }
            });
            add(Decode);              
            
            chatbox=new JPanel();
            BoxLayout layout=new BoxLayout(chatbox,BoxLayout.Y_AXIS);
            chatbox.setBackground(Color.BLACK);
            chatbox.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            chatbox.setLayout(layout);
            JScrollPane scroll = new JScrollPane(chatbox);
            scroll.setBounds(50,150,600,450);
            add(scroll);       
            
            grp=new ButtonGroup();
            
            Send=new JButton("SEND");
           
            Send.setEnabled(false);
            Send.setBounds(300,610,100,40);
            Send.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            BufferedImage IM=Bufim;
                            if(IM==null){
                                return;
                            }
                            Decode.setEnabled(true);
                            JPanel Panel=new JPanel();
                            Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                            Panel.setBackground(Color.white);
                            
                            JCheckBox chk=new JCheckBox("CLIENT");
                            chk.setSize(100, 50);
                            checks[++JC]=chk;
                            grp.add(checks[JC]);
                            
                            JLabel lb=new JLabel();
                            lb.setSize(150,150);
                            lb.setIcon(new ImageIcon(IM.getScaledInstance(lb.getWidth(), lb.getHeight(), Image.SCALE_SMOOTH)));
                            
                            images[JC]=IM;
                            
                            Panel.add(checks[JC]);
                            Panel.add(lb);
                            chatbox.add(Panel);
                            chatbox.add(Box.createVerticalStrut(20));
                            cli.validate();
                            cli.repaint();                            
                            sendMessage(IM);
                        }
                    });
                }
            });
            add(Send);
            
            cli.validate(); 
            cli.repaint();
            
    }
   
    public static void main(String[] args) {
    	
    	//Clin clin=new Clin("172.22.9.17");
    	Clin clin=new Clin("localhost");
		 clin.startRunning();    	
    	
		 
	}

    public void startRunning(){//////////////////////////////
		try{
			
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException){
			showMessage("\nClient terminated the connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	private void connectToServer() throws IOException{
		showMessage("\nAttempting connection...");
		connection = new Socket(InetAddress.getByName(ServerIP), 7777);
		showMessage("\nConnection Established! Connected to: " + connection.getInetAddress().getHostName());
	}
	private void setupStreams() throws IOException{//////////////////////////////////////////////////
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		
		showMessage("\nStreams are now setup");
	}
	private void whileChatting() throws IOException{///////////////////////////////////
           ////////
		ableToSend(true);
            Object mess=null;
            try {
                showMessage("\n" + (String)input.readObject());
                security=JOptionPane.showInputDialog(this,"Hey Client please provide the OTP you just recieved!");
                if(security==null){
                    security="-";
                }
                sendMessage(security);
                String rs=(String)input.readObject();
                showMessage("\n" + ((rs.equals("CLOSE"))?"":rs));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Clin.class.getName()).log(Level.SEVERE, null, ex);
            }
		do{
			try{
				mess =input.readObject();
                                if(mess instanceof String){
                                    if(!((String)mess).equals("CLOSE"))
                                        showMessage("\n" + mess);
                                }
                                else {
                                    ByteArrayInputStream bin=new ByteArrayInputStream((byte[]) mess);
                                    BufferedImage ms=ImageIO.read(bin);
                                    displayMessage(ms);
                                    bin.close();  
                                }
			}catch(Exception Ex){
				showMessage("\nThe server has sent an unknown object!");
			}
		}while(mess instanceof byte[]||(mess!=null&&!((String)mess).equals("CLOSE")));
	}
	
	public void closeConnection(){////////////////////////////////////////////////////////
                sendMessage("CLOSE");
		showMessage("\nClosing Connections...");
		ableToSend(false);
		try{
			output.close();
			input.close();
                        connection.close();
		}catch(IOException ioEx){
			showMessage("\nError in closing the streams!");
		}
	}
	
	private void sendMessage(Object message){//////////////////////////////////////////
            ByteArrayOutputStream bos;
            /*if(message==null){
                return;
            }*/
		try{
                    if(message==null||message instanceof String){
                        output.writeObject(message);
                        output.flush();
                    }else{
                        BufferedImage bm=(BufferedImage)message;
                        bos=new ByteArrayOutputStream();
                        ImageIO.write(bm, "png", bos );
                        byte[] imageInByte = bos.toByteArray();
                        bos.flush();
			output.writeObject(imageInByte);
                        output.flush();
                        bos.close();
                    }
                    setlabel.setText("No image is set!");
                    Bufim=null;
		}catch(IOException ioEx){
			statusarea.append("\n Error : Cannot send message!, PLEASE RETRY");
		}
	}
        
        private void displayMessage(BufferedImage mes){
                   SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            if(mes==null){
                                return;
                            }
                            Decode.setEnabled(true);
                            BufferedImage IM=mes;
                            JPanel Panel=new JPanel();
                            Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
                            Panel.setBackground(Color.white);
                            
                            JCheckBox chk=new JCheckBox("SERVER");
                            chk.setSize(100, 50);
                            checks[++JC]=chk;
                            grp.add(checks[JC]);
                            
                            JLabel lb=new JLabel();
                            lb.setSize(150,150);
                            lb.setIcon(new ImageIcon(IM.getScaledInstance(lb.getWidth(), lb.getHeight(), Image.SCALE_SMOOTH)));
                            
                            images[JC]=IM;
                            
                            Panel.add(lb);
                            Panel.add(checks[JC]);
                            chatbox.add(Panel);
                            chatbox.add(Box.createVerticalStrut(20));
                            cli.repaint();
                            cli.validate();                            
                        }
                    });            
        }
	
	private void showMessage(final String text){/////////////////////////////////////
		SwingUtilities.invokeLater(
			new Runnable(){
                                @Override
				public void run(){
					statusarea.append(text);
				}
			}
		);
	}
	
	private void ableToSend(final boolean tof){///////////////////////////////////////
		SwingUtilities.invokeLater(
			new Runnable(){
                                @Override
				public void run(){
                                    Send.setEnabled(tof);
				}
			}
		);
	} 
	
	 
}
 