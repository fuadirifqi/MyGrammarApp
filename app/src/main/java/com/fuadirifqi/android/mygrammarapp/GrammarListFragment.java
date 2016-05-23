package com.fuadirifqi.android.mygrammarapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by fuadirifqi on 5/16/16.
 */
public class GrammarListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mGrammarRecyclerView;
    private GrammarAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_grammar_list, container, false);

        mGrammarRecyclerView = (RecyclerView)view.findViewById(R.id.grammar_recycler_view);
        mGrammarRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_grammar_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.menu_new_item_grammar:
                Grammar grammar = new Grammar();
                GrammarLab.get(getActivity()).addGrammar(grammar);
                Intent intent = GrammarPagerActivity.newIntent(getActivity(), grammar.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateSubtitle(){
        GrammarLab grammarLab = GrammarLab.get(getActivity());
        int grammarCount = grammarLab.getGrammars().size();
        //String subtitle = getString(R.string.subtitle_format, grammarCount);
        int grammarSize = grammarLab.getGrammars().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, grammarSize, grammarSize);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        GrammarLab grammarLab = GrammarLab.get(getActivity());
        List<Grammar> grammars = grammarLab.getGrammars();

        if(mAdapter == null){
            mAdapter = new GrammarAdapter(grammars);
            mGrammarRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setGrammars(grammars);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();

    }

    private class GrammarHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Grammar mGrammar;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mValidCheckBox;
        private TextView mSuspectTextView;

        public GrammarHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_grammar_title_textview);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_grammar_date_textview);
            mValidCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_grammar_valid_checkbox);
            mSuspectTextView = (TextView)itemView.findViewById(R.id.list_item_grammar_suspect_textview);
        }

        public void bindGrammar(Grammar grammar) {

            mGrammar = grammar;
            mTitleTextView.setText(mGrammar.getTitle());
            mDateTextView.setText(mGrammar.getDate().toString());
            mValidCheckBox.setChecked(mGrammar.isValid());
            if (mGrammar.getSuspect() != null) {
                mSuspectTextView.setText(mGrammar.getSuspect());
            }else{
                mSuspectTextView.setText("---No Suspect---");
            }
        }

        @Override
        public void onClick(View v){
            //Toast.makeText(getActivity(), mGrammar.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();

            //Intent intent = new Intent(getActivity(), GrammarActivity.class);
            Intent intent = GrammarPagerActivity.newIntent(getActivity(), mGrammar.getId());
            startActivity(intent);

        }

    }

    private class GrammarAdapter extends RecyclerView.Adapter<GrammarHolder>{

        private List<Grammar> mGrammars;

        public GrammarAdapter(List<Grammar> grammars){
            mGrammars = grammars;
        }

        @Override
        public GrammarHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_grammar, parent, false);

            return new GrammarHolder(view);
        }

        @Override
        public void onBindViewHolder(GrammarHolder holder, int position){
            Grammar grammar = mGrammars.get(position);

            holder.bindGrammar(grammar);

            //int pos = holder.getAdapterPosition();
            //mAdapter.notifyItemChanged(pos);

        }

        @Override
        public int getItemCount(){
            return mGrammars.size();
        }

        public void setGrammars(List<Grammar> grammars){
            mGrammars = grammars;
        }
    }
}
