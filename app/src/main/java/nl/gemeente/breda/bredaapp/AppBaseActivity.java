package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.flask.floatingactionmenu.FloatingActionButton;
import com.flask.floatingactionmenu.FloatingActionMenu;
import com.flask.floatingactionmenu.FloatingActionToggleButton;
import com.flask.floatingactionmenu.OnFloatingActionMenuSelectedListener;

public abstract class AppBaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
	
	private FrameLayout view_stub;
	private Toolbar toolbarSimple;
	private NavigationView navigationView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private Menu menu;
	
	private FloatingActionMenu floatingActionMenu;
	
	private ImageView shareButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.app_base_layout);
		view_stub = (FrameLayout) findViewById(R.id.view_stub);
		
		toolbarSimple = (Toolbar) findViewById(R.id.toolbar);
		toolbarSimple.setTitle("App");
		toolbarSimple.setNavigationIcon(R.drawable.menu_hamburger);
		setSupportActionBar(toolbarSimple);
		
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
		drawerToggle.setDrawerIndicatorEnabled(true);
		drawerLayout.addDrawerListener(drawerToggle);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		
		menu = navigationView.getMenu();
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setOnMenuItemClickListener(this);
		}
		
		floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fam);
		floatingActionMenu.setOnFloatingActionMenuSelectedListener(new OnFloatingActionMenuSelectedListener() {
			@Override
			public void onFloatingActionMenuSelected(FloatingActionButton floatingActionButton) {
				if (floatingActionButton instanceof FloatingActionToggleButton) {
					FloatingActionToggleButton fatb = (FloatingActionToggleButton) floatingActionButton;
				} else if (floatingActionButton instanceof FloatingActionButton) {
					FloatingActionButton fab = (FloatingActionButton) floatingActionButton;
					String label = fab.getLabelText();
					Toast.makeText(getApplicationContext(), label, Toast.LENGTH_SHORT).show();
					//onMenuClick(Test2Activity.class, R.id.fab_location, false);
				}
			}
		});
		
		shareButton = (ImageView) findViewById(R.id.toolbar_share);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out InfraMeld! It's insane!");
				shareIntent.setType("text/plain");
				startActivity(shareIntent);
			}
		});
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration configuration) {
		super.onConfigurationChanged(configuration);
		drawerToggle.onConfigurationChanged(configuration);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		if (view_stub != null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			View stubView = inflater.inflate(layoutResID, view_stub, false);
			view_stub.addView(stubView, lp);
		}
	}
	
	@Override
	public void setContentView(View view) {
		if (view_stub != null) {
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			view_stub.addView(view, lp);
		}
	}
	
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		if (view_stub != null) {
			view_stub.addView(view, params);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav_reports:
				//onMenuClick(Test1Activity.class, R.id.nav_reports, true);
				break;
			
			case R.id.nav_my_reports:
				//onMenuClick(Test3Activity.class, R.id.nav_my_reports, true);
				break;
			
			case R.id.nav_account:
			case R.id.nav_settings:
			case R.id.nav_info:
				Toast toast = Toast.makeText(getApplicationContext(), "Function not implemented.", Toast.LENGTH_LONG);
				toast.show();
				break;
		}
		
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		}
		
		return false;
	}
	
	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	public void onMenuClick(Class item, int menuID, boolean clear) {
		Intent intent = new Intent(getApplicationContext(), item);
		intent.putExtra("menuID", menuID);
		if (clear) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}
		startActivity(intent);
	}
	
	public void setMenuSelected(Bundle items) {
		if (items != null) {
			int itemID = items.getInt("menuID");
			navigationView.setCheckedItem(itemID);
		}
	}
	
	public void setFABVisibility(boolean visible) {
		if (visible) {
			floatingActionMenu.setVisibility(View.VISIBLE);
		} else {
			floatingActionMenu.setVisibility(View.INVISIBLE);
		}
	}
}
