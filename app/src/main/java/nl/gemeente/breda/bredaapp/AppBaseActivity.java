package nl.gemeente.breda.bredaapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.Random;

import nl.gemeente.breda.bredaapp.eastereggs.TestEasterEgg;
import nl.gemeente.breda.bredaapp.eastereggs.snake.Snake;
import nl.gemeente.breda.bredaapp.eastereggs.spaceinvaders.MainActivity;

public abstract class AppBaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
	
	private FrameLayout view_stub;
	private Toolbar toolbarSimple;
	private NavigationView navigationView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private Menu menu;
	
	private ImageView shareButton;
	private ImageView logo;
	
	private int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.app_base_layout);
		view_stub = (FrameLayout) findViewById(R.id.view_stub);
		
		toolbarSimple = (Toolbar) findViewById(R.id.toolbar);
		toolbarSimple.setTitle(getResources().getString(R.string.app_name));
		toolbarSimple.setNavigationIcon(R.drawable.ic_menu_white_24dp);
		setSupportActionBar(toolbarSimple);
		
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
		drawerToggle.setDrawerIndicatorEnabled(true);
		drawerLayout.addDrawerListener(drawerToggle);
		//getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbarSimple.setNavigationIcon(R.drawable.ic_menu_white_24dp);
		
		menu = navigationView.getMenu();
		for (int i = 0; i < menu.size(); i++) {
			menu.getItem(i).setOnMenuItemClickListener(this);
		}
		
		View headerLayout = navigationView.getHeaderView(0);
		logo = (ImageView) headerLayout.findViewById(R.id.navigation_header_logo);
		i = 1;
		logo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(i < 5){
					i++;
				}
				else if(i == 5){
					Random r = new Random();
					int rand = r.nextInt(2) + 1;
					Log.i("RANDOM", "" + rand);
					
					switch (rand) {
						case 1:
							Intent spaceinvaders = new Intent(getApplicationContext(), MainActivity.class);
							startActivity(spaceinvaders);
							i = 1;
							break;
						
						case 2:
							Intent snake = new Intent(getApplicationContext(), Snake.class);
							startActivity(snake);
							i = 1;
							break;
						
						default:
							Intent easteregg = new Intent(getApplicationContext(), TestEasterEgg.class);
							startActivity(easteregg);
							i = 1;
							break;
					}
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
				onMenuClick(MainScreenActivity.class, R.id.nav_reports, true);
				break;
			
			case R.id.nav_my_reports:
				onMenuClick(FavoriteReportsActivity.class, R.id.nav_my_reports, true);
				break;
			
			case R.id.nav_settings:
				onMenuClick(UserSettingsActivity.class, R.id.nav_settings, false);
				break;

			
			case R.id.nav_info:
				onMenuClick(InfoActivity.class, R.id.nav_info, false);
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
	
	public void setToolbarTitle(String title) {
		toolbarSimple.setTitle(title);
	}
	
	public void setToolbarTitle(@StringRes int title) {
		toolbarSimple.setTitle(title);
	}
}
