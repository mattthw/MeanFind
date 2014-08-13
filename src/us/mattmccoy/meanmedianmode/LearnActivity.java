package us.mattmccoy.meanmedianmode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import us.mattmccoy.meanfind.R;

public class LearnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Learn");
		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_learn);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	public void mean(View view){
		Uri uriUrl = Uri.parse("http://wikipedia.org/wiki/mean");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
	}
	public void median(View view){
		Uri uriUrl2 = Uri.parse("http://wikipedia.org/wiki/median");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl2);
        startActivity(launchBrowser);
	}
	public void mode(View view){
		Uri uriUrl3 = Uri.parse("http://wikipedia.org/wiki/mode");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl3);
        startActivity(launchBrowser);
	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learn, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
