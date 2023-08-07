
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
public class Netinfo{
	JFrame frame=new JFrame("NETWORK INFORMATION");
	JTextField t1=new JTextField(20);
	JTextField t2=new JTextField(20);
	JLabel l1=new JLabel("             ENTER IP ADDRESS :");
	JLabel l2=new JLabel("ENTER NUM OF ADDRESS IN SUBNET:");
	JButton display=new JButton("subnetdetails");
	JButton clear=new JButton("clear");
	JButton map=new JButton("MAC");
	JTextArea txt=new JTextArea(15,15);
	Netinfo(){
		map.setBounds(225,200,100,25);
		map.setFocusable(false);
		map.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    	try
            {String ipaddr = t1.getText();
            InetAddress address = InetAddress.getByName(ipaddr);
            txt.append("\n\n\nDISPLAYING MAC INFORMATION  USING MAC ADDRESS RESOLUTION PROTOCOL\n");
            txt.append("IP ADDRESS :"+address+"\n");
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            if (ni!=null)
            {   byte[] mac = ni.getHardwareAddress();
                if (mac != null)
                {   txt.append("MAC ADDRESS : ");
                    for (int i=0; i < mac.length; i++)
                    {
                        txt.append((String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" :"")));
                    }
                }
                else
                {
                	txt.append("\nAddress doesn't exist or is not accessible/");
                    
                  }
                }
            else
            {
            	txt.append("\nNetwork Interface for the specified address is not found");
            }
        }
        catch(UnknownHostException | SocketException ea)
        {
        }}    });	
		clear.setBounds(225,200,100,25);
	     clear.setFocusable(false);
	      clear.addActionListener(new ActionListener(){
	    public void actionPerformed(ActionEvent e) {
	    	txt.setText(" ");
	    }});
       display.setBounds(225,200,100,25);
       display.setFocusable(false);
       display.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    	       txt.append("\nDISPLAYING SUBNET INFORMATION\n");
    			txt.append("Ip address: ");
    			String ip=t1.getText();
    			txt.append(ip+"\n");
    			String split_ip[] = ip.split("\\."); //SPlit the string after every .
    			String split_bip[] = new String[4]; //split binary ip
    			String bip = "";
    			for(int i=0;i<4;i++){
    			split_bip[i] = appendZeros(Integer.toBinaryString(Integer.parseInt(split_ip[i])));
    			bip += split_bip[i];
    			}
    			txt.append("Binary Format "+bip+"\n");
    			String a=t2.getText();
    			int n=Integer.parseInt(a);
    			//Calculation of mask
    			int bits = (int)Math.ceil(Math.log(n)/Math.log(2));
    			int mask = 32-bits;
    			txt.append("Subnet mask = "+mask+"\n");
    			int fbip[] = new int[32];
    			for(int i=0; i<32;i++) fbip[i] = (int)bip.charAt(i)-48; //convert cahracter 0,1 to integer 0,1
    			for(int i=31;i>31-bits;i--)//Get first address by ANDing last n bits with 0
    			fbip[i] &= 0;
    			String fip[] = {"","","",""};
    			for(int i=0;i<32;i++)
    			fip[i/8] = new String(fip[i/8]+fbip[i]);
    			txt.append("\nNetwork address is  "+" :");
    			for(int i=0;i<4;i++){
    			txt.append(" "+Integer.parseInt(fip[i],2)+"");
    			if(i!=3) 
    				System.out.print(".");
    			txt.append(".");
    			}txt.append(" ");
    			int lbip[] = new int[32];
    			for(int i=0; i<32;i++) lbip[i] = (int)bip.charAt(i)-48; //convert character 0,1 to integer 0,1
    			for(int i=31;i>31-bits;i--)//Get last address by ORing last n bits with 1
    			lbip[i] |= 1;
    			String lip[] = {"","","",""};
    			for(int i=0;i<32;i++)
    			lip[i/8] = new String(lip[i/8]+lbip[i]);
    			txt.append("\nBroadcast address is = ");
    			for(int i=0;i<4;i++){
    				txt.append(""+Integer.parseInt(lip[i],2)+"");
    			if(i!=3) 
    				txt.append(". ");}
    			txt.append(" ");}});
        frame.add(l1);
        frame.add(t1);
        frame.add(l2);
        frame.add(t2);
        frame.add(display);
        frame.add(clear);
       	frame.add(map);
       	frame.add(txt);
       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       	frame.setSize(500,600);
       	frame.setLayout(new FlowLayout());
       	frame.setVisible(true);
       	frame.setResizable(false);
}
public  String appendZeros(String s){
String temp = new String("00000000");
return temp.substring(s.length())+ s;
}
}

