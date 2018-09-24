package agentAspirateur;

public class Piece {
	
	private boolean poussiere;
	private boolean bijou;
	private int abscisse;
	private int ordonnée;
	
	public Piece(int a, int o) {
		this.ordonnée = o;
		this.abscisse = a;
		this.poussiere = false;
		this.bijou = false;
	}
	
	public void setPoussiere(boolean p) {
		this.poussiere = p;
	}
	
	public void setBijou(boolean b) {
		this.bijou = b;
	}
	
	public boolean getPoussiere() {
		return this.poussiere;
	}
	
	public boolean getBijou() {
		return this.bijou;
	}
}

