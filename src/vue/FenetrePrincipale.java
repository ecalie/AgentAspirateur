package vue;

import java.awt.BorderLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.*;

public class FenetrePrincipale extends JFrame implements Runnable {

	private JLabel score;
	private JLabel action;
	private Agent robot;
	private Carte carte;

	public FenetrePrincipale(Carte c, Agent robot) {
		super("Plan du manoir");
		this.setLayout(new BorderLayout());
		this.carte = c;
		this.score = new JLabel("score : " + robot.getEtatInterne() + "      ");
		this.action = new JLabel("      action : " );
		this.robot = robot;
		
		this.add(carte, BorderLayout.CENTER);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(score, BorderLayout.EAST);
		panel.add(action, BorderLayout.WEST);
		
		this.add(panel, BorderLayout.NORTH);
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	public void run() {
		while (true) {
			this.score.setText("score : " + robot.getEtatInterne() + "      ");

			String action;
			if (robot.action() != null) {
				switch (robot.action()) {
				case ATTENDRE: 
					action = "ATTENDRE";
					break;
				case RAMASSER:
					action = "RAMASSER";
					break;
				case ASPIRER:
					action = "ASPIRER";
					break;
				default:
					action = robot.getDestination().toString();
					break;
				}

				this.action.setText("      action  : " + action);
			}
			
			this.carte.afficher();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Saisir 0 pour une recherche non informee ou 1 pour une recherche informee : ");


		Scanner sc = new Scanner(System.in);
		boolean OK = false;
		boolean informe = true;
		while (!OK) {
			String s = sc.next();
			if(s.equals("0")) {
				OK = true;
				informe = false;
			}
			else if(s.equals("1")) {
				OK = true;
				informe = true;
			}

		}

		System.out.println("Vous avez choisis : " + (informe ? "INFORME" : "NON INFORME"));

		
		Environnement env = new Environnement(10,10);
		Agent robot = new Agent(env, informe);
		Carte c = new Carte(env, robot);
		FenetrePrincipale fp = new FenetrePrincipale(c, robot);

		new Thread(fp).start();
		new Thread(env).start();
		new Thread(robot).start();
	}
}
