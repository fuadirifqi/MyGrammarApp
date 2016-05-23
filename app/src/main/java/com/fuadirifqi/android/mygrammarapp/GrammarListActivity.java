package com.fuadirifqi.android.mygrammarapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


/**
 * Created by fuadirifqi on 5/16/16.
 */
public class GrammarListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new GrammarListFragment();
    }
}
