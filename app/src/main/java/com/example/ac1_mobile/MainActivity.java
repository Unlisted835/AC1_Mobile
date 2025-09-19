package com.example.ac1_mobile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAddBook = findViewById(R.id.btnAddBook);
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editAuthor = findViewById(R.id.editAuthor);
        CheckBox ckbFinished = findViewById(R.id.ckbFinished);
        LinearLayout lytBooks = findViewById(R.id.lytBooks);

        lytBooks.setOrientation(LinearLayout.VERTICAL);
        lytBooks.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        btnAddBook.setOnClickListener(view -> {
            bookToBeAdded.title = editTitle.getText().toString();
            bookToBeAdded.author = editAuthor.getText().toString();
            bookToBeAdded.finished = ckbFinished.isChecked();

            if (bookToBeAdded.IsNotComplete()) {
                Toast.makeText(this, "Preencha todos os campos do livro", Toast.LENGTH_LONG).show();
                return;
            }

            lytBooks.addView(createBookListView(bookToBeAdded));
            bookToBeAdded = new Book();

            ckbFinished.setActivated(bookToBeAdded.finished);
            editTitle.setText(bookToBeAdded.title);
            editAuthor.setText(bookToBeAdded.author);
        });
    }

    private View createBookListView(Book book) {

        LinearLayout lytBooks = findViewById(R.id.lytBooks);

        var defaultParams = new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        var container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(defaultParams);
        container.setPadding(16, 16, 16, 16);
        container.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        var titleView = new TextView(this);
        titleView.setLayoutParams(defaultParams);
        titleView.setText("Título: " + book.title);
        titleView.setTextSize(22);
        titleView.setSingleLine(true);

        var authorView = new TextView(this);
        authorView.setLayoutParams(defaultParams);
        authorView.setText("Autor: " + book.author);
        authorView.setTextSize(22);
        authorView.setSingleLine(true);

        var ckbFinished = new CheckBox(this);
        ckbFinished.setLayoutParams(defaultParams);
        ckbFinished.setChecked(book.finished);
        ckbFinished.setTextSize(22);
        ckbFinished.setText("Lido?");

        var removeButton = new Button(this);
        removeButton.setLayoutParams(defaultParams);
        removeButton.setText("Remover");
        ckbFinished.setTextSize(22);
        removeButton.setOnClickListener(v -> lytBooks.removeView(container));

        container.addView(titleView);
        container.addView(authorView);
        container.addView(ckbFinished);
        container.addView(removeButton);

        return container;
    }
    private Book bookToBeAdded = new Book();
    public static class Book {
        public int id;
        public String title;
        public String author;
        public boolean finished = false;
        public boolean IsNotComplete () {
            return title == null || title.isEmpty() || author == null || author.isEmpty();
        }
    }
}