package it.polito.tdp.nyc.model;

public class Event implements Comparable<Event>{

	public enum EventType{
		INIZIO_HOTSPOT,			//Il tecnico inizia a lavorare su un HotSpot
		FINE_HOTSPOT,			//Il tecnico termina la lavorazione sull'HotSpot.
		NUOVO_QUARTIERE;			//Evento in cui la squadra si sposta in un nuovo quartiere. Posso schedurarlo quando un tecnico diventa libero e il numero di liberi è uguale al numero di totali.
	}
	
	//Compilo l'evento con i soliti campi.
	private int time;
	private EventType type;
	private int tecnico;		//Mi serve l'informazione sul tecnico che deve iniziare o finire il lavoro.
	
	
	public int getTime() {
		return time;
	}

	public EventType getType() {
		return type;
	}

	public int getTecnico() {
		return tecnico;
	}

	public Event(int time, EventType type, int tecnico) {
		
		this.time = time;
		this.type = type;
		this.tecnico = tecnico;
	}



	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return this.time-o.time;
	}
	
}
