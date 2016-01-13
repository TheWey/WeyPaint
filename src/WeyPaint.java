

import java.awt.*;
import java.nio.*;
import java.util.Random;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;



public class WeyPaint extends JPanel implements KeyListener, MouseListener{

	//VARIABLE DECLARATION: DECLARE THEM NOW OR FOREVER HOLD YOUR PEACE
	
	/**
	 * WeyPaint
	 */
	private static final long serialVersionUID = 1L;

	public static Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
	
	public static ArrayList<Integer> x=new ArrayList<Integer>();
	public static ArrayList<Integer> y=new ArrayList<Integer>();
	public static ArrayList<Integer> width=new ArrayList<Integer>();
	public static ArrayList<Integer> height=new ArrayList<Integer>();
	
	public static ArrayList<Integer> linexone=new ArrayList<Integer>();
	public static ArrayList<Integer> linextwo=new ArrayList<Integer>();
	public static ArrayList<Integer> lineyone=new ArrayList<Integer>();
	public static ArrayList<Integer> lineytwo=new ArrayList<Integer>();
	
	public static ArrayList<Integer> printerx=new ArrayList<Integer>();
	public static ArrayList<Integer> printery=new ArrayList<Integer>();
	public static ArrayList<String> printerwords=new ArrayList<String>();
	public static ArrayList<Integer> printersize=new ArrayList<Integer>();
	
	public static ArrayList<String> undolist=new ArrayList<String>();
	
	String redoer="";
	
	public static Random rand=new Random();
	public static int randomnum;

	
	public static boolean drawmode=false;
	public static int drawstarty;
	public static int drawstartx;
	
	public static boolean linedrawmode=false;
	
	public static int circx=-100;
	public static int circy=-100;
	
	public static String printer="";
	public static int printersizer=0;
	
	public static boolean secretcolors=false;
	
	public static String fileloc;
	
	
	
	public static void main(String[] args) throws Exception{
		
		JOptionPane.showMessageDialog(null, "Welcome!\n\nInstructions:\n\n" +
				"-Click twice to create a line with the click points as endpoints\n" +
				"-Right click twice to create a rectangle with the click points as two corners\n" +
				"-Control-click to place text\n" +
				"-Press 'z' to undo the last object placement; as many as you have placed\n"+
                                "-Press 'x' to redo the last undo\n" +
				"-Press 'c' to clear the screen\n" +
				"-Press \"Esc\" for epilepsy!\n" +
				"-Press 'h' for help", "Welcome!", JOptionPane.DEFAULT_OPTION);
		String filepath;
		int fileques=JOptionPane.showConfirmDialog(null, "Would you like to open a file? Otherwise you will start fresh.", "File?", JOptionPane.YES_NO_OPTION);
		
		Path filepath2=FileSystems.getDefault().getPath(System.getProperty("user.home"));
		
		if (fileques==1){
			
			filepath=JOptionPane.showInputDialog("Please enter the directory to save your file, starting from your home directory.\nLeave this blank to place the save file directly in your home directory.\n" +
					"This starts from "+System.getProperty("user.home"));
			if (!filepath.startsWith(System.getProperty("user.home"))){
				filepath2=FileSystems.getDefault().getPath(System.getProperty("user.home"), filepath);
			}
			else {
				filepath2=FileSystems.getDefault().getPath(filepath);
			}
			
			filepath2=filepath2.normalize();
			Files.createDirectories(filepath2);
			filepath=JOptionPane.showInputDialog("Please enter your file name.\n" +
					"This will be put into the directrory "+filepath2.toString());
			if (!filepath.endsWith(".wey")){
				filepath=filepath+".wey";
			}
			filepath2=FileSystems.getDefault().getPath(filepath2.toString(), filepath);
			filepath2=filepath2.normalize();
			fileloc=filepath2.toString();
		}
		else if(fileques==0){
			
			boolean fileexists=false;
			String notexists="";
			while (!fileexists){
				fileloc=JOptionPane.showInputDialog(notexists+"Enter the location of your save file in your home directory.");
				if (!fileloc.startsWith(System.getProperty("user.home"))){
					fileloc=FileSystems.getDefault().getPath(System.getProperty("user.home"), fileloc).toString();
				}
				if (!fileloc.endsWith(".wey")) fileloc=fileloc+".wey";
				fileloc=FileSystems.getDefault().getPath(fileloc).normalize().toString();
				if (Files.exists(FileSystems.getDefault().getPath(fileloc))){
					fileexists=true;
				}
				else notexists="File not found! Try again.\n";
			}
			fileloc=FileSystems.getDefault().getPath(fileloc).normalize().toString();
			firstRead();
		}
		
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WeyPaint fin=new WeyPaint();
		jf.add(fin);
		jf.setSize(dim.width, dim.height);
		jf.setTitle("WeyPaint");
		jf.addMouseListener(fin);
		jf.addKeyListener(fin);
		jf.setVisible(true);
		
		
	}
	
