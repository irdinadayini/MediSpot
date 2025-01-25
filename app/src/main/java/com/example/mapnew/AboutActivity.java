package com.example.mapnew;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    Toolbar aboutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutToolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(aboutToolbar);
        getSupportActionBar().setTitle("About");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Correcting the TextView initialization
        TextView txtGitHub = findViewById(R.id.txtGitHub);

        // Using HtmlCompat for compatibility
        txtGitHub.setText(HtmlCompat.fromHtml("<a href='https://github.com/irdinadayini/MediSpot'>Visit my GitHub</a>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        txtGitHub.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}
