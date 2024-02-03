import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
public class Fenetre extends JFrame {
	private JMenuBar menuBar = new JMenuBar();
	private JPopupMenu jpm = new JPopupMenu();
	private JToolBar toolBar = new JToolBar();
	private JMenu fichier = new JMenu("Fichier"),
			 formepointeur = new JMenu("Forme du pointeur"),
             couleurpointeur = new JMenu("Couleur du pointeur"),
	              edition = new JMenu("Edition");
	private JMenuItem effacer = new JMenuItem("effacer"),
			          annuler = new JMenuItem("Annuler"),
			          quiter = new JMenuItem("quitter"),
	                  rond = new JMenuItem("Rond"),
	                  carre= new JMenuItem("Carré"),
	                  rouge= new JMenuItem("Rouge"),
	                  vert = new JMenuItem("Vert"),
	                		  enregistrer = new JMenuItem("Enregistrer"),
	                		  enregistrerSous = new JMenuItem("Enregistrer Sous"),
	                		  ouvrir = new JMenuItem("Ouvrir"),
	                  bleu = new JMenuItem("Bleu");
	private JButton square = new JButton(new ImageIcon("images/carre.png")),
	                circle = new JButton(new ImageIcon("images/rond.png")),
	                red    = new JButton(new ImageIcon("images/rouge.png")),
	                green  = new JButton(new ImageIcon("images/vert.png")),
	                blue    = new JButton(new ImageIcon("images/bleu.png"));
	 private Color fondBouton = Color.white;
	private DrawPanel Dpan = new DrawPanel();
	private JPanel container = new JPanel();
	private String mousePointerStatus = "rond";
	private FormePointerListener fpl = new FormePointerListener();
	//File Operation
	JFileChooser fileChooser = new JFileChooser("backup/");
	//Nos filtres
	ZFileFilter filtre = new ZFileFilter(".amz", "Fichier Ardoise Mazique");
	File file;
Fenetre()
{
	this.setTitle("Ardoise Magique");
	this.setSize(700, 500);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLocationRelativeTo(null);
	container.setLayout( new BorderLayout());
	Dpan.addMouseMotionListener(new PointerMoveListener());
	Dpan.addMouseListener( new DpanListener());
	container.add(Dpan,BorderLayout.CENTER);
	this.fileChooser.addChoosableFileFilter(filtre);
	this.setContentPane(container);
	this.initMenu();
	this.initToolBar();
	this.setVisible(true);
}
private void initMenu() {
	effacer.addActionListener(new ActionListener()
			{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Dpan.erase();
		}
			});
	fichier.add(effacer);
	fichier.addSeparator();
	fichier.add(quiter);
	annuler.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Dpan.getPoints().remove(0);
			Dpan.repaint();
		}
	});
	annuler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
	fichier.add(annuler);
	rond.addActionListener(fpl);
	formepointeur.add(rond);
	carre.addActionListener(fpl);
	formepointeur.add(carre);
	edition.add(formepointeur);
	rouge.addActionListener(new CouleurListener());
	couleurpointeur.add(rouge);
	vert.addActionListener(new CouleurListener());
	couleurpointeur.add(vert);
	bleu.addActionListener(new CouleurListener());
	couleurpointeur.add(bleu);
	edition.add(couleurpointeur);
	fichier.setMnemonic('F');
	edition.setMnemonic('E');
	effacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK));
	quiter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
	quiter.addActionListener(new ActionListener() {
		public void actionPerformed (ActionEvent event){
			System.exit(0);
			}
	});
	menuBar.add(fichier);
	menuBar.add(edition);
	this.setJMenuBar(menuBar);
	enregistrer.addActionListener(new ActionListener(){
		public void actionPerformed (ActionEvent arg0) {
		ObjectOutputStream oos;
		//S'il ne s'agit pas du premier enregistrement !
		if(file != null){
		try {
		oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(Dpan.getPoints());
		oos.close();
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		//Sinon on demande le nom du fichier
		else{
		if(fileChooser.showSaveDialog(null) ==
		JFileChooser.APPROVE_OPTION){
		file = fileChooser.getSelectedFile();
		//Si l'extension est valide
		System.out.println(fileChooser.getFileFilter());
		if(filtre.accept(file))
		{
			System.out.println("OK");
		try {
		oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(Dpan.getPoints());
		oos.close();
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		else{
			System.out.println("faux");
		//Si vous n'avez pas spécifié une extension valide !
		JOptionPane alert = new JOptionPane();
		alert.showMessageDialog(null, "Erreur d'extension de fichier ! \nVotre sauvegarde a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		}
		}
		}
		});
		fichier.add(enregistrer);
		enregistrerSous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
				//On détermine l'action à faire !
				enregistrerSous.addActionListener(new ActionListener(){
				public void actionPerformed (ActionEvent arg0) {
				if(fileChooser.showSaveDialog(null) ==
				JFileChooser.APPROVE_OPTION){
				file = fileChooser.getSelectedFile();
				//Si l'extension est valide
				if(filtre.accept(file))
				{
				try {
				ObjectOutputStream oos = new ObjectOutputStream(new
				FileOutputStream(file));
				oos.writeObject(Dpan.getPoints());
				oos.close();
				} catch (FileNotFoundException e) {
				e.printStackTrace();
				} catch (IOException e) {
				e.printStackTrace();
				}
				}
				else{
				//Si vous n'avez pas spécifié une extension valide !
				JOptionPane alert = new JOptionPane();
				alert.showMessageDialog(null, "Erreur d'extension de fichier ! \nVotre sauvegarde a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				}
				}
				});
				fichier.add(enregistrerSous);
				ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_DOWN_MASK));
				ouvrir.addActionListener(new ActionListener(){
				public void actionPerformed (ActionEvent e){
				if(fileChooser.showOpenDialog(null) ==JFileChooser.APPROVE_OPTION){
				file = fileChooser.getSelectedFile();
				if(filtre.accept(file))
				{
					System.out.println("Open");
				try {
				ObjectInputStream ois = new ObjectInputStream(new
				FileInputStream(file));
				Dpan.setPoints((ArrayList<Point>)ois.readObject());
				ois.close();
				} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				} catch (IOException e1) {
				e1.printStackTrace();
				} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
				}
				}
				else{
					System.out.println("OpenError");
				JOptionPane alert = new JOptionPane();
				alert.showMessageDialog(null, "Erreur d'extension de fichier ! \nVotre chargement a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
				}
				}
				});
				fichier.add(ouvrir);
}
private void initToolBar(){
	this.square.setBackground(fondBouton);
	this.square.addActionListener(fpl);
	this.toolBar.add(square);
	this.circle.setBackground(fondBouton);
	this.circle.addActionListener(fpl);
	this.toolBar.add(circle);
	this.toolBar.addSeparator();
	this.red.setBackground(fondBouton);
	this.red.addActionListener(new CouleurListener());
	this.toolBar.add(red);
	this.green.setBackground(fondBouton);
	this.green.addActionListener(new CouleurListener());
	this.toolBar.add(green);
	this.blue.setBackground(fondBouton);
	this.blue.addActionListener(new CouleurListener());
	this.toolBar.add(blue);
	this.add(toolBar, BorderLayout.NORTH);
}
class FormePointerListener implements ActionListener{
	public void actionPerformed (ActionEvent e) {
		if(e.getSource().getClass().getName().equals("javax.swing.JMenuItem")){
			if(e.getSource()==carre)Dpan.setFormePointer("carre");
			else Dpan.setFormePointer("rond");
			}
			else{
			if(e.getSource()==square)Dpan.setFormePointer("carre");
			else Dpan.setFormePointer("rond");
			}
	
	}
	}
class CouleurListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().getClass().getName().equals("javax.swing.JMenuItem")){
			System.out.println("OK !");
			if(e.getSource()==vert)Dpan.setCouleurPointer(Color.green);
			else if(e.getSource()==bleu)Dpan.setCouleurPointer(Color.blue);
			else Dpan.setCouleurPointer(Color.red);
			}
			else{
			if(e.getSource()==green)Dpan.setCouleurPointer(Color.green);
			else if(e.getSource()==blue)Dpan.setCouleurPointer(Color.blue);
			else Dpan.setCouleurPointer(Color.red);
			}
	}
}
class DpanListener implements MouseListener{
	public void mouseClicked(MouseEvent event) {
		
	}
	/**
	* Méthode appelée lors du survol de la souris
	*/
	public void mouseEntered(MouseEvent event) {
		
	}
	/**
	* Méthode appelée lorsque la souris sort de la zone du bouton
	*/
	public void mouseExited(MouseEvent event) {
	}
	/**
	* Méthode appelée lorsque l'on presse le clic gauche de la souris
	*/
	public void mousePressed(MouseEvent e) {
		Dpan.getPoints().add(new Point(e.getX() - (10/ 2), e.getY() -(10 / 2), 10,Dpan.getCouleurPointer(), Dpan.getFormePointer()));
		Dpan.repaint();
	}
	/**
	* Méthode appelée lorsque l'on relâche le clic de souris
	*/
	public void mouseReleased(MouseEvent event) {
		Dpan.setLeaveMouseStatus(true);
		Dpan.setClickGaucheStatus(false);
		if(event.isPopupTrigger())
		{
		jpm.add(effacer);
		jpm.add(quiter);
		jpm.add(formepointeur);
		jpm.add(couleurpointeur);
		//La méthode qui va afficher le menu
		jpm.show(Dpan, event.getX(), event.getY());
		}
	}}
		class PointerMoveListener implements MouseMotionListener
		{
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Dpan.getPoints().add(new Point(e.getX() - (10 / 2), e.getY() -(10 / 2), 10, Dpan.getCouleurPointer(), Dpan.getFormePointer()));
				Dpan.repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		
}
