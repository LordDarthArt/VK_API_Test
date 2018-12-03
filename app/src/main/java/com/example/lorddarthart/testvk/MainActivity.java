package com.example.lorddarthart.testvk;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText txtId, txtCount;
    private TextView txtResultCount;
    private View view2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    List<NoteText> noteText;
    private Button btnShow;
    private GetWall gw;
    private ConstraintLayout consLayRV, consttraintLayoutCountResult;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gw = new GetWall();
        setContentView(R.layout.activity_main);

        InputFilter infiltId = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        txtId.setText(dest);
                        txtId.setSelection(dest.length());
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter infiltCount = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        txtCount.setText(dest);
                        txtCount.getSelectionEnd();
                        return "";
                    }
                }
                return null;
            }
        };

        txtId = findViewById(R.id.txtId);
        txtCount = findViewById(R.id.txtCount);
        recyclerView = findViewById(R.id.rvMain);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnShow = findViewById(R.id.btnShow);
        consLayRV = findViewById(R.id.consLayRV);
        txtResultCount = findViewById(R.id.txtResultCount);
        consttraintLayoutCountResult = findViewById(R.id.constraintLayoutCountResult);
        view2 = findViewById(R.id.view2);

        txtId.setFilters(new InputFilter[]{infiltId});
        txtCount.setFilters(new InputFilter[]{infiltCount});

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    result = new GetWall().execute().get();
                    if (result==1) {
                        initializeAdapter();
                        view2.setVisibility(View.VISIBLE);
                        consttraintLayoutCountResult.setVisibility(View.VISIBLE);
                        txtResultCount.setText("Записей с текстом: "+noteText.size());
                    } else {
                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText("Записи недоступны :-(");
                        textView.setTextColor(Color.GRAY);
                        textView.setTextSize((int) getApplicationContext().getResources().getDisplayMetrics().density, 16);
                        textView.setSingleLine(false);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.setGravity(Gravity.CENTER);
                        linearLayout.addView(textView);
                        consLayRV.addView(linearLayout);
                        view2.setVisibility(View.GONE);
                        consttraintLayoutCountResult.setVisibility(View.GONE);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        txtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtId.getText().toString().equals("")||txtCount.getText().toString().equals("")) {
                    btnShow.setEnabled(false);
                }
                if (txtId.getText().toString().length()>0&&txtCount.getText().toString().length()>0) {
                    btnShow.setEnabled(true);
                }
            }
        });
        txtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtId.getText().toString().equals("")||txtCount.getText().toString().equals("")) {
                    btnShow.setEnabled(false);
                }
                if (txtId.getText().toString().length()>0&&txtCount.getText().toString().length()>0) {
                    btnShow.setEnabled(true);
                }
            }
        });
    }

    private void initializeAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), noteText);
        recyclerView.bringToFront();
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private class GetWall extends AsyncTask<Void, Void, Integer> {
        int result;

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                if (noteText!=null) {
                    noteText.clear();
                }
                noteText = new HttpService().GetNotes(txtId.getText().toString(), Long.valueOf(txtCount.getText().toString()));
                if (!(noteText==null)&&(noteText.size()>0)&&!(noteText.get(0).getResponse() == 401)&&!(noteText.get(0).getResponse() == 18)&&!(noteText.get(0).getResponse() == 19)&&!(noteText.get(0).getResponse() == 30)) {
                    result = 1;
                } else {
                    result = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result ;
        }
    }
}
