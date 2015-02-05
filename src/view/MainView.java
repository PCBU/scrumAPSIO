package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controller.MainController;

public class MainView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5739927886065724973L;

	private JTextArea contenu;
	private JFrame mainFrame;
	private JTextField commande;
	private JButton Bouton;
	private MainController mainController;

	public MainView() {
		/**
		 * Initialisation du controller de commandes
		 */
		mainController = new MainController(this);
		
		/*
		 * Initialisation des composants de la vue
		 */
		MouseListener interact = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainController.commande(commande.getText());
				commande.setText("");
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		};

		mainFrame = new JFrame("Gestion des consultants");
		mainFrame.setLayout(new BorderLayout());
		JPanel P1 = new JPanel(new BorderLayout());
		contenu = new JTextArea();
		contenu.setEnabled(true);
		contenu.setEditable(false);				
		
		commande = new JTextField();
		Bouton = new JButton("Envoyer");
		Bouton.addMouseListener(interact);

		//Initialisation des evenements du textfield
		commande.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				String key = arg0.getKeyChar()+"";				
				if(key.equals("\n")) 
				{
					mainController.commande(commande.getText());
					commande.setText("");
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {			
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		/*
		 * Ajout des composants a la vue
		 */
		P1.add(commande, BorderLayout.CENTER);
		mainFrame.add(contenu, BorderLayout.CENTER);		
		mainFrame.add(P1, BorderLayout.PAGE_END);
		P1.add(Bouton, BorderLayout.EAST);
		//Initilisation des attributs de la vue

		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(800, 400);
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);
	}

	/**
	 * Affiche le contenu sur l'ecran principal
	 * 
	 * @param message
	 */
	public void afficher(String message)
	{
		//"\n" permet un retour a la ligne
		contenu.append(message+"\n");
		contenu.repaint();
	}	
	
	/**
	 * Efface tout ce qui est affiché sur l'ecran principal
	 */
	public void effacer()
	{
		contenu.setText("");
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainView frame = new MainView();		
	}
}
