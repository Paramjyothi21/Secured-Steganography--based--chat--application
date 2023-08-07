//DecodeMessage.java
 import java.awt.image.*;
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import javax.imageio.*;
 
 public class DecodeMessage extends JFrame implements ActionListener
 {
 JButton open = new JButton("Open"), decode = new JButton("Decode"),
    reset = new JButton("Reset");
 JTextArea message = new JTextArea(10,3);
 BufferedImage image = null;
 JScrollPane imagePane = new JScrollPane();
 
 public DecodeMessage(BufferedImage BM) {
    super("Decode stegonographic message in image");
    image=BM;    
    assembleInterface();
    
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
    this.setBounds(500,100,500,500);
    this.setVisible(true);
    }
 
 private void assembleInterface() {
    JPanel p = new JPanel(new FlowLayout());
    p.add(open);
    p.add(decode);
    p.add(reset);
    this.getContentPane().add(p, BorderLayout.NORTH);
    open.addActionListener(this);
    decode.addActionListener(this);
    decode.setEnabled(false);
    reset.addActionListener(this);
    open.setMnemonic('O');
    decode.setMnemonic('D');
    reset.setMnemonic('R');
    
    p = new JPanel(new GridLayout(1,1));
    p.add(new JScrollPane(message));
    message.setFont(new Font("Arial",Font.BOLD,20));
    p.setBorder(BorderFactory.createTitledBorder("Decoded message"));
    message.setEditable(false);
    this.getContentPane().add(p, BorderLayout.SOUTH);
    
    imagePane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
   this.getContentPane().add(imagePane, BorderLayout.CENTER);
    }
 @Override
 public void actionPerformed(ActionEvent ae) {
    Object o = ae.getSource();
    if(o == open)
       openImage();
    else if(o == decode)
       decodeMessage();
    else if(o == reset) 
       resetInterface();
    }
 
 /*private java.io.File showFileDialog(boolean open) {
    JFileChooser fc = new JFileChooser("Open an image");
    javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
       @Override
       public boolean accept(java.io.File f) {
          String name = f.getName().toLowerCase();
          return f.isDirectory() ||   name.endsWith(".png") || name.endsWith(".bmp")|| name.endsWith(".jpg")|| name.endsWith(".jpeg");
          }
       @Override
       public String getDescription() {
          return "Image (*.png, *.bmp, *.jpg, *.jpeg)";
          }
       };
    fc.setAcceptAllFileFilterUsed(false);
    fc.addChoosableFileFilter(ff);
 
    java.io.File f = null;
    if(open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
       f = fc.getSelectedFile();
    else if(!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
       f = fc.getSelectedFile();
    return f;
   }*/
 
 private void openImage() {
    try {   
        if(image==null){
            JOptionPane.showMessageDialog(this, "No message has been selected !");
            return;
        }
       JLabel l = new JLabel(new ImageIcon(image));
      imagePane.getViewport().add(l);
      decode.setEnabled(true);
       this.validate();
       } catch(Exception ex) { ex.printStackTrace(); }
    }
 
 private void decodeMessage() {
    message.setText(decode(image));
    }
 
 
 private void resetInterface() {
    message.setText("");
    imagePane.getViewport().removeAll();
    image = null;
    this.validate();
    }
 	public String decode(BufferedImage img)
	{
            class OTEx extends Exception{
                String display(){
                    return "Incorrect OTP!";
                }
            }
		byte[] decod;
		try
		{
			decod = decode_text(get_byte_data(img));
                        String dc=new String(decod);
                        String otp="";
                        int Ch=(int)dc.charAt(dc.length()-1);
                        for(int i=dc.length()-2;i>=dc.length()-1-Ch;i--){
                            otp+=dc.charAt(i);
                        }
                        //otp recieved is reversed so i must reverse it again
                        int ci=otp.length()-1;
                        char[] ott=otp.toCharArray();
                        for(int i=0;i<otp.length();i++){
                            if(i<ci){
                                char ch=ott[i];
                                ott[i]=ott[ci];
                                ott[ci]=ch;
                                ci--;
                            }
                            else break;
                        }     
                        dc=dc.substring(0, dc.length()-otp.length()-1);
                        String oTp=JOptionPane.showInputDialog("Please enter the OTP!");
                        boolean match=true;
                        if(oTp.length()!=otp.length()){
                            match=false;
                        }
                        if(match){
                            for(int i=0;i<otp.length();i++){
                                if(oTp.charAt(i)!=ott[i]){
                                    match=false;
                                    break;
                                }
                            }         
                        }
                        if(!match){
                            throw new OTEx();
                            
                        }
			return(dc);
		}
                catch(OTEx ex){
                    JOptionPane.showMessageDialog(null, 
				ex.display(),"Error",
				JOptionPane.ERROR_MESSAGE);
                    return "";
                }
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"Image not found !","Error",
				JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}
	
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		for(int i=0; i<32; ++i)
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		byte[] result = new byte[length];
		
		for(int b=0; b<result.length; ++b )
		{
			for(int i=0; i<8; ++i, ++offset)
			{
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}
 
 /*public static void main(String arg[]) {
    new DecodeMessage();
    }*/
 }
