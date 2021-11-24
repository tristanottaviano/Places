package com.Places.dao;

import com.Places.ws.WebServicesMap;
import com.Places.ws.WebServicesPins;
import com.Places.ws.WebServicesUsers;

public class FakeData {
	
	
	public void createData(){
		
		User user1 = new User("Remsi", "password", "Remsi bio");
		User user2 = new User("Paulsi", "password", "Paulsi bio");
		User user3 = new User("Trizy", "password", "Trizy bio");
		User user4 = new User("Clumsy", "password", "Clumsy bio");
		
		WebServicesUsers wsU = new WebServicesUsers();
		
		wsU.createUser(user1);
		wsU.createUser(user2);
		wsU.createUser(user3);
		wsU.createUser(user4);

		
		WebServicesPins wsP = new WebServicesPins();
		
		Pin remsi1 = new Pin("Remsi pin 1", "Description", "test", 1,1,0);
		Pin remsi2 = new Pin("Remsi pin 2", "Description", "test", 2,2,0);
		
		Pin paulsi1 = new Pin("Paulsi pin 1", "Description", "test", 1,1,0);
		Pin paulsi2 = new Pin("Paulsi pin2", "Description", "test", 1,1,0);
		
		Pin Trizy1 = new Pin("Trizy pin 1", "Description", "test", 1,1,0);
		Pin Trizy2 = new Pin("Trizy pin 2", "Description", "test", 1,1,0);
		
		Pin Clumsy1 = new Pin("Clumsy pin 1", "Description", "test", 1,1,0);
		Pin Clumsy2 = new Pin("Clumsy pin 2", "Description", "test", 1,1,0);
		
		wsP.createPin(remsi1);
		wsP.createPin(remsi2);
		wsP.createPin(paulsi1);
		wsP.createPin(paulsi2);
		wsP.createPin(Trizy1);
		wsP.createPin(Trizy2);
		wsP.createPin(Clumsy1);
		wsP.createPin(Clumsy2);
		
		WebServicesMap wsM = new WebServicesMap();
		
		Map remsiMap = new Map(0, "Map Remsi","description", "tags",0);
		Map RemsiMap = new Map(0, "Map Paulsi", "description", "tags",0);
		Map TrizyMap = new Map(0, "Map Trizy","description", "tags",0);
		Map ClumsyMap = new Map(0, "Map Clumsy", "description","tags",0);
		
	}
	
	

}
