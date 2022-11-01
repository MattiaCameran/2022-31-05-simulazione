package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private Graph<City, DefaultWeightedEdge> grafo;
	
	private NYCDao dao;
	
	private List<String> providers;
	
	private List<City> cities;
	
	private Map<Integer, Hotspot> idMap;	//Ho creato la idMap, ma in questo esercizio non è necessario utilizzarla.
	
	public Model() {
		
		this.dao = new NYCDao();
		this.idMap = new HashMap<Integer, Hotspot>();
		
	}
	
	public List<String> getProviders() {	//Metodo getter per ritornare i provider che controlla che sia già stata fatta la query.
		
		if(this.providers == null) {
			this.providers = this.dao.getProviders();
		}
		return this.providers;
	}
	
	public String creaGrafo(String provider) {	//Ritorno una stringa per controllare dal controller.
		
		//Prima recupero le città dal DAO, poi creo il grafo. Creo un nuovo riferimento al DAO.
		//Questo perché io ho creato il dao una sola volta nel costruttore, ma questo potrebbe essere cambiato nel tempo in quanto l'utente potrebbe cambiare provider negli anni, così facendo ogni volta che creo controllo dal DB se l'utente ha ancora o no il provider.
		NYCDao dao = new NYCDao();
		
		this.cities = this.dao.getAllCities(provider);
		
		
		//Creo grafo.
		this.grafo = new SimpleWeightedGraph<City, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici.
		Graphs.addAllVertices(this.grafo, this.cities);	//Più comodo fare così che usare il dao dentro la creazione del grafo.
		
		System.out.println("Grafo creato!");
		System.out.println("Numero vertici " +this.grafo.vertexSet().size());
		//Provo il programma. Funziona.
		
		//Aggiungo archi. In questo arco ho pochi vertici perché devo selezionare solo le città che hanno un provider comune, quindi posso operare con il doppio ciclo for.
		for(City c1: this.cities) {
			for(City c2: this.cities) {
				
				if(!c1.equals(c2)) {	//Creo l'arco solo se non sono uguali le città.
					
					double peso = LatLngTool.distance(c1.getPosizione(),c2.getPosizione(), LengthUnit.KILOMETER);	//Visto che Kilometer appartiene alla ENUM, con ctrl+barra spaziatrice riesco a recuperarlo subito.
				
					Graphs.addEdgeWithVertices(this.grafo, c1, c2, peso);
				
				}
			}
		}
		//Ho creato l'arco.
		
		
		return "Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi";
		
	}

	public List<City> getCities() {
		return cities;
	}
	
	public List<CityDistance> getCityDistances(City scelto){
		
		List<CityDistance> result = new ArrayList<CityDistance>();
		
		List<City> vicini = Graphs.neighborListOf(this.grafo, scelto);	//Se il grafo fosse orientato utilizzeremmo SuccessorList of o PredessorListof.
		
		for(City c: vicini) {
			result.add(new CityDistance(c.getNome(), this.grafo.getEdgeWeight(this.grafo.getEdge(scelto, c))));	//Prendi il peso dell'arco appartenente al grafo tra il vertice scelto e c.
		}
		
		Collections.sort(result, new Comparator<CityDistance>() {

			@Override
			public int compare(CityDistance o1, CityDistance o2) {
				
				return o1.getDistanza().compareTo(o2.getDistanza());
			}
			
			
		});
		
		return result;
	}
}
