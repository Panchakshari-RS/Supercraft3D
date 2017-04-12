package com.trova.supercraft;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Panchakshari on 16/3/2017.
 */

public class MyJobNotesTabFragment extends Fragment {
    private static Context context;
    private View contentview = null;
    private static EditText etNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("MyJobNotesTabFragment", "0 .........");
        contentview = inflater.inflate(R.layout.notes_tab, container, false);
        context = this.getContext();

        // also show engineer notes
        String notes = myGlobals.currJobActive.getStlDoctorFileNotes();
        etNotes = (EditText) contentview.findViewById(R.id.notes);
        if((notes != null) && (!notes.equals("")) && (!notes.equals("null")) && (notes.length() > 0)) {
            notes = notes.replace("<br />", "\n");
            notes = notes.replace("<br/>", "\n");
            notes = notes.replace("<br >", "\n");
            notes = notes.replace("<br>", "\n");
            etNotes.setText(notes);
        }
        if(myGlobals.isCompletedJob) {
            etNotes.setKeyListener(null);
/*
            etNotes.setClickable(false);
            etNotes.setFocusable(false);
            etNotes.setFocusableInTouchMode(false);
*/
        } else {
            etNotes.addTextChangedListener(new MyNotesTextWatcher(etNotes));
        }

        return contentview;
    }

        private class MyNotesTextWatcher implements TextWatcher {

        private View view;

        private MyNotesTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int id = view.getId();

            if(id == R.id.notes) {
                String notes = getNotes();
                myGlobals.dbHandler.updateJobDoctorNotes(myGlobals.jobId, notes);
            }
        }
    }

    public static String getNotes() {
        return etNotes.getText().toString();
    }
}
