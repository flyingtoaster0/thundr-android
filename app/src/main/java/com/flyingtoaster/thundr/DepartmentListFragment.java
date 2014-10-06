package com.flyingtoaster.thundr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tim on 10/6/14.
 */
public class DepartmentListFragment extends Fragment implements GetJSONArrayListener {

    private View mRootView;
    ListView listview;
    JSONArray jArray;
    ProgressBar bar;
    FragmentCallbackListener mListener;
    private String mDept;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_browse, container, false);

        listview = (ListView) mRootView.findViewById(R.id.list);
        bar = (ProgressBar) mRootView.findViewById(R.id.loader);

        Bundle args = getArguments();
        mDept = args.getString("dept_code");

        new GetJSONArrayTask(this, "http://thundr.ca/api/courses/department/"+mDept).execute();

        return mRootView;
    }

    public void setFragmentCallbackListener(FragmentCallbackListener listener) {
        mListener = listener;
    }

    public void onJSONArrayPreExecute(){};
    public void onJSONArrayProgressUpdate(String... params){};
    public void onJSONArrayPostExecute(final JSONArray jArray) {
        this.jArray = jArray;
        final ArrayList<JSONObject> list = new ArrayList<JSONObject>();

        try
        {
            for (int i = 0; i < jArray.length(); ++i) {
                list.add(jArray.getJSONObject(i));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        Log.d("Debug","Loading complete");

        final CourseAdapter adapter = new CourseAdapter(getActivity(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final JSONObject item = (JSONObject)parent.getItemAtPosition(position);

                try
                {
                    Bundle b = new Bundle();
                    b.putString("dept_code", item.getString("department"));
                    b.putString("course_code", item.getString("course_code"));
                    mListener.onCourseSelected(b);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    Log.e("Exceptions", e.toString());
                }
            }
        });
        bar.setVisibility(View.GONE);
    };
    public void onJSONArrayCancelled(){};
}
