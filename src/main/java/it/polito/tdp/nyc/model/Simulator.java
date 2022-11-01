package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.model.Event.EventType;

public class Simulator {

	//Dati in ingresso
	private List<City> cities;
	private Graph<City, DefaultWeightedEdge> grafo;
	//Grafo e lista di città per sapere come spostarmi.
	private City partenza;	//Città di partenza
	private int N;	//Numero di tecnici
	
	
	//Dati in uscita
	private int durata;	//Durata in minuti della simulazione.
	private List<Integer> revisionati;	//Non mi serve fare una lista di Integer, Integer dove il primo indica il tecnico e il secondo il numero di hotspot revisionati perché posso tracciare i tecnici tramite l'indice della lista (vado infatti da 0 a N-1 per i).
	
	//Stato del mondo
	
	//Devo sapere in che quartiere sono, quali quartieri ho già visitato e quali no e quanti hotspot mi mancano da revisionare in questo quartiere.
	private List<City> daVisitare;	//La lista dei quartieri da visitare NON include CurrentCity
	private City currentCity;	
	private int hotSpotRimanenti;
	private int tecniciOccupati;	//Informazione che mi serve a capire quando ho tutti i tecnici liberi, così cambio quartiere (se ho trattato tutti gli HotSpot).
	
	//Coda degli eventi
	PriorityQueue<Event> queue;

	public Simulator(Graph<City, DefaultWeightedEdge> grafo, List<City> cities) {
		super();
		this.cities = cities;
		this.grafo = grafo;
		
		//Nel costruttore metto i dati che non ha senso cambiare, gli altri li metto nel metodo init().
	}
	
	public void init(City partenza, int N) {
		this.partenza = partenza;
		this.N = N;
		
		//inizializzo gli output.
		this.durata = 0;
		this.revisionati = new ArrayList<Integer>();
		for(int i = 0; i<N; i++) {
			revisionati.add(0);
		}
		//Inizializzo lo stato del mondo.
		currentCity = this.partenza;
		daVisitare = new ArrayList<City>(this.cities);
		daVisitare.remove(this.currentCity);	//Inizializzo la lista delle città da visitare togliendogli la città corrente.
		
		this.hotSpotRimanenti = this.currentCity.getnHotspot();	//All'inizio devo visitare tutti gli hotspot per la città.
		this.tecniciOccupati = 0;
		
		//Questo qua sopra è il mondo prima che i tecnici si sveglino, allo stato iniziale.
		
		//Nel metodo init() devo però anche avere il caricamento iniziale della coda.
		
		int i = 0;
		while(this.tecniciOccupati < N && this.hotSpotRimanenti > 0) {	//Finché ho un tecnico disponibile e ho almeno un hotspoto da revisionare.
			
			//Assegno un tecnico ad un HotSpot.
			queue.add(new Event(0, EventType.INIZIO_HOTSPOT, i));	//Assegno fino a quando posso un tecnico al tempo 0 ad un hotspot disponibile per la città corrente.
			this.tecniciOccupati++;
			this.hotSpotRimanenti--;
			
			//Se invece non ho più tecnici liberi e ho finito gli hotspot, mi sposto in un'altra città
		}
		
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {	//Se la coda non è vuota.
			Event e = this.queue.poll();	//Estrai.
			this.durata = e.getTime();		//Alla fine della simulazione nel campo durata avrò il numero di passi.
			processEvent(e);
			
		}
		
	}

	private void processEvent(Event e) {
		
		int time = e.getTime();
		EventType type = e.getType();
		int tecnico = e.getTecnico();
		
		switch(type) {
		
		case INIZIO_HOTSPOT:
			this.revisionati.set(tecnico, this.revisionati.get(tecnico)+1);		//Aumento di 1 il numero di revisionati per il tecnico specifico.
			
			if(Math.random()<0.1) {
				queue.add(new Event(time+25, EventType.FINE_HOTSPOT, tecnico));	//Finisco dopo 25 minuti.
			}
			else {
				queue.add(new Event(time+25, EventType.FINE_HOTSPOT, tecnico)); //Finisco dopo 10 minuti.
			}
			break;
		case FINE_HOTSPOT:
			
			this.tecniciOccupati--;
			//Ho finito di lavorare su un HotSpot, se c'è. Se non c'è non faccio nulla. Se non c'è e sono l'ultimo libero, cambiamo quartiere.
			if(this.hotSpotRimanenti > 0) {
				//Mi sposto ad un altro hotSpot.
				
				int spostamento = (int)(Math.random()*11)+10;
				this.tecniciOccupati++;
				this.hotSpotRimanenti--;
				queue.add(new Event(time+spostamento, EventType.INIZIO_HOTSPOT, tecnico));
			}
			else if(this.hotSpotRimanenti == 0 && tecniciOccupati> 0) {
				//Non fai nulla
			}
			//Devo vedere se c'è un quartiere libero.
			else if(daVisitare.size() > 0){
				//Tutti cambiamo quartiere.
				
				//Si va alla città più vicina tra quelle rimanenti.
				City destinazione = piuVicino(this.currentCity, this.daVisitare);
				
				//Devo calcolare il tempo di spostamento.
				int spostamento = (int)(this.grafo.getEdgeWeight(this.grafo.getEdge(currentCity, destinazione)) / 50.0 * 60);	//Distanza / velocità = tempo in ore * 60 = tempo in minuti.
				this.currentCity = destinazione;	//La città corrente diventa la destinazione.
				this.daVisitare.remove(destinazione);	//Tolgo "destinazione" dalle città da visitare.
				
				this.hotSpotRimanenti = this.currentCity.getnHotspot();	//I nuovi hotspot da visitare saranno quelli della currentCity.
				this.queue.add((new Event(time+spostamento, EventType.NUOVO_QUARTIERE, -1)));	//Metto "-1" perché il get del Tecnico non ci interessa.
			
			}
			else {
				//Fine simulazione.
			}
			break;
		case NUOVO_QUARTIERE:
			
				int i = 0;
				while(this.tecniciOccupati < N && this.hotSpotRimanenti > 0) {	//Finché ho un tecnico disponibile e ho almeno un hotspoto da revisionare.
				//Assegno un tecnico ad un HotSpot.
				queue.add(new Event(time, EventType.INIZIO_HOTSPOT, i));	//Assegno fino a quando posso un tecnico al tempo 0 ad un hotspot disponibile per la città corrente.
				this.tecniciOccupati++;
				this.hotSpotRimanenti--;
				
				//Se invece non ho più tecnici liberi e ho finito gli hotspot, mi sposto in un'altra città
			}
			break;
			
		}
		
	}

	private City piuVicino(City current, List<City> vicine) {
		
		//Calcolo il minimo del peso degli archi.
		double min = 100000000.0;
		City destinazione = null;
		for(City v: vicine) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(current, v));
			if(peso < min) {
				min = peso;
				destinazione = v;
			}
		}
		return destinazione;
	}

	public int getDurata() {
		return durata;
	}

	public List<Integer> getRevisionati() {
		return revisionati;
	}
	
}
