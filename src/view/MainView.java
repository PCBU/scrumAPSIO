package view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.MainController;

public class MainView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5739927886065724973L;

	private JTextArea contenu;
	private JFrame mainFrame;
	private JTextField commande;
	
	private MainController mainController;

	public MainView() {
		/**
		 * Initialisation du controller de commandes
		 */
		mainController = new MainController(this);
		
		/*
		 * Initialisation des composants de la vue
		 */
		mainFrame = new JFrame("Gestion des consultants");
		mainFrame.setLayout(new BorderLayout());
				
		contenu = new JTextArea();
		contenu.setEnabled(false);
		contenu.setEditable(false);				
		
		commande = new JTextField();
		
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
		mainFrame.add(contenu, BorderLayout.CENTER);		
		mainFrame.add(commande, BorderLayout.PAGE_END);
		
		//Initilisation des attributs de la vue
		
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(800, 400);
		mainFrame.setVisible(true);	
	}	
	
	/**
	 * Affiche le contenu sur l'ecran principal
	 * 
	 * @param contenu
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
