//package com.togrulseyid.gags.main;
//
//import android.os.Bundle;
//import com.actionbarsherlock.app.SherlockPreferenceActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//import com.actionbarsherlock.view.Window;
//import com.togrulseyid.gags.viewer.R;
//
///**
// * ك�?لّ�? نَ�?ْس�? ذَائ�?قَة�? الْمَوْت�? ۖ ث�?مَّ إ�?لَيْنَا ت�?رْجَع�?ونَ
// * */
//
//public class Preference extends SherlockPreferenceActivity {
//	
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //Used to put dark icons on light action bar
//    	boolean isLight = true ;
//        menu.add("Save")
//            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
////        menu.add("Search")
////            .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
////            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
////        menu.add("Refresh")
////            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
////            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	// TODO Auto-generated method stub
//    	switch (item.getItemId()) {
//    	
//    	 case android.R.id.home:
//		    	finish();
//		    	return true;
//			
//		case 1:
//	    	saveFile();
//	    	return true;
//	    	
//	    default: 
//	    	return super.onOptionsItemSelected(item);  
//
//    	}
//    }
//    
//	private void saveFile() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//    protected void onCreate(Bundle savedInstanceState) {
////    	setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);//Theme_Sherlock_Light
//		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
//		
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        super.onCreate(savedInstanceState);
//
//        addPreferencesFromResource(R.xml.preferences);
//    }
//}
//