	public void undo(){

		
		
		if (undolist.isEmpty()){
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Nothing to undo!", "", JOptionPane.ERROR_MESSAGE);
		}
		
		else{
			String ulist=undolist.get(undolist.size()-1);
                        redoer="";
			if (ulist.equalsIgnoreCase("text")){
				
				redoer="text;"+printerx.get(printerx.size()-1)+";"+printery.get(printery.size()-1)+";"+printersize.get(printersize.size()-1)+";"+printerwords.get(printerwords.size()-1);
				
				printerx.remove(printerx.size()-1);
				printery.remove(printery.size()-1);
				printersize.remove(printersize.size()-1);
				printerwords.remove(printerwords.size()-1);
				
				undolist.remove(undolist.size()-1);
			}
			else if (ulist.equalsIgnoreCase("box")){
				
				redoer="box;"+x.get(x.size()-1)+";"+y.get(y.size()-1)+";"+width.get(width.size()-1)+";"+height.get(height.size()-1);
				
				x.remove(x.size()-1);
				y.remove(y.size()-1);
				width.remove(width.size()-1);
				height.remove(height.size()-1);
				
				undolist.remove(undolist.size()-1);
			}
			else if (ulist.equalsIgnoreCase("line")){
				
				redoer="line;"+linexone.get(linexone.size()-1)+";"+linextwo.get(linextwo.size()-1)+";"+lineyone.get(lineyone.size()-1)+";"+lineytwo.get(lineytwo.size()-1);
				
				linexone.remove(linexone.size()-1);
				linextwo.remove(linextwo.size()-1);
				lineyone.remove(lineyone.size()-1);
				lineytwo.remove(lineytwo.size()-1);
				
				undolist.remove(undolist.size()-1);
			}
			
			System.out.println(undolist);
			repaint();
		}
	}
	
