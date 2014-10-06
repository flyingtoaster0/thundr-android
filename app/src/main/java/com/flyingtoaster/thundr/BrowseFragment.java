package com.flyingtoaster.thundr;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by tim on 10/5/14.
 */
public class BrowseFragment extends Fragment implements GetJSONArrayListener {
    private View mRootView;
    ListView listview;
    JSONArray jArray;
    ProgressBar bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_browse, container, false);

        new GetJSONArrayTask(this, "http://thundr.ca/api/departments").execute();

        listview = (ListView) mRootView.findViewById(R.id.list);

        bar = (ProgressBar) mRootView.findViewById(R.id.loader);

        return mRootView;
    }

    public void onJSONArrayPreExecute(){};
    public void onJSONArrayProgressUpdate(String... params){};
    public void onJSONArrayPostExecute(JSONArray jArray) {
        this.jArray = jArray;
        final ArrayList<String> list = new ArrayList<String>();

        try
        {
            for (int i = 0; i < jArray.length(); ++i) {
                list.add(jArray.getJSONObject(i).getString("dept_name"));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                /*
                try
                {
                    final String deptCode = (String)MainActivity.this.jArray.getJSONObject(position).get("dept_code");
                    Intent intent = new Intent(MainActivity.this, CourseListActivity.class);

                    Bundle b = new Bundle();
                    b.putString("dept_code", deptCode);
                    intent.putExtras(b);
                    startActivity(intent);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                */
            }
        });
        bar.setVisibility(View.GONE);
        //progressBar.dismiss();
    };
    public void onJSONArrayCancelled(){};
}
