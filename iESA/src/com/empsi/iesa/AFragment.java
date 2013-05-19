package com.empsi.iesa;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

public class AFragment extends SherlockListFragment {

	public static final String TAG = AFragment.class.getSimpleName();
	public ActionBar actionBar;
	public List<ParsedRow> entries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;
	public String initialTitle;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		try {
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			/* Create a new ContentHandler and apply it to the XML-Reader*/
			PlistHandler myHandler = new PlistHandler();
			xr.setContentHandler(myHandler);

			/* Parse the xml-data from our file. */
			Resources res = getResources();
			InputStream is = res.openRawResource(R.raw.data);
			xr.parse(new InputSource(is));
			/* Parsing has finished. */

			/* Our PlistHandler now provides the parsed data to us. */
			this.entries = myHandler.getListOfRows();

			/* Set the result to be displayed in our GUI. */
			//              Toast.makeText(this, parsedExampleDataSet.toString(), Toast.LENGTH_LONG).show();

			/* Set the result to be displayed in our GUI. */
			EmpsiAdapter adapter = new EmpsiAdapter(getActivity(), R.layout.empsi_custom_row, this.entries, this);
			setListAdapter(adapter);

		} catch (Exception e) {
			/* Display any Error to the GUI. */
			Log.e(TAG, "XMLQueryError", e);
		}
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		//	        Toast.makeText(this, this.currentEntries.get(position).getView(), Toast.LENGTH_SHORT).show();
		ParsedRow clickedRow = EmpsiAdapter.currentData.get(position);
		int view = Integer.parseInt(clickedRow.getView());
		String title = clickedRow.getTitle();
		String scroll = clickedRow.getScroll();

		FileList fl = new FileList();
		String url = fl.getURL(view);

		Intent i = new Intent(getActivity(), MainActivity.class);

		i.putExtra("view", url);
		i.putExtra("title", title);
		i.putExtra("scroll", scroll);
		i.setData(Uri.parse(url));

		Log.d(TAG, "INTENT: " + title);
		Log.d(TAG, "INTENT: " + url);
		Log.d(TAG, "INTENT: " + scroll);

		//	        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

		startActivity(i);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			/* Set the result to be displayed in our GUI. */
			int indexOfLast = EmpsiAdapter.prevData.size() - 1;
			this.previousEntries = EmpsiAdapter.prevData.get(indexOfLast);
			EmpsiAdapter.prevData.remove(indexOfLast);
			EmpsiAdapter adapter = new EmpsiAdapter(getActivity(), R.layout.empsi_custom_row, this.previousEntries, this);
			setListAdapter(adapter);

			break;
		default:
			getActivity().finish();
			Toast.makeText(getActivity(), "home pressed", Toast.LENGTH_LONG).show();
		}
		return true;
	}
}