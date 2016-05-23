package com.fuadirifqi.android.mygrammarapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.UUID;

/**
 * Created by fuadirifqi on 5/16/16.
 */
public class GrammarFragment extends Fragment {

    private static final String ARG_GRAMMAR_ID = "grammar_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_IMAGE = "DialogImage";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;

    private Grammar mGrammar;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mValidCheckBox;
    private Button mSuspectButton;

    private Button mReportButton;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private File mPhotoFile;

    public static GrammarFragment newInstance(UUID grammarId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRAMMAR_ID, grammarId);

        GrammarFragment fragment = new GrammarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID grammarId = (UUID)getArguments().getSerializable(ARG_GRAMMAR_ID);

        mGrammar = GrammarLab.get(getActivity()).getGrammar(grammarId);

        mPhotoFile = GrammarLab.get(getActivity()).getPhotoFile(mGrammar);

    }

    @Override
    public void onPause(){
        super.onPause();
        GrammarLab.get(getActivity()).updateGrammar(mGrammar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_grammar, container, false);

        mTitleField = (EditText)v.findViewById(R.id.grammar_title);
        mTitleField.setText(mGrammar.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGrammar.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.grammar_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mGrammar.getDate());
                dialog.setTargetFragment(GrammarFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mValidCheckBox = (CheckBox)v.findViewById(R.id.grammar_valid);
        mValidCheckBox.setChecked(mGrammar.isValid());
        mValidCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGrammar.setValid(isChecked);
            }
        });

        mReportButton = (Button)v.findViewById(R.id.grammar_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getGrammarReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.grammar_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.grammar_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mGrammar.getSuspect() != null){
            mSuspectButton.setText(mGrammar.getSuspect());
        }

        PackageManager pm = getActivity().getPackageManager();
        if(pm.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton)v.findViewById(R.id.grammar_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(pm) != null;
        //boolean canTakePhoto = true;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.grammar_photo);
        updatePhotoView();

        mPhotoView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                ImageThumbnailFragment dialog = ImageThumbnailFragment.newInstance(mPhotoFile);
                dialog.show(manager, DIALOG_IMAGE);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_grammar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_delete_grammar:
                GrammarLab.get(getActivity()).deleteGrammar(mGrammar);
                Intent intent = new Intent(getActivity(), GrammarListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date =  (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mGrammar.setDate(date);
            updateDate();
        }

        else if (requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();

            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try{
                if(c.getCount() == 0){
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                mGrammar.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally{
                c.close();
            }
        }

        else if(requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mGrammar.getDate().toString());
    }

    private String getGrammarReport(){
        String validString = null;

        if(mGrammar.isValid()){
            validString = getString(R.string.grammar_report_solved);
        }else{
            validString = getString(R.string.grammar_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mGrammar.getDate()).toString();

        String suspect = mGrammar.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.grammar_report_no_suspect);
        }else{
            suspect = getString(R.string.grammar_report_suspect, suspect);
        }

        String rep = getString(R.string.grammar_report, mGrammar.getTitle(), dateString, validString, suspect);

        return rep;

    }


    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
