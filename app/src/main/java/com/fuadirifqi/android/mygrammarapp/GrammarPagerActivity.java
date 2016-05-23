package com.fuadirifqi.android.mygrammarapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by fuadirifqi on 5/17/16.
 */
public class GrammarPagerActivity extends AppCompatActivity {

    private static final String EXTRA_GRAMMAR_ID = "com.fuadirifqi.android.mygrammarapp.grammar_id";

    private ViewPager mViewPager;
    private List<Grammar> mGrammars;


    public static Intent newIntent(Context packageContext, UUID grammarId){
        Intent intent = new Intent(packageContext, GrammarPagerActivity.class);
        intent.putExtra(EXTRA_GRAMMAR_ID, grammarId);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_pager);

        UUID grammarId = (UUID) getIntent().getSerializableExtra(EXTRA_GRAMMAR_ID);

        mViewPager = (ViewPager)findViewById(R.id.activity_grammar_pager_view_pager);

        mGrammars = GrammarLab.get(this).getGrammars();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Grammar grammar = mGrammars.get(position);
                return GrammarFragment.newInstance(grammar.getId());
            }

            @Override
            public int getCount() {
                return mGrammars.size();
            }
        });

        for(int i = 0; i < mGrammars.size(); i++){
            if(mGrammars.get(i).getId().equals(grammarId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
