package vue;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import modele.Agent;
import modele.Environnement;
import modele.Piece;

public class Carte extends JFrame implements Runnable {

	private Agent robot;
	private JLabel[][] pieces;
	private int longueur;
	private int largeur;
	private Environnement env;

	public Carte(Environnement env) {
		super("Plan du manoir");
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.env = env;

		longueur = env.getLongueur();
		largeur = env.getLargeur();

		this.pieces = new JLabel[longueur][largeur];

		this.setLayout(new GridLayout(longueur, largeur));

		for(int i = 0 ; i < longueur ; i++)
			for (int j = 0 ; j < largeur ; j++) {
				JLabel s = new JLabel();
				s.setIcon(new ImageIcon("images/rien.png")); 
				this.add(s);
				pieces[i][j] = s;
			}

		this.pack();
		this.setVisible(true);
	}

	public JLabel[][] getPieces() {
		return pieces;
	}

	public void afficher() {
		this.pack();
		this.setVisible(true);
		while (true) {
			int i = env.getOrdonnee();
			int j = env.getAbscisse();

			Piece p = env.getPieces()[i][j];
			
			if (p.getBijou() && p.getPoussiere()) {
				this.pieces[i][j].setIcon(new ImageIcon("images/deux.png"));
			} else if (p.getBijou()) {
				this.pieces[i][j].setIcon(new ImageIcon("images/bijou.png"));
			} else if (p.getPoussiere()) {
				this.pieces[i][j].setIcon(new ImageIcon("images/poussiere.png"));
			}

			this.repaint();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Carte c = new Carte(new Environnement(10,10));

		Thread thread = new Thread(c);
		thread.start();
	}

	@Override
	public void run() {
		Thread thread = new Thread(env);
		thread.start();
		this.afficher();
	}
}

