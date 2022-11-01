/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.nyc;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.CityDistance;
import it.polito.tdp.nyc.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="cmbProvider"
    private ComboBox<String> cmbProvider; // Value injected by FXMLLoader

    @FXML // fx:id="cmbQuartiere"
    private ComboBox<City> cmbQuartiere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="clQuartiere"
    private TableColumn<CityDistance, String> clQuartiere; // Value injected by FXMLLoader
 
    @FXML // fx:id="clDistanza"
    private TableColumn<CityDistance, Double> clDistanza; // Value injected by FXMLLoader
    
    @FXML // fx:id="tblQuartieri"
    private TableView<CityDistance> tblQuartieri; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	//Quando creo il grafo posso popolare la comboBox di città.
    	
    	//Estraggo il provider selezionato.
    	String provider = this.cmbProvider.getValue();
    	
    	//Controllo errori
    	if(provider == null) {
    		txtResult.appendText("Errore: selezionare un provider!");
    		return;
    	}
    	
    	//Creo il grafo
    	String msg = model.creaGrafo(provider);
    	txtResult.appendText(msg);
    	
    	//Popolo la tendina dei quartieri dopo averla liberata da eventuali città di grafi precedenti.
    	this.cmbQuartiere.getItems().clear();
    	this.cmbQuartiere.getItems().addAll(this.model.getCities());
    	
    	
    }

    @FXML
    void doQuartieriAdiacenti(ActionEvent event) {
    	
    	City scelto = this.cmbQuartiere.getValue();
    	
    	if(scelto == null) {
    		txtResult.appendText("Seleziona un quartiere");
    		return;
    	}
    	
    	//Posso chiedere al modello di darmi la lista.
    	List<CityDistance> distanze = model.getCityDistances(scelto);
    	
    	//Devo ora configurare la TableView e la TableColumn
    	//Vedi initialize()
    	
    	//Creo una nuova ObservableCollections per poter impostare il valore della tabella.
    	this.tblQuartieri.setItems(FXCollections.observableArrayList(distanze));
    }

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProvider != null : "fx:id=\"cmbProvider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbQuartiere != null : "fx:id=\"cmbQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clDistanza != null : "fx:id=\"clDistanza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clQuartiere != null : "fx:id=\"clQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";

        //Devo informare le celle come stamparsi.
        this.clQuartiere.setCellValueFactory(new PropertyValueFactory<CityDistance, String>("nome"));
        this.clDistanza.setCellValueFactory(new PropertyValueFactory<CityDistance, Double>("distanza"));
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	//Qua popolo la tendina di providers
    	cmbProvider.getItems().addAll(this.model.getProviders());
    }

}
