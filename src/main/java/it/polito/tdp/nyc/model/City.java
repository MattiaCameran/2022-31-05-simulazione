package it.polito.tdp.nyc.model;

import java.util.Objects;

import com.javadocmd.simplelatlng.LatLng;

public class City {

	//Mi creo una classe City di comodo per contenere le informazioni.
	
	private String nome;
	private LatLng posizione;
	private int nHotspot;	//Devo aggiungere questo campo per il punto due dell'esercizio
	
	public City(String nome, LatLng posizione, int nHotspot) {
		super();
		this.nome = nome;
		this.posizione = posizione;
		this.nHotspot = nHotspot;
	}
	public int getnHotspot() {
		return nHotspot;
	}
	public void setnHotspot(int nHotspot) {
		this.nHotspot = nHotspot;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LatLng getPosizione() {
		return posizione;
	}
	public void setPosizione(LatLng posizione) {
		this.posizione = posizione;
	}
	
	//Sono i vertici del nostro grafo. Devono essere Hashable.
	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(nome, other.nome);
	}
	@Override
	public String toString() {
		return nome;
	}
	
	
}
