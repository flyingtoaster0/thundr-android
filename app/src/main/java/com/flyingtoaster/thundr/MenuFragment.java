package com.flyingtoaster.thundr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tim on 2014-10-05.
 */
public class MenuFragment extends Fragment {
    View mRootView;
    ListView mListView;
    EditText mSearchEditText;

    MainActivity mMainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_menu, container, false);

        mMainActivity = (MainActivity)getActivity();

        mListView = (ListView)mRootView.findViewById(R.id.menu_listview);
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);

        mListView.setAdapter(new MenuListAdapter(getActivity(), list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                mMainActivity.goToFragment(position);
            }
        });

        mSearchEditText = (EditText)mRootView.findViewById(R.id.menu_searchedittext);
        mSearchEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        mRootView.setFocusableInTouchMode(true);
        mRootView.requestFocus();
        return mRootView;
    }
}
