package nl.gemeente.breda.bredaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.gemeente.breda.bredaapp.R;
import nl.gemeente.breda.bredaapp.domain.Service;

public class ServiceAdapter extends ArrayAdapter<Service> {
	
	int layout;
	
	public ServiceAdapter(Context context, ArrayList<Service> services, int layout) {
		super(context, layout, services);
		this.layout = layout;
		
	}
	
	@Override
	public View getView(int position, View convertViewInitial, ViewGroup parent) {
		
		// Create product
		Service service = getItem(position);
		View convertView = convertViewInitial;
		
		// Check for existing view
		if (convertViewInitial == null) {
			convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
		}
		
		// Select row items
		TextView serviceName = (TextView) convertView.findViewById(R.id.spinnerLayoutAdapter_tv_spinnerText);
		
		// Get and set content
		serviceName.setText(service.getServiceName());
		
		// Return view
		return convertView;
	}
}
