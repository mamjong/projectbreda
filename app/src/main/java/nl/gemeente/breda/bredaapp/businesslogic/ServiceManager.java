package nl.gemeente.breda.bredaapp.businesslogic;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.domain.Report;

public class ServiceManager {
    private static ArrayList<Report> reports = new ArrayList<>();

    public static void addReport(Report report){
        reports.add(report);
    }

    public static ArrayList<Report> getReports(){
        return reports;
    }
}
