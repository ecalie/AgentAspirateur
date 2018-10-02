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

	public Carte(Environnement env, Agent robot) {
		super("Plan du manoir");
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.env = env;
		this.robot = robot;

		longueur = env.getLongueur();
		largeur = env.getLargeur();

		this.pieces = new JLabel[largeur][longueur];

		this.setLayout(new GridLayout(longueur, largeur));

		for(int j = 0 ; j < longueur ; j++)
			for (int i = 0 ; i < largeur ; i++) {
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
			for (int i = 0 ; i < this.largeur ; i++) {
				for (int j = 0 ; j < this.longueur ; j++) {

					Piece p = env.getPieces()[i][j];
					String nomImage;
					if (p.getBijou() && p.getPoussiere()) {
						nomImage = "images/deux";
					} else if (p.getBijou()) {
						nomImage = "images/bijou";
					} else if (p.getPoussiere()) {
						nomImage = "images/poussiere";
					} else {
						nomImage = "images/rien";
					}
					
					if (this.robot.getPosition().getAbscisse() == i && this.robot.getPosition().getOrdonnee() == j) 
						nomImage += "_robot";
					
					nomImage += ".png";
					this.pieces[i][j].setIcon(new ImageIcon(nomImage));
				}
			}
			this.repaint();
		}
	}

	public static void main(String[] args) {
		Environnement env = new Environnement(5,5);
		Carte c = new Carte(env, new Agent(env));

		Thread thread = new Thread(c);
		thread.start();
	}

	@Override
	public void run() {
		Thread thread = new Thread(env);
		thread.start();

		Thread thread2 = new Thread(robot);
		thread2.start();

		this.afficher();
	}
}

