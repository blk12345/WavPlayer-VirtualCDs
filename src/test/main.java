package test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.Font;
import javax.swing.event.ListSelectionListener;



import javax.swing.event.ListSelectionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

public class main {
	
	 public static boolean streaming = true;
	public static Long current;
	 public static Clip clip = null;
	 public static String status;
	 public static String lastAudio = "";
	 public static AudioInputStream stream = null;
	 public static boolean playing = false;
	 public static int songplays = 0;
	 public static boolean albumfinished= true;
	 public static String file = "";
	 public static int songnum = 0;
	 public static int num = 0;
	 public static int length = 1;
	 public static String filename = "";
	 public static boolean stopped = false;
	 
	 private boolean uppressed=false;
		private boolean downpressed=false;
	 
	public static File album = new File("");
	public static File[] songs = album.listFiles();
	public static String library = "album";
	 public static File folder = new File(library);
	 public static File[] list = folder.listFiles();
	 public static boolean skipped = true;
	private JFrame frame;
	private JPanel panel;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frame.setVisible(true);
					ImageIcon icon = new ImageIcon("cd.jpg");
					
					window.frame.setIconImage(icon.getImage());
					window.frame.setTitle("Virtual Cds!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		thread i = new thread();
		initialize();
		i.start();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		panel = new JPanel();
		frame.setBounds(100, 100, 392, 212);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DownAction s = new DownAction();
		DownReleased nons = new DownReleased();
		 panel.getInputMap().put(KeyStroke.getKeyStroke("S"), "s");
		panel.getActionMap().put("s", s);
		panel.getInputMap().put(KeyStroke.getKeyStroke("released S"), "nons");
		panel.getActionMap().put("nons", nons);
		
		JLabel time = new JLabel(" ");
		time.setBounds(63, 52, 45, 13);
		frame.getContentPane().add(time);
		if(clip!=null) {
			pauseAudio();
		}
		
		
		current=0L;
		try {
			stream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		
		Timer timer2 = new Timer(1, new ActionListener() {
	        public void actionPerformed(ActionEvent a) {
	        	if(downpressed==true) {
	        		playAlbum(filename, songnum);
	        	}
	        }
	        });timer2.start();
		
		Timer timer = new Timer(clip.getFrameLength(), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               num++;
               
               
                	time.setText(Integer.toString(num/60));               		
                	
                	folder = new File(library);
                	list = folder.listFiles();
                	
                	if(songnum<list.length) {
	                	if(clip.isActive()==false) {
	                		try {
	                		playAlbum(filename, songnum);
	                		
	                		}catch(Exception e1) {
	                			System.out.println(e1);
	                		}
	                			
	                				songnum++;
	                			
	                		num = 0;
	                		
	                	}
	                	}
                	if(songnum==list.length) {
                		songnum = 0;
                	}
                
                
            }
        });
		
		DefaultListModel d = new DefaultListModel();
		
		if(list!=null) {
			for (int i=0; i< list.length;i++) {
		
			d.addElement(list[i]);
		}
		}
		JList list_1 = new JList(d);
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				timer.stop();
				filename = list[list_1.getSelectedIndex()].toString();
				
			}
		});
		 JScrollPane scrollPane = new JScrollPane(list_1);
		 scrollPane.setSize(200, 120);
		 scrollPane.setLocation(170, 50);
	      Container contentPane = frame.getContentPane();
	      contentPane.add(scrollPane, BorderLayout.CENTER);
	
		
		JButton btnNewButton_2 = new JButton("play/restart");
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					
					
					songnum=0;
					timer.stop();
					stopAudio();
					
			        if(clip.isActive()==false) {
			        	timer.start();
			        }
			        timer.restart();
			        	
				}});    
			     
			frame.getContentPane().setLayout(null);
			btnNewButton_2.setBounds(10, 67, 151, 31);
			frame.getContentPane().add(btnNewButton_2);
			
			   JButton btnNewButton_1 = new JButton("pause/resume");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						
						if(stopped==false) {
							pauseAudio();
							timer.stop();
							stopped=true;
						}else {
							resumeAudio();
							timer.start();
							stopped=false;
						}
						
						
						
					}
				});
				btnNewButton_1.setBounds(10, 122, 151, 31);
				frame.getContentPane().add(btnNewButton_1);
				
		        
			
		
			
			JButton btnNewButton = new JButton(">");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
						stopAudio();
					
					
				}
			});
			btnNewButton.setBounds(89, 98, 72, 21);
			frame.getContentPane().add(btnNewButton);
			
			
			
			
			
			JButton btnNewButton_3 = new JButton("change directory");
			btnNewButton_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					library = textField.getText();
					folder = new File(library);
					list = folder.listFiles();
					
				
						d.removeAllElements();
					
					for (int i=0; i< list.length;i++) {
						d.addElement(list[i]);
					}
					
					}
			});
			btnNewButton_3.setBounds(214, 21, 156, 21);
			frame.getContentPane().add(btnNewButton_3);
			
			JButton btnNewButton_4 = new JButton("<");
			btnNewButton_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					stopAudio();
					songnum-=2;
				}
			});
			btnNewButton_4.setBounds(10, 98, 76, 21);
			frame.getContentPane().add(btnNewButton_4);
			
			textField = new JTextField();
			textField.setBounds(10, 28, 200, 19);
			frame.getContentPane().add(textField);
			textField.setColumns(10);
			
			
			
			
			
			
			
			
			
		            
			
}
	


public static void playAudio(String file) {
		
		playing = true;
		current=0L;
		try {
			stream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		if(stream!=null) {
			
		
			try {
				clip.open(stream);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				}
		
		
		clip.loop(0);
		clip.start();
		}
		lastAudio = file;
		
		}
	public static void stopAudio() {
		clip.stop();
		clip.close();
		albumfinished = true;
		streaming=false;
	}
	public static void pauseAudio() {
		current = clip.getMicrosecondPosition();
		clip.stop();
		streaming = false;
	}
	public static void resumeAudio() {
		clip.close();
		
		try {
			stream = AudioSystem.getAudioInputStream(new File(lastAudio).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1);
		}
		
		try {
			clip.open(stream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		clip.setMicrosecondPosition(current);
		clip.start();
		clip.loop(0);
		
		
		
	}
	public static void getAlbum(String name) {
		String song= "";
		folder = new File(name);
		list = folder.listFiles();
		for (int i = 0; i < list.length; i++) {
			  if (list[i].isFile()) {
			    System.out.println("File " + list[i].getName());
			  } else if (list[i].isDirectory()) {
			    System.out.println("Directory " + list[i].getName());
			  }
			}//andrei Suvorkov
	}
	public static void playAlbum(String name, int i) {
		getAlbum(name);
		albumfinished = false;
		
			
			
			if(clip!=null) {
			
				    playAudio(name +"/"+ list[i].getName());
				   
			  
		}
	

}
	public static void clock(int ms) {
		int seconds = ms/60;
		int minutes = seconds/60;
		int hours = minutes/60;
		
	}
	class DownAction extends AbstractAction{
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			downpressed=true;
			//leftpressed=false;
		}
		
	}
	class DownReleased extends AbstractAction{

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		downpressed=false;
		//leftpressed=false;
	}

	}
}
