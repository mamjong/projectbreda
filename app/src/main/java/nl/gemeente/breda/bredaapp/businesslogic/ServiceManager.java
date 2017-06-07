package nl.gemeente.breda.bredaapp.businesslogic;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.domain.Service;

public class ServiceManager {
	private ServiceManager(){
		// to do
	}
	private static ArrayList<Service> services = new ArrayList<>();
	
	public static void addService(Service service) {
		services.add(service);
	}
	
	public static ArrayList<Service> getServices() {
		return services;
	}
	
	public static void emptyArray() {
		services.clear();
	}
}
