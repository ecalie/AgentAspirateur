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

	public abstract Noeud getNoeudParent();

}
