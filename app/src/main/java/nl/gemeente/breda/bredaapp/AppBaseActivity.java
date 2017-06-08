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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import nl.gemeente.breda.bredaapp.util.ThemeManager;

public abstract class AppBaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
	
	private FrameLayout viewStub;
	private Toolbar toolbarSimple;
	private NavigationView navigationView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	
	private ImageView shareButton;
	
	private String shareText = "Check out InfraMeld! It's insane!";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeManager.setTheme(AppBaseActivity.this);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.app_base_layout);
		viewStub = (FrameLayout) findViewById(R.id.view_stub);
		
		toolbarSimple = (Toolbar) findViewById(R.id.toolbar);
		toolbarSimple.setTitle(getResources().getString(R.string.app_name));
		toolbarSimple.setNavigationIcon(R.drawable.ic_menu_white_24dp);
		setSupportActionBar(toolbarSimple);
		
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
		drawerToggle.setDrawerIndicatorEnabled(true);
		drawerLayout.addDrawerListener(drawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbarSimple.setNavigationIcon(R.drawable.ic_menu_white_24dp);
		
		Menu menu = navigationView.getMenu();
		for (int y = 0; y < menu.size(); y++) {
			menu.getItem(y).setOnMenuItemClickListener(this);
		}
		
		shareButton = (ImageView) findViewById(R.id.toolbar_share);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
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
		if (viewStub != null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			View stubView = inflater.inflate(layoutResID, viewStub, false);
			viewStub.addView(stubView, lp);
		}
	}
	
	@Override
	public void setContentView(View view) {
		if (viewStub != null) {
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			viewStub.addView(view, lp);
		}
	}
	
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		if (viewStub != null) {
			viewStub.addView(view, params);
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
				break;
			
			case R.id.nav_settings:
				onMenuClick(UserSettingsActivity.class, R.id.nav_settings, false);
				break;
			
			
			case R.id.nav_info:
				onMenuClick(InfoActivity.class, R.id.nav_info, false);
				break;
			
			default:break;
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
	
	public void setShareText(String text) {
		shareText = text;
	}
	
	public void setShareText(@StringRes int text) {
		shareText = getResources().getString(text);
	}
	
	public void setShareVisible(boolean visible) {
		if (visible) {
			shareButton.setVisibility(View.VISIBLE);
		} else {
			shareButton.setVisibility(View.INVISIBLE);
		}
	}
}
