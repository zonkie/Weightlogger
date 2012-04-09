package de.ardunoid.weightlogger;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;
import com.androidplot.xy.XYPlot;

import java.util.Arrays;

import de.ardunoid.weightlogger.R;
import de.ardunoid.weightlogger.DBAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.app.ListActivity;

public class weightShowActivity extends ListActivity {
	private DBAdapter DBAdapter;
	private Cursor mNotesCursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weightshow);

		try {
			Log.d("test", "Trying to read Data");
			DBAdapter = new DBAdapter(this);
			DBAdapter.open();
			fillData();
			DBAdapter.close();
			Log.d("test", "Closed DB Adapter");
		} catch (Exception e) {
			Log.e("test", "Caught Exception");
			Log.e("test", e.getMessage());
			Context context = getApplicationContext();
			CharSequence text = "I'm sorry, there was en Error reading the latest Entry from the Database! "
					+ e.getMessage();
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}

	private void fillData() {
		mNotesCursor = DBAdapter.getList();
		startManagingCursor(mNotesCursor);

		String[] from = new String[] { de.ardunoid.weightlogger.DBAdapter.KEY_VALUE };
		Log.d("test", "Got KEY_ITEMNAME static");
		int[] to = new int[] { R.id.text1 };
		Log.d("test", "Did something with an int...");
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.weightshow, mNotesCursor, from, to);
		Log.d("test", "created simplecursoradapter");
		setListAdapter(notes);
		Log.d("test", "set list adapter");
	}

}
