package com.togrulseyid.gags.activity;
//package com.togrulseyid.gags.main;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ExpandableListAdapter;
//import android.widget.ExpandableListView;
//import android.widget.Toast;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.actionbarsherlock.widget.SearchView;
//import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
//import com.togrulseyid.gags.libs.Constants;
//import com.togrulseyid.gags.libs.adapters.SearchExpandalbeListAdapter;
//import com.togrulseyid.gags.viewer.R;
//import com.togrulseyid.gags.viewers.AnimationViewer;
//import com.togrulseyid.gags.viewers.ImageViewer;
//import com.togrulseyid.gags.viewers.JokeViewer;
//import com.togrulseyid.gags.viewers.VideoViewer;
//
//public class Search extends SherlockActivity implements OnQueryTextListener{
//
//	private SearchManager searchManager ;
//    private SearchView searchView ;
//    private TextView more ;
//    private LinearLayout moreView ;
//    private ExpandableListView expListView ;
//    private ExpandableListAdapter  listAdapter ;
//    private List<String> listDataHeader ;
//    private HashMap<String, List<HashMap<Byte, String>>> listDataChild ;
//	private Intent intent ;
//	private List<HashMap<Byte, String>> list ;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
//		super.onCreate(savedInstanceState);		
//		setContentView(R.layout.search);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//		more = (TextView) findViewById(R.id.more);
//		moreView  = (LinearLayout) findViewById(R.id.moreView);
//		expListView = (ExpandableListView) findViewById(R.id.expandableListView);		
//		
//		more.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				moreView.setVisibility(View.VISIBLE);
//			}
//		});
//		
//		
//
//		// preparing list data
//		prepareListData();
//
//		listAdapter = new SearchExpandalbeListAdapter(this, listDataHeader, listDataChild);
//
//		// setting list adapter
//		expListView.setAdapter(listAdapter);
//		expListView.setOnChildClickListener(new OnChildClickListener() {
//			
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//				// TODO Auto-generated method stub
//				switch (groupPosition) {
//				case 0:
//
//					list = listDataChild.get(Constants.IMAGES);
//					try{
//						intent = new Intent(getApplicationContext(), ImageViewer.class);
//						intent.putExtra( Constants.TAG_SRC, list.get(childPosition).get((byte) 1));
//						intent.putExtra( Constants.TAG_ALT, list.get(childPosition).get((byte) 0));
//						startActivity(intent);
//					}catch (Exception e){
//						Toast.makeText(getApplicationContext(), e.getMessage(), 2000).show();
//						
//					}
//					
//					
////					intent = new Intent(Search.this, ImageViewer.class);
////					Log.d(Constants.TAG_SRC, list.get(childPosition).get((byte)0));
////					Log.d(Constants.TAG_ALT, list.get(childPosition).get((byte)1));
////					
////					intent.putExtra(Constants.TAG_SRC, list.get(childPosition).get(1));
////					intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(0));
////					startActivity(intent);
//					break;
//				case 1:
////					list = listDataChild.get(Constants.ANIMATIONS);
////					intent = new Intent(getApplicationContext(), AnimationViewer.class);
////					intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(0));
////					intent.putExtra(Constants.TAG_SRC, list.get(childPosition).get(1));
////					startActivity(intent);
////					
//
//					
//					list = listDataChild.get(Constants.ANIMATIONS);
//					try{
//						intent = new Intent(getApplicationContext(), AnimationViewer.class);
//						intent.putExtra( Constants.TAG_ALT, list.get(childPosition).get((byte) 0));
//						intent.putExtra( Constants.TAG_SRC, list.get(childPosition).get((byte) 1));
//						startActivity(intent);
//					}catch (Exception e){
//						Toast.makeText(getApplicationContext(), e.getMessage(), 2000).show();
//						
//					}
//					
//					
//					break;
//
//				case 2:
//					list = listDataChild.get(Constants.VIDEOS);
//					intent = new Intent(getApplicationContext(), VideoViewer.class);
//					intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(0));
//					intent.putExtra(Constants.TAG_URL, list.get(childPosition).get(1));
//					startActivity(intent);
//					break;
//					
//				case 3:
////					list = listDataChild.get(Constants.JOKES);
////					intent = new Intent(Search.this, JokeViewer.class);
////					intent.putExtra(Constants.TAG_TITLE, list.get(childPosition).get(0));
////					intent.putExtra(Constants.TAG_TEXT, list.get(childPosition).get(1));
////					startActivity(intent);
//					
//					
//					list = listDataChild.get(Constants.JOKES);
//					try{
//						intent = new Intent(getApplicationContext(), JokeViewer.class);
//						intent.putExtra( Constants.TAG_TITLE, list.get(childPosition).get((byte) 0));
//						intent.putExtra( Constants.TAG_TEXT, list.get(childPosition).get((byte) 1));
//						startActivity(intent);
//					}catch (Exception e){
//						Toast.makeText(getApplicationContext(), e.getMessage(), 2000).show();
//						
//					}
//					
//					break;
//
//				default:
//					break;
//				}
//				return false;
//			}
//		});
//		
//		
//	}
//	
//	
//	
//	
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		getSupportMenuInflater().inflate(R.menu.gags, menu);
//		
//		searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//		searchView.setOnQueryTextListener(this);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		switch (item.getItemId()) {
//		    case android.R.id.home:
//		    	finish();
//		    	return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		// TODO Auto-generated method stub
//		super.onConfigurationChanged(newConfig);
//		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////			Log.e("On Config Change", "LANDSCAPE");
//			getSupportActionBar().hide();
//		} else {
////			Log.e("On Config Change", "PORTRAIT");
//			getSupportActionBar().show();
//		}
//	}
//
//	@Override
//	public boolean onQueryTextSubmit(String query) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean onQueryTextChange(String newText) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
//	
//	
//	
//	
//	
//	private List<HashMap<Byte, String>> Animations, Images, Videos, Jokes;
//	private HashMap<Byte, String> images, anime, video, joke;
//	/*
//	 * Preparing the list data
//	 */
//	private void prepareListData() {
////		listDataHeader = new ArrayList<HashMap<Byte, String>>();
////		listDataChild = new HashMap<String, List<String>>();
//		
//		listDataHeader = new ArrayList<String>();
//		listDataChild  = new HashMap<String, List<HashMap<Byte, String>>>();
//
//		// Adding child data
//		listDataHeader.add(Constants.IMAGES);
//		listDataHeader.add(Constants.ANIMATIONS);
//		listDataHeader.add(Constants.VIDEOS);
//		listDataHeader.add(Constants.JOKES);
//
//		// Adding child data
//		Images = new ArrayList<HashMap<Byte, String>>();
//		
//		images = new HashMap<Byte, String>();
//		images.put((byte) 0, "Louis CK nails it about buying a rug...");
//		images.put((byte) 1, "http://lolsnaps.com/upload_pic/LouisCKnailsitaboutbuyingarug-88515.jpeg");
//		Images.add(images);
//		
//		images = new HashMap<Byte, String>();
//		images.put((byte) 0, "Faith in humanity restored");
//		images.put((byte) 1, "http://lolsnaps.com/upload_pic/Faithinhumanityrestored-52318.jpg");
//		Images.add(images);
//		
//		images = new HashMap<Byte, String>();
//		images.put((byte) 0, "A secret room behind a bookshelf is cool until…");
//		images.put((byte) 1, "http://lolsnaps.com/upload_pic/Asecretroombehindabookshelfiscooluntil-17778.jpg");
//		Images.add(images);
//		
////		images = new HashMap<Byte, String>();
////		images.put((byte) 0, "Pulp Fiction");
////		images.put((byte) 1, R.drawable.trash_box+"");
////		Images.add(images);
////		
////		images = new HashMap<Byte, String>();
////		images.put((byte) 0, "The Good, the Bad and the Ugly");
////		images.put((byte) 1, R.drawable.warning+"");
////		Images.add(images);
////		
////		images = new HashMap<Byte, String>();
////		images.put((byte) 0, "The Dark Knight");
////		images.put((byte) 1, R.drawable.trash_box+"");
////		Images.add(images);
////		
////		images = new HashMap<Byte, String>();
////		images.put((byte) 0, "12 Angry Men");
////		images.put((byte) 1, R.drawable.warning+"");
////		Images.add(images);
//		
//		
//		
//		// Adding child data
//		Animations = new ArrayList<HashMap<Byte, String>>();
//		
//		anime = new HashMap<Byte, String>();
//		anime.put((byte) 0, "He's Been In The Tub Banging on Those Drums For 8 Hours");
//		anime.put((byte) 1, "http://media.giphy.com/media/c57rkrOxZGfTi/giphy.gif");
//		Animations.add(anime);
//		
//		anime = new HashMap<Byte, String>();
//		anime.put((byte) 0, "Practice Makes Perfect...and Cuteness!");
//		anime.put((byte) 1, "https://i.chzbgr.com/maxW500/7905551616/h6AC0E009");
//		Animations.add(anime);
//
//		anime = new HashMap<Byte, String>();
//		anime.put((byte) 0, "Nice Robe, Pity About The Lack of Environmental Awareness");
//		anime.put((byte) 1, "http://media.giphy.com/media/haIdhPbf66ili/giphy.gif");
//		Animations.add(anime);
//
//		
//		
//		// Adding child data
//		Videos = new ArrayList<HashMap<Byte, String>>();
//		
//		video = new HashMap<Byte, String>();
//		video.put((byte) 0, "Frisky Gay Security Guard Prank");
//		video.put((byte) 1, "ewu-u_J4biU");
//		Videos.add(video);
//		
//		video = new HashMap<Byte, String>();
//		video.put((byte) 0, "Boob Tube Pranks - Best of Just for Laughs Gags");
//		video.put((byte) 1, "wzWKFmJFFdA");
//		Videos.add(video);
//		
//		video = new HashMap<Byte, String>();
//		video.put((byte) 0, "Stinky Breath Prank");
//		video.put((byte) 1, "fW7yowZBqg8");
//		Videos.add(video);
//
//		
//		
//
//		
//		
//		// Adding child data
//		Jokes = new ArrayList<HashMap<Byte, String>>();
//		
//		joke = new HashMap<Byte, String>();
//		joke.put((byte) 0, "The Conjuring");
//		joke.put((byte) 1, "wzWKFmJFFdA Boob Tube Pranks - Best of Just for Laughs Gags fW7yowZBqg8 Stinky Breath Prank");
//		Jokes.add(joke);
//		
//		joke = new HashMap<Byte, String>();
//		joke.put((byte) 0, "Despicable Me 2");
//		joke.put((byte) 1, "wzWKFmJFFdA Boob Tube Pranks - Best of Just for Laughs Gags fW7yowZBqg8 Stinky Breath Prank");
//		Jokes.add(joke);
//		
//		
//
//		listDataChild.put(listDataHeader.get(0), Images); // Header, Child data
//		listDataChild.put(listDataHeader.get(1), Animations);
//		listDataChild.put(listDataHeader.get(2), Videos);
//		listDataChild.put(listDataHeader.get(3), Jokes);
//	}
//	
//	
//	
//	
//	
//	
//	
//
////case 0:
////	list = listDataChild.get(Constants.IMAGES);
////	intent = new Intent(Search.this, ImageViewer.class);
////	intent.putExtra(Constants.TAG_SRC, list.get(childPosition).get(Constants.TAG_SRC));
////	intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(Constants.TAG_ALT));
////	startActivity(intent);
////	break;
////case 1:
////	list = listDataChild.get(Constants.ANIMATIONS);
////	intent = new Intent(Search.this, AnimationViewer.class);
////	intent.putExtra(Constants.TAG_SRC, list.get(childPosition).get(Constants.TAG_SRC));
////	intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(Constants.TAG_ALT));
////	startActivity(intent);
////	break;
////
////case 2:
////	list = listDataChild.get(Constants.VIDEOS);
////	intent = new Intent(Search.this, VideoViewer.class);
////	intent.putExtra(Constants.TAG_URL, list.get(childPosition).get(Constants.TAG_URL));
////	intent.putExtra(Constants.TAG_ALT, list.get(childPosition).get(Constants.TAG_ALT));
////	startActivity(intent);
////	break;
////	
////case 3:
////	list = listDataChild.get(Constants.JOKES);
////	intent = new Intent(Search.this, JokeViewer.class);
////	intent.putExtra(Constants.TAG_TITLE, list.get(childPosition).get(Constants.TAG_TITLE));
////	intent.putExtra(Constants.TAG_TEXT, list.get(childPosition).get(Constants.TAG_TEXT));
////	startActivity(intent);
////	break;
////
////default:
////	break;
//
//
//}
