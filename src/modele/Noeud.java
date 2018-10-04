package modele;

public abstract class Noeud {

	protected Piece positionRobot;
	/** L'action qui permet d'arriver à l'état correspondant au noeud. */
	protected Action action;
	/** Coût total pour arriver à ce noeud. */
	protected int cout;


	public Noeud(Piece position, Action action, int cout) {
		this.positionRobot = position;
		this.action = action;
		this.cout = cout;
	}

	public Piece getPositionRobot() {
		return positionRobot;
	}

	public Action getAction() {
		return this.action;
	}

	public int getCout() {
		return this.cout;
	}

	/**
	 * Calculer le gain de l'action selon l'environnemenr.
	 * Seules les actions ramsser et aspirer peuvent faire gagner ou perdre des points.
	 * @return Le nombre de points gagnés si le robot effectué l'action.
	 */
	public int calculerGain() {
		switch (action) {
		case ATTENDRE:
		case MONTER:
		case DESCENDRE:
		case GAUCHE:
		case DROITE:
			return 0;
		case ASPIRER:
			return this.positionRobot.gainAspirer();
		case RAMASSER:
			return this.positionRobot.gainRamasser();
		default:
			return 0;
		}
	}
	public abstract Noeud getNoeudParent();

}
