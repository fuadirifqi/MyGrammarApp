package com.fuadirifqi.android.mygrammarapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.UUID;

public class GrammarActivity extends SingleFragmentActivity {

    private static final String EXTRA_GRAMMAR_ID = "com.fuadirifqi.android.mygrammarapp.grammar_id";

    public static Intent newIntent(Context packageContext, UUID grammarId){
        Intent intent = new Intent(packageContext, GrammarActivity.class);
        intent.putExtra(EXTRA_GRAMMAR_ID, grammarId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID grammarId = (UUID)getIntent().getSerializableExtra(EXTRA_GRAMMAR_ID);
        return GrammarFragment.newInstance(grammarId);
    }


}
