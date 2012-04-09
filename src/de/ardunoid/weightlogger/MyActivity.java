package de.ardunoid.weightlogger;

import de.ardunoid.weightlogger.DBAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MyActivity extends Activity {
	DBAdapter db = new DBAdapter(this);
	private XYPlot mySimpleXYPlot;
	private Cursor mNotesCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myexample);

		try {
			// Initialize our XYPlot reference:
			mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
			db.open();
			mNotesCursor = db.getWeightValues();
			startManagingCursor(mNotesCursor);

			// Create two arrays of y-values to plot:
			ArrayList<Number> series1Numbers = new ArrayList<Number>();
			for (mNotesCursor.moveToFirst(); mNotesCursor.moveToNext(); mNotesCursor.isAfterLast()) {
				// The Cursor is now set to the right position
				series1Numbers.add(mNotesCursor.getInt(mNotesCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_VALUE)));
				Log.d("test","Added Number to array: " + String.valueOf(mNotesCursor.getInt(mNotesCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_VALUE))));
			}

			// Turn the above arrays into XYSeries:
			XYSeries series1 = new SimpleXYSeries(series1Numbers, // SimpleXYSeries
																	// takes a
																	// List so
																	// turn our
																	// array
																	// into a
																	// List
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means
															// use the element
															// index as the x
															// value
					"Weight"); // Set the display title of the series

			// Create a formatter to use for drawing a series using
			// LineAndPointRenderer:
			LineAndPointFormatter series1Format = new LineAndPointFormatter(
					Color.rgb(0, 200, 0), // line color
					Color.rgb(0, 100, 0), // point color
					null); // fill color (optional)

			// Add series1 to the xyplot:
			mySimpleXYPlot.addSeries(series1, series1Format);

			// Reduce the number of range labels
			mySimpleXYPlot.setTicksPerRangeLabel(3);

			// By default, AndroidPlot displays developer guides to aid in
			// laying out your plot.
			// To get rid of them call disableAllMarkup():
			mySimpleXYPlot.disableAllMarkup();
			db.close();
		} catch (Exception e) {
			db.close();
		}
	}

}