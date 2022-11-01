package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.Hotspot;

public class NYCDao {
	
	
	
	public List<Hotspot> getAllHotspot(){
		
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			List<Hotspot> result = new ArrayList<Hotspot>();
			while (res.next()) {
		
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
					}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	//Mi faccio una query che mi restituisca una lista di providers. In questo modo posso costruire le componenti connesse per tutti i providers.
	public List<String> getProviders(){
		
		String sql=" SELECT DISTINCT Provider "
				+"FROM nyc_wifi_hotspot_locations "
				+"ORDER BY Provider";
		
		List<String> result = new ArrayList<String>();
		
		try {
		
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
				result.add(res.getString("Provider"));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<City> getAllCities (String provider){
		
		String sql = "SELECT DISTINCT City, AVG(Latitude) as Lat, AVG(Longitude) as Lng, COUNT(*) as NUM "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE provider = ? "
				+ "GROUP BY City "
				+ "ORDER BY City";
		
		List<City> listaCittà = new ArrayList<City>();
		
		try {
		
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, provider);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
				listaCittà.add(new City(res.getString("City"), new LatLng(res.getDouble("Lat"), res.getDouble("Lng")), res.getInt("NUM")));
				//Ho creato l'oggetto di comodo città che ha come parametri Nome (String) e posizione (LatLng). Per passargli
				//la posizione devo creare un oggetto LatLng dove passo PRIMA LA LATITUDINE POI LA LONGITUDINE.
			}
			
			conn.close();
			return listaCittà;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}