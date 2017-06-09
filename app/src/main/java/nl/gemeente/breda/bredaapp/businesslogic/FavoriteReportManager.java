package nl.gemeente.breda.bredaapp.businesslogic;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.domain.Report;

public class FavoriteReportManager {
	private FavoriteReportManager(){
		// to do
	}
	private static ArrayList<Report> reports = new ArrayList<>();
	
	public static void addReport(Report report) {
		reports.add(report);
	}
	
	public static ArrayList<Report> getFavoriteReports() {
		return reports;
	}
	
	public static void emptyArray() {
		reports.clear();
	}
}
