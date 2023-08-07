//EmbedMessage.java
 import java.awt.image.*;
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import javax.imageio.*;
 
 public class EmbedMessage extends JFrame implements ActionListener
 {
 JButton open = new JButton("Open"), embed = new JButton("Embed"),
    set = new JButton("Done"), reset = new JButton("Reset");
 JTextArea message = new JTextArea(10,3);
 BufferedImage sourceImage = null, embeddedImage = null;
 JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
 JScrollPane originalPane = new JScrollPane(),
    embeddedPane = new JScrollPane();
 Serv SER=null;
 Clin CLI=null;
 String secur=null;
 
 public EmbedMessage(Object iim,String sec) {
     
    super("Embed stegonographic message in image");
    secur=sec;
    if(iim instanceof Serv){
        SER=(Serv)iim;
    }
    else{
        CLI=(Clin)iim;
    }
    assembleInterface();
    
    this.setBounds(500,100,500, 500);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
    this.setVisible(true);
    sp.setDividerLocation(0.5);
    this.validate();
    }
 
 private void assembleInterface() {
    JPanel p = new JPanel(new FlowLayout());
    p.add(open);
    p.add(embed);
    p.add(set);   
    p.add(reset);
    this.getContentPane().add(p, BorderLayout.SOUTH);
    open.addActionListener(this);
    embed.addActionListener(this);
    set.addActionListener(this);   
    reset.addActionListener(this);
    open.setMnemonic('O');
    embed.setMnemonic('E');
    set.setMnemonic('S');
    reset.setMnemonic('R');
    
    p = new JPanel(new GridLayout(1,1));
    p.add(new JScrollPane(message));
    message.setFont(new Font("Arial",Font.BOLD,20));
    p.setBorder(BorderFactory.createTitledBorder("Message to be embedded"));
    this.getContentPane().add(p, BorderLayout.NORTH);
    
    sp.setLeftComponent(originalPane);
    sp.setRightComponent(embeddedPane);
    originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));
    embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
    this.getContentPane().add(sp, BorderLayout.CENTER);
    }
 
 @Override
 public void actionPerformed(ActionEvent ae) {
    Object o = ae.getSource();
    if(o == open)
       openImage();
    else if(o == embed)
       embedMessage();
    else if(o == set){ 
       setImage();
       this.dispose();
    }
    else if(o == reset) 
       resetInterface();
    }
 
 private java.io.File showFileDialog(final boolean open) {
    JFileChooser fc = new JFileChooser("Open an image");
    javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
       @Override
       public boolean accept(java.io.File f) {
          String name = f.getName().toLowerCase();
          if(open)
             return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".tiff") ||
                name.endsWith(".bmp") || name.endsWith(".dib");
          return f.isDirectory() || name.endsWith(".png") ||    name.endsWith(".bmp");
          }
       @Override
       public String getDescription() {
          if(open)
             return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
          return "Image (*.png, *.bmp)";
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
    }
 
 private void openImage() {
    java.io.File f = showFileDialog(true);
    try {   
       sourceImage = ImageIO.read(f);
       JLabel l = new JLabel(new ImageIcon(sourceImage));
       originalPane.getViewport().add(l);
       this.validate();
       } catch(Exception ex) { ex.printStackTrace(); }
    }
 
 private void embedMessage() {
     if(sourceImage==null){
         JOptionPane.showMessageDialog(this, "Please choose an image !");
         return;
     }
    String mess = message.getText();
    embeddedImage = sourceImage;
    if(encode(embeddedImage, mess)){
        JLabel l = new JLabel(new ImageIcon(embeddedImage));
        embeddedPane.getViewport().add(l);
        this.validate();
    }
    else{
        embeddedImage=null;
    }
    }
 
 private void setImage() {
    if(embeddedImage == null) {
       JOptionPane.showMessageDialog(this, "No message has been embedded!", 
         "Nothing to send", JOptionPane.ERROR_MESSAGE);
       return;
      }
    if(SER!=null){
        SER.Bufim=embeddedImage;
        SER.setlabel.setText("Image is ready to be sent!");
    }
    else{
        CLI.Bufim=embeddedImage;
        CLI.setlabel.setText("Image is ready to be sent!");
    }
    
    
   /*java.io.File f = showFileDialog(false);
    String name = f.getName();
    String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();
    if(!ext.equals("png") && !ext.equals("bmp") &&   !ext.equals("dib")) {
          ext = "png";
          f = new java.io.File(f.getAbsolutePath()+".png");
          }
    try {
       if(f.exists()) f.delete();
       ImageIO.write(embeddedImage, ext.toUpperCase(), f);
       } catch(Exception ex) { ex.printStackTrace(); }*/
    }

 private void resetInterface() {
    message.setText("");
    originalPane.getViewport().removeAll();
    embeddedPane.getViewport().removeAll();
    sourceImage = null;
    embeddedImage = null;
    sp.setDividerLocation(0.5);
    this.validate();
    }
 	public boolean encode(BufferedImage img, String message)
	{
		return add_text(img,message);
	}
	private boolean add_text(BufferedImage image, String text)
	{
                String s=secur;
                text+=s;
                char ln=(char)s.length();
                text+=ln;
		byte img[]  = get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0);
			encode_text(img, msg, 32);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
                        return false;
		}
		return true;
	}
	
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	private byte[] bit_conversion(int i)
	{
		byte byte3 = (byte)((i & 0xFF000000) >>> 24);
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16);
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 );
		byte byte0 = (byte)((i & 0x000000FF)	   );
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		for(int i=0; i<addition.length; ++i)
		{
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset)
			{
				int b = (add >>> bit) & 1;
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}
 
 /*public static void main(String arg[]) {
    new EmbedMessage();
    }*/
}
