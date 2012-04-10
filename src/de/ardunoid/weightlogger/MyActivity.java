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
	private Cursor mValuesCursor;
	private Cursor mBodyfatCursor;
	private Cursor mBodywaterCursor;
	private Cursor mBodybonemassCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myexample);

		try {
			// Initialize our XYPlot reference:
			mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
			db.open();
			// Weight
			mValuesCursor = db.getWeightValues();
			startManagingCursor(mValuesCursor);
			ArrayList<Number> weightGraphData = new ArrayList<Number>();
			for (mValuesCursor.moveToFirst(); mValuesCursor.moveToNext(); mValuesCursor.isAfterLast()) {
				weightGraphData.add(mValuesCursor.getInt(mValuesCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_VALUE)));
			}
			// Turn the above arrays into XYSeries:
			XYSeries bodyWeightSeries = new SimpleXYSeries(weightGraphData,
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
					"Weight"); 

			LineAndPointFormatter bodyWeightSeriesFormat = new LineAndPointFormatter(
					Color.rgb(255, 190, 0), // line color
					Color.rgb(255, 220, 0), // point color
					null); // fill color (optional)

			mySimpleXYPlot.addSeries(bodyWeightSeries, bodyWeightSeriesFormat);
			
			
			// Fat
			mBodyfatCursor = db.getBodyfatValues();
			startManagingCursor(mBodyfatCursor);
			ArrayList<Number> fatGraphData = new ArrayList<Number>();
			for (mBodyfatCursor.moveToFirst(); mBodyfatCursor.moveToNext(); mBodyfatCursor.isAfterLast()) {
				fatGraphData.add(mBodyfatCursor.getInt(mBodyfatCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_BODYFAT)));
			}
			// Turn the above arrays into XYSeries:
			XYSeries bodyFatSeries = new SimpleXYSeries(fatGraphData,
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
					"Fat"); 

			LineAndPointFormatter bodyFatSeriesFormat = new LineAndPointFormatter(
					Color.rgb(0, 0, 0), // line color
					Color.rgb(0, 10, 0), // point color
					null); // fill color (optional)

			mySimpleXYPlot.addSeries(bodyFatSeries, bodyFatSeriesFormat);

			// Water
			mBodywaterCursor = db.getBodywaterValues();
			startManagingCursor(mBodywaterCursor);
			ArrayList<Number> waterGraphData = new ArrayList<Number>();
			for (mBodywaterCursor.moveToFirst(); mBodywaterCursor.moveToNext(); mBodywaterCursor.isAfterLast()) {
				waterGraphData.add(mBodywaterCursor.getInt(mBodywaterCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_BODYWATER)));
			}
			// Turn the above arrays into XYSeries:
			XYSeries bodyWaterSeries = new SimpleXYSeries(waterGraphData,
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
					"Water"); 

			LineAndPointFormatter bodyWaterSeriesFormat = new LineAndPointFormatter(
					Color.rgb(0, 0, 255), // line color
					Color.rgb(0, 0, 100), // point color
					null); // fill color (optional)

			mySimpleXYPlot.addSeries(bodyWaterSeries, bodyWaterSeriesFormat);

			
			
			// Bonemass
			mBodybonemassCursor = db.getBodybonemassValues();
			startManagingCursor(mBodybonemassCursor);
			ArrayList<Number> boneGraphData = new ArrayList<Number>();
			for (mBodybonemassCursor.moveToFirst(); mBodybonemassCursor.moveToNext(); mBodybonemassCursor.isAfterLast()) {
				boneGraphData.add(mBodybonemassCursor.getInt(mBodybonemassCursor.getColumnIndex(de.ardunoid.weightlogger.DBAdapter.KEY_BODYBONE)));
			}
			// Turn the above arrays into XYSeries:
			XYSeries bodyBoneSeries = new SimpleXYSeries(boneGraphData,
					SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
					"Bone"); 

			LineAndPointFormatter bodyBoneSeriesFormat = new LineAndPointFormatter(
					Color.rgb(255, 255, 100), // line color
					Color.rgb(100, 100, 0), // point color
					null); // fill color (optional)

			mySimpleXYPlot.addSeries(bodyBoneSeries, bodyBoneSeriesFormat);




			// Reduce the number of range labels
			mySimpleXYPlot.setTicksPerRangeLabel(3);

			mySimpleXYPlot.setDomainLabel("ardunoid.de");
			mySimpleXYPlot.setBackgroundColor(Color.rgb(10, 10, 10));
			mySimpleXYPlot.setPlotMarginTop(50);
			mySimpleXYPlot.setPlotMarginBottom(50);
			
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