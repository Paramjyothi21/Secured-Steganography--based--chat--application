
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
class IDandPasswords {
	HashMap<String,String> logininfo = new HashMap<String,String>();
	String user;
	String pass;
	String regno;
	String grade;
	String sbranch;
	IDandPasswords(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","12345678");  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from Student");  
			while(rs.next())  
			{
			//System.out.println("REGNO : "+rs.getString(1)+"  STUDENTNAME :  "+rs.getString(2)+"  PASSWORD :  "+rs.getString(3)+"   CGPA : "+rs.getString(4)+"   BRANCH: "+rs.getString(5));  
			regno=rs.getString(1);
			user=rs.getString(2);
			pass=rs.getString(3);
			grade=rs.getString(4);
			sbranch=rs.getString(5);
			logininfo.put(user,pass);
			}
			 //System.out.println(logininfo); 
			}catch(Exception e){ System.out.println(e);}  
			}  
		
public HashMap getLoginInfo(){
		return logininfo;
	}
}
class Login implements ActionListener{
	JFrame frame = new JFrame();
	JButton loginButton = new JButton("LOGIN");
	JButton resetButton = new JButton("RESET");
	JButton register=new JButton("REGISTER");
	JTextField userIDField = new JTextField(20);
	JPasswordField userPasswordField = new JPasswordField(18);
	JLabel userIDLabel = new JLabel("USERID:");
	JLabel userPasswordLabel = new JLabel("PASSWORD:");
	JLabel messageLabel = new JLabel();
	JLabel loginform=new JLabel("LOGIN  FORM");
	HashMap<String,String> logininfo = new HashMap<String,String>();
	BufferedImage img;
	JFrame fm;
	JTextArea distxt=new JTextArea(15,15);
	 Login(HashMap<String,String> loginInfoOriginal) {
		logininfo = loginInfoOriginal;
		userIDLabel.setBounds(50,100,75,25);
		userPasswordLabel.setBounds(50,150,75,25);
		loginform.setBounds(125,250,250,35);
		loginform.setFont(new Font(null,Font.CENTER_BASELINE,40));
		messageLabel.setBounds(125,250,250,35);
		messageLabel.setFont(new Font(null,Font.ITALIC,30));
		userIDField.setBounds(125,100,200,25);
		userPasswordField.setBounds(125,150,200,25);
		loginButton.setBounds(125,200,100,25);
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		resetButton.setBounds(225,200,100,25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		register.setBounds(225,200,100,25);
		register.setFocusable(false);
		register.addActionListener(this);
		frame.add(loginform);
		frame.add(userIDLabel);
		frame.add(userIDField);
		frame.add(userPasswordLabel);
		frame.add(userPasswordField);
		frame.add(messageLabel);
		frame.add(loginButton);
		frame.add(resetButton);
		frame.add(register);
		frame.add(distxt);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,400);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setResizable(false);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==resetButton) {
			userIDField.setText("");
			userPasswordField.setText("");
		}
		if(e.getSource()==loginButton) {
			String userID = userIDField.getText();
			String password = String.valueOf(userPasswordField.getPassword());
			if(logininfo.containsKey(userID)) {
				if(logininfo.get(userID).equals(password)) {
					messageLabel.setForeground(Color.green);
					messageLabel.setText("Login successful");
					frame.dispose();
					JOptionPane.showInputDialog("Hello "+userID+" !!\nYou are Logged in SuccessFully..\n Enter ok");
				   JOptionPane.showInputDialog(" YOUR REQUESTED IP ADDRESS is : 172.22.62.202 ");
				   new Netinfo();
				}
				else {
					messageLabel.setForeground(Color.red);
					messageLabel.setText("Wrong password");
				}

			}
			else {
				messageLabel.setForeground(Color.red);
				messageLabel.setText("username not found");
				
			}
		}
		if(e.getSource()==register) {
			   fm=new JFrame();
			   JTextField t1=new JTextField(25);
			   JTextField t2=new JTextField(25);
			   JTextField t3=new JTextField(30);
			   JTextField t4=new JTextField(30);
			   JTextField t5=new JTextField(30);
			   JTextArea txt=new JTextArea(8,8);
			   JLabel j0=new JLabel("REGISTRATION FORM");
			   JLabel j1=new JLabel("REGISTRATION NUMBER");
			   JLabel j2=new JLabel("STUDENT NAME ");
			   JLabel j3=new JLabel(" CREATE PASSWORD");
			   JLabel j4=new JLabel("ENTER GPA");
			   JLabel j5=new JLabel("ENTER BRANCH");
		      JButton submit =new JButton("SUBMIT");
		      submit.addActionListener(new ActionListener(){
	                public void actionPerformed(ActionEvent ae) {
	                	try {
	                		String roll,name,pwd,cgpa,branch;
	        				Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","12345678"); 
	        				PreparedStatement ps=con.prepareStatement("insert into student values(?,?,?,?,?)");
	        				roll=t1.getText();
	        				name=t2.getText();
	        				pwd=t3.getText();
	        				cgpa=t4.getText();
	        				branch=t5.getText();
	        				ps.setString(1, roll);
	        				ps.setString(2, name);
	        				ps.setString(3, pwd);
	        				ps.setString(4, cgpa);
	        				ps.setString(5, branch);
	        				ResultSet rs2=ps.executeQuery();
	        				System.out.println("RECORD INSERTED SUCCESSFULLY..\n");
	        				 txt.setText("REGISTRATION COMPLETED SUCESSFULLY..");
	        			}catch(Exception eM)
	        			{
	        				eM.printStackTrace();
	        			}
	                }
	            });
		      
		      j0.setBounds(125,250,250,35);
			  j0.setFont(new Font(null,Font.CENTER_BASELINE,40));
				fm.add(j0);
			  fm.add(j1);
			  fm.add(t1);
			  fm.add(j2);
			  fm.add(t2);
			  fm.add(j3);
			  fm.add(t3);
			  fm.add(j4);
			  fm.add(t4);
			  fm.add(j5);
			  fm.add(t5);
			  fm.add(submit);
			  fm.add(txt);
			  fm.setBackground(Color.cyan);
			  fm.setForeground(Color.red);
			   fm.setSize(500,500);
			   fm.setLayout(new FlowLayout());
			   fm.setVisible(true);
			   fm.setResizable(true);
			   fm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}
}
public class LoginPage {
	public static void main(String[] args) {
			 IDandPasswords idandPasswords = new IDandPasswords();
				Login loginPage = new Login(idandPasswords.getLoginInfo());

		}	
		
	}