	public void redo(){
		//System.out.println("WEYBACON");
		String[] redo=redoer.split(";");
                redoer="";
		if (redo[0].equals("text")){
			undolist.add("text");
			
			printerx.add(Integer.parseInt(redo[1]));
			printery.add(Integer.parseInt(redo[2]));
			printersize.add(Integer.parseInt(redo[3]));
                        printerwords.add(redo[4]);
		}
		else if (redo[0].equals("box")){
			undolist.add("box");
			
			x.add(Integer.parseInt(redo[1]));
			y.add(Integer.parseInt(redo[2]));
			width.add(Integer.parseInt(redo[3]));
			height.add(Integer.parseInt(redo[4]));
		}
		else if (redo[0].equals("line")){
			undolist.add("line");
			
			linexone.add(Integer.parseInt(redo[1]));
			linextwo.add(Integer.parseInt(redo[2]));
			lineyone.add(Integer.parseInt(redo[3]));
			lineytwo.add(Integer.parseInt(redo[4]));
		}
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		
		g.setColor(Color.RED);
		g.fillOval(circx, circy, 10, 10);
		g.setColor(Color.BLACK);
		
			System.out.println("\n\nStart debug coordinates:\n");
			System.out.println("Rectangles:");
			for (int p=0;p<x.size();p++){
				
				g.drawRect((int) x.get(p), (int)y.get(p), (int) width.get(p), (int) height.get(p));
				System.out.println("X:"+x.get(p)+"  Y:"+y.get(p));
			
			}
			System.out.println("");
			System.out.println("Text Strings:");
			for (int k=0;k<printerx.size();k++){
				g.setFont(new Font("helvetica", Font.PLAIN, (int) printersize.get(k)));
				g.drawString((String) printerwords.get(k), (int) printerx.get(k), (int) printery.get(k));
				System.out.println("Text:"+printerwords.get(k)+" X:"+printerx.get(k)+" Y:"+printery.get(k));
			}
			System.out.println("Lines:");
			for (int a=0;a<lineytwo.size();a++){
				g.drawLine((int) linexone.get(a), (int)lineyone.get(a), (int) linextwo.get(a), (int) lineytwo.get(a));
				System.out.println("X1:"+linexone.get(a)+"  Y1:"+lineyone.get(a)+"  "+"X2:"+linextwo.get(a)+"  Y2:"+lineytwo.get(a));
			}
			
			try{
				save();
			}catch (Exception a){}//SAVES TEXT FILE OF COORDINATES
			
			
			System.out.println("End debug");
	}
	public WeyPaint(){}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
			if (!secretcolors){
				secretcolors=true;
				repaint();
				colors colorrun=new colors();
				colorrun.start();
			}
			else if (secretcolors){
				secretcolors=false;
			}
		}//end secret colors check
		
		if (e.getKeyCode()==KeyEvent.VK_C){
			int clearsure=JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the draw field?\nTHIS CAN NOT BE UNDONE!", "Clear?", JOptionPane.YES_NO_OPTION);
			
			if (clearsure==0){
				secretcolors=false;
				x.clear();
				y.clear();
				width.clear();
				height.clear();

				linexone.clear();
				lineyone.clear();
				linextwo.clear();
				lineytwo.clear();

				printerx.clear();
				printery.clear();
				printerwords.clear();
				printersize.clear();

				undolist.clear();
                                
                                redoer="";

				System.out.println("Screen Cleared");
				repaint();
			}
		}
		
		if (e.getKeyCode()==KeyEvent.VK_Z){
			undo();
		}
		if (e.getKeyCode()==KeyEvent.VK_H){
			helpmenu();
		}
		if (e.getKeyCode()==KeyEvent.VK_X){
			redo();
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int clickx=e.getX()-9+7;
		int clicky=e.getY()-30-3;
		circx=clickx-5;
		circy=clicky-5;
		if (e.isControlDown()){
			
			
			
			drawmode=false;
			if (linedrawmode){
				linexone.remove(linexone.size()-1);
				lineyone.remove(lineyone.size()-1);
				linedrawmode=false;
			}//fin draw mode resetters
			
			repaint();
			
			printer=JOptionPane.showInputDialog("Enter text to place in your art!");
			printersizer=Integer.parseInt(JOptionPane.showInputDialog("Enter a size for the text!"));
			
			if (!printer.equals("")){
				printersize.add(new Integer((int)printersizer));
				printerx.add(new Integer(clickx));
				printery.add(new Integer(clicky));
				printerwords.add(printer);
				
				undolist.add("text");
			}
			printersizer=0;
			circx=-100;
			circy=-100;
		}
		else if (e.isMetaDown()){
			
			if (linedrawmode){
				linexone.remove(linexone.size()-1);
				lineyone.remove(lineyone.size()-1);
				linedrawmode=false;
			}//end reset of line draw
			if (!drawmode){
				drawmode=true;
				drawstartx=clickx;
				drawstarty=clicky;
			}
			else{
				drawmode=false;
				undolist.add("box");
				if (clickx<drawstartx){
					x.add(new Integer(clickx));
					width.add(new Integer(drawstartx-clickx));
				}
				else{
					x.add(new Integer(drawstartx));
					width.add(new Integer(clickx-drawstartx));
				}
			
				if (clicky<drawstarty){
					y.add(new Integer(clicky));
					height.add(new Integer(drawstarty-clicky));
				}
				else{
					y.add(new Integer(drawstarty));
					height.add(new Integer(clicky-drawstarty));
				}
				circx=-100;
				circy=-100;
			}
		}//end box draw
		else{
			
			drawmode=false;
			if (!linedrawmode){
				linedrawmode=true;
				linexone.add(new Integer(clickx));
				lineyone.add(new Integer(clicky));
			}
			else{
				undolist.add("line");
				linedrawmode=false;
				linextwo.add(new Integer(clickx));
				lineytwo.add(new Integer(clicky));
				circx=-100;
				circy=-100;
			}
		}
		repaint();
		
	}//end click method
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseDragged(MouseEvent e){
		
	}
	
	
	public class colors extends Thread{
		public void run(){
			Graphics g=getGraphics();
			while (secretcolors){
					try{
						Thread.sleep(10);
					}catch (InterruptedException a){}
				for (int q=0;q<x.size();q++){
					
					randomnum=rand.nextInt(11);
					switch (randomnum){
						case 0:
							g.setColor(Color.blue);
							break;
						case 1:
							g.setColor(Color.cyan);
							break;
						case 2:
							g.setColor(Color.darkGray);
							break;
						case 3:
							g.setColor(Color.gray);
							break;
						case 4:
							g.setColor(Color.green);
							break;
						case 5:
							g.setColor(Color.lightGray);
							break;
						case 6:
							g.setColor(Color.magenta);
							break;
						case 7:
							g.setColor(Color.orange);
							break;
						case 8:
							g.setColor(Color.pink);
							break;
						case 9:
							g.setColor(Color.red);
							break;
						case 10:
							g.setColor(Color.white);
							break;
						case 11:
							g.setColor(Color.yellow);
							break;
						default:
							g.setColor(Color.black);
							break;
						}
						g.drawRect((int) x.get(q), (int)y.get(q), (int) width.get(q), (int) height.get(q));
					}//end for loop for rects
				
				
				for (int l=0;l<printerx.size();l++){
					randomnum=rand.nextInt(11);
					switch (randomnum){
					case 0:
						g.setColor(Color.blue);
						break;
					case 1:
						g.setColor(Color.cyan);
						break;
					case 2:
						g.setColor(Color.darkGray);
						break;
					case 3:
						g.setColor(Color.gray);
						break;
					case 4:
						g.setColor(Color.green);
						break;
					case 5:
						g.setColor(Color.lightGray);
						break;
					case 6:
						g.setColor(Color.magenta);
						break;
					case 7:
						g.setColor(Color.orange);
						break;
					case 8:
						g.setColor(Color.pink);
						break;
					case 9:
						g.setColor(Color.red);
						break;
					case 10:
						g.setColor(Color.white);
						break;
					case 11:
						g.setColor(Color.yellow);
						break;
					default:
						g.setColor(Color.black);
						break;
					}
					g.setFont(new Font("helvetica", Font.PLAIN, (int) printersize.get(l)));
					g.drawString((String) printerwords.get(l), (int) printerx.get(l), (int) printery.get(l));
				}//end for loop for words
				
				
				for (int p=0;p<lineytwo.size();p++){
					randomnum=rand.nextInt(11);
					switch (randomnum){
					case 0:
						g.setColor(Color.blue);
						break;
					case 1:
						g.setColor(Color.cyan);
						break;
					case 2:
						g.setColor(Color.darkGray);
						break;
					case 3:
						g.setColor(Color.gray);
						break;
					case 4:
						g.setColor(Color.green);
						break;
					case 5:
						g.setColor(Color.lightGray);
						break;
					case 6:
						g.setColor(Color.magenta);
						break;
					case 7:
						g.setColor(Color.orange);
						break;
					case 8:
						g.setColor(Color.pink);
						break;
					case 9:
						g.setColor(Color.red);
						break;
					case 10:
						g.setColor(Color.white);
						break;
					case 11:
						g.setColor(Color.yellow);
						break;
					default:
						g.setColor(Color.black);
						break;
					}
					g.drawLine((int) linexone.get(p), (int)lineyone.get(p), (int) linextwo.get(p), (int) lineytwo.get(p));
				}//end for loop for lines
				
			}//end loop while
			
			repaint();
		}//end run
	}//end thread

	public static void helpmenu(){
		JOptionPane.showMessageDialog(null, "Help Menu:\n\n" +
				"-Click twice to create a line with the click points as endpoints\n" +
				"-Right click twice to create a rectangle with the click points as two corners\n" +
				"-Control-click to place text\n" +
				"-Press 'z' to undo the last object placement; as many as you have placed\n"+
                                "-Press 'x' to redo the last undo\n" +
				"-Press 'c' to clear the screen\n" +
				"-Press \"Esc\" for epilepsy!\n" +
				"-Press 'h' for help", "Help Menu", JOptionPane.QUESTION_MESSAGE);
	}
	
	public static void save() throws Exception{
		
		BufferedWriter write=new BufferedWriter(new FileWriter(fileloc));//box, then line, then words
		
		String writer="";
		
		//BOX
		if (!x.isEmpty()){
			write.write("box");
			write.newLine();

			for (int h=0;h<x.size();h++){//box x
				writer=writer+" "+x.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<y.size();h++){//box y
				writer=writer+" "+y.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<width.size();h++){//box width
				writer=writer+" "+width.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<height.size();h++){//box height
				writer=writer+" "+height.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";
		}
		
		//LINE
		if (!linexone.isEmpty()){
			write.write("line");
			write.newLine();

			for (int h=0;h<linexone.size();h++){//linexone
				writer=writer+" "+linexone.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<linextwo.size();h++){//linextwo
				writer=writer+" "+linextwo.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<lineyone.size();h++){//lineyone
				writer=writer+" "+lineyone.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<lineytwo.size();h++){//lineytwo
				writer=writer+" "+lineytwo.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";
		}
		
		//TEXT
		if (!printerx.isEmpty()){
			write.write("text");
			write.newLine();

			for (int h=0;h<printerx.size();h++){//printerx
				writer=writer+" "+printerx.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<printery.size();h++){//printery
				writer=writer+" "+printery.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<printerwords.size();h++){//printerwords
				writer=writer+";"+printerwords.get(h);
			}
                        writer=writer.replaceFirst(";", "");
			write.write(writer);
			write.newLine();
			writer="";

			for (int h=0;h<printersize.size();h++){//printersize
				writer=writer+" "+printersize.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";
		}
		
		//UNDO
		if (!undolist.isEmpty()){
			write.write("undo");
			write.newLine();

			for (int h=0;h<undolist.size();h++){//undolist
				writer=writer+" "+undolist.get(h);
			}
			writer=writer.replaceFirst(" ", "");
			write.write(writer);
			write.newLine();
			writer="";
		}
		
		
		write.close();
	}//END OF SAVE...ALMOST DONE WITH THE MADNESS
	
	public static void firstRead() throws Exception{
		BufferedReader read=new BufferedReader(new FileReader(fileloc));//box, then line, then words, then undo
		
		String reader="";
		String readname="";
		
		
		readname=readname+read.readLine();
		
		//BOX
		if (readname.equals("box")){
			reader=read.readLine();//box x
			String[] xread=reader.split(" ");
			for (int d=0;d<xread.length;d++){
				x.add(Integer.parseInt(xread[d]));
			}

			reader=read.readLine();//box y
			String[] yread=reader.split(" ");
			for (int d=0;d<yread.length;d++){
				y.add(Integer.parseInt(yread[d]));
			}

			reader=read.readLine();//box width
			String[] widthread=reader.split(" ");
			for (int d=0;d<widthread.length;d++){
				width.add(Integer.parseInt(widthread[d]));
			}

			reader=read.readLine();//box height
			String[] heightread=reader.split(" ");
			for (int d=0;d<heightread.length;d++){
				height.add(Integer.parseInt(heightread[d]));
			}
			readname=read.readLine();
		}


		//LINE
		if (readname.equals("line")){
			reader=read.readLine();//linexone
			String[] linexoneread=reader.split(" ");
			for (int d=0;d<linexoneread.length;d++){
				linexone.add(Integer.parseInt(linexoneread[d]));
			}

			reader=read.readLine();//linextwo
			String[] linextworead=reader.split(" ");
			for (int d=0;d<linextworead.length;d++){
				linextwo.add(Integer.parseInt(linextworead[d]));
			}

			reader=read.readLine();//lineyone
			String[] lineyoneread=reader.split(" ");
			for (int d=0;d<lineyoneread.length;d++){
				lineyone.add(Integer.parseInt(lineyoneread[d]));
			}

			reader=read.readLine();//lineytwo
			String[] lineytworead=reader.split(" ");
			for (int d=0;d<lineytworead.length;d++){
				lineytwo.add(Integer.parseInt(lineytworead[d]));
			}
			readname=read.readLine();
		}


		//TEXT
		if (readname.equals("text")){
			reader=read.readLine();//printerx
			String[] printerxread=reader.split(" ");
			for (int d=0;d<printerxread.length;d++){
				printerx.add(Integer.parseInt(printerxread[d]));
			}

			reader=read.readLine();//printery
			String[] printeryread=reader.split(" ");
			for (int d=0;d<printeryread.length;d++){
				printery.add(Integer.parseInt(printeryread[d]));
			}

			reader=read.readLine();//printerwords
			String[] printerwordsread=reader.split(";");
			for (int d=0;d<printerwordsread.length;d++){
				printerwords.add(printerwordsread[d]);
			}

			reader=read.readLine();//printersize
			String[] printersizeread=reader.split(" ");
			for (int d=0;d<printersizeread.length;d++){
				printersize.add(Integer.parseInt(printersizeread[d]));
			}
			readname=read.readLine();
		}

		//UNDO
		if (readname.equals("undo")){
			reader=read.readLine();//undolist
			String[] undolistread=reader.split(" ");
			for (int d=0;d<undolistread.length;d++){
				undolist.add(undolistread[d]);
			}
		}
	}//END READ...YES I'M DONE!!
	
	
}//end main class