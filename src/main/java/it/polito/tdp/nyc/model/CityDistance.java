package it.polito.tdp.nyc.model;

public class CityDistance {
	
	//Questi oggetti dovranno essere popolati a partire dal grafo per riempire la tabella quando un utente seleziona un quartiere.
	//Punto 1d.

	private String nome;
	private Double distanza;	//Uso Double oggetto per poter utilizzare la sua compareTo() nel modello
	public CityDistance(String nome, double distanza) {
		super();
		this.nome = nome;
		this.distanza = distanza;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	
	
}
