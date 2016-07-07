package it.xabaras.android.acheckablewordlist.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import it.xabaras.android.acheckablewordlist.widget.ACheckableWordList;
import it.xabaras.android.logger.Logger;

public class MainActivity extends AppCompatActivity {
    private ACheckableWordList wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wordList = (ACheckableWordList) findViewById(R.id.wordList);
        wordList.setOnItemClickListener(new ACheckableWordList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, boolean isSelected) {
                Snackbar.make(v, String.format("Word %d clicked, %s", position, isSelected ? "selected" : "unselected") , Snackbar.LENGTH_SHORT)
                    .show();
            }
        });


        Button btnShowSelection = (Button) findViewById(R.id.btnShowSelection);
        btnShowSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Integer> selection = wordList.getSelection();
                    if ( selection.size() > 0 ) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage("You have selected items at following positions: " + TextUtils.join(",", selection))
                                .setNeutralButton(android.R.string.ok, null)
                                .show();
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.app_name)
                                .setMessage("You have selected no items")
                                .setNeutralButton(android.R.string.ok, null)
                                .show();
                    }
                } catch(Exception e) {
                    Logger.e(this, e);
                }
            }
        });


        Button btnClearSelection = (Button) findViewById(R.id.btnClearSelection);
        btnClearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordList.clearSelection();
            }
        });

        reset();
        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View alertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_word_prompt, null, false);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Word")
                        .setView(alertView)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    EditText text1 = (EditText) alertView.findViewById(R.id.text1);
                                    if ( text1.getText().length() > 0 ) {
                                        wordList.addWord(text1.getText().toString());
                                        Snackbar.make(wordList, String.format("Word %s added", text1.getText().toString()), Snackbar.LENGTH_LONG)
                                                .setAction(android.R.string.cancel, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        wordList.removeWordAt(wordList.getWordCount() - 1);
                                                    }
                                                })
                                                .show();
                                    }
                                } catch(Exception e) {
                                    Logger.e(this, e);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void reset() {
        try {
            wordList.removeAllWords();
            wordList.setWords(Arrays.asList(new String[]{"Hello", "ACheckableWordList", "This", "is", "a", "sample"}));
        } catch(Exception e) {
            Logger.e(this, e);
        }
    }
}
