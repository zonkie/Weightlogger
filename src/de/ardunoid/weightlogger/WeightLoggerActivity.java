package de.ardunoid.weightlogger;

import java.sql.Date;
import java.util.Calendar;

import de.ardunoid.weightlogger.DBAdapter;
import de.ardunoid.weightlogger.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeightLoggerActivity extends Activity {
	DBAdapter db = new DBAdapter(this);
	String height = "";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final SharedPreferences einstellungen = preferencesActivity
				.getPreferences(this);

		height = einstellungen.getString("prefHeight", "0");
		TextView heightText = (TextView) findViewById(R.id.textHeight);
		heightText.setText("Height: "+ height);
	}

	public void onClickAddHandler(final View view) {
		try {

			final EditText value = (EditText) findViewById(R.id.edtWeight);
			final EditText BodyFat = (EditText) findViewById(R.id.edtBodyfat);
			final EditText BodyWater = (EditText) findViewById(R.id.edtBodyWater);
			final EditText BodyBone = (EditText) findViewById(R.id.edtBodyBone);
			final float bmiCalc = this.calcBmi(Float.valueOf(value.getText().toString()), Float.valueOf(height));
			Log.d("test", "Adding entry");

			try {
				final Calendar c = Calendar.getInstance();
				Context context = getApplicationContext();
				Log.d("Soenke", "context getApplicationContext");
				CharSequence bubbleText = "";
				db.open();
				Log.d("test", "insert weight");
				db.insertWeight(String.valueOf(c.getTime().toString()), value
						.getText().toString(), BodyFat.getText().toString(),
						BodyWater.getText().toString(), BodyBone.getText()
								.toString(), String.valueOf(bmiCalc));
				Log.d("test", "Inserted entry");
				bubbleText = "Ok";

				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, bubbleText, duration);
				toast.show();

				value.setText("");
				BodyFat.setText("");
				BodyWater.setText("");
				BodyBone.setText("");
			} catch (Exception e) {
				Log.e("test", "ERROR ADDING!");
				Log.e("test", e.getMessage());
				db.close();
				Context context = getApplicationContext();
				CharSequence text = "FAIL!!!!!!! " + e.getMessage();
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

			}
			db.close();

		} catch (Exception e) {
			// TODO: Do something...
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.optExit:
			finish();
			return true;

		case R.id.optShowAll:
			final Intent intentShowAll = new Intent(this,
					weightShowActivity.class);
			startActivity(intentShowAll);
			return true;

		case R.id.optShowGraph:
			final Intent intentShowGraph = new Intent(this, MyActivity.class);
			startActivity(intentShowGraph);
			return true;

		case R.id.optSettings:
			final Intent intentShowSettings = new Intent(this, preferencesActivity.class);
			startActivity(intentShowSettings);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	public float calcBmi(float weight, float height){
		Log.w("test","height: " + String.valueOf(height));
		return (float) (weight / Math.pow((height/100), 2));
	}
}