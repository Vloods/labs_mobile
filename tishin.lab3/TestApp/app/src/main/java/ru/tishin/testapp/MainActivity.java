package ru.tishin.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int MAX_SCORE = 8;

    private SharedPreferences sharedPreferences;

    private int count_tries;
    private int best_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        best_score = sharedPreferences.getInt("best_score", 0);
        count_tries = sharedPreferences.getInt("count_tries", 0);

        int points = getIntent().getIntExtra("points", -1);

        if(points != -1) {
            Toast toast = Toast.makeText(this,
                    "Ваш результат " +
                            Integer.toString(points)+ "/" + Integer.toString(MAX_SCORE) + "!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();

            if (best_score < points) {
                best_score = points;
            }

            count_tries += 1;

            sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("best_score", best_score);
            editor.putInt("count_tries", count_tries);
            editor.apply();
        }

        if(count_tries != 0){
            TextView text_count_tries = (TextView) findViewById(R.id.text_count_tries);
            TextView text_best_score = (TextView) findViewById(R.id.text_best_score);


            text_best_score.setVisibility(View.VISIBLE);
            text_best_score.setText(getText(R.string.text_best_score) +
                    Integer.toString(best_score) + "/" + Integer.toString(MAX_SCORE));

            text_count_tries.setVisibility(View.VISIBLE);
            text_count_tries.setText(getText(R.string.text_count_tries) + Integer.toString(count_tries));
        }
    }

    public void OnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AnswerActivity1.class);
        startActivity(intent);
    }
}