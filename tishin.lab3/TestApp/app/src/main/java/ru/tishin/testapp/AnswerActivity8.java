package ru.tishin.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class AnswerActivity8 extends AppCompatActivity {
    private int point = 0;
    private int sum_previous_points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer8);

        sum_previous_points = getIntent().getIntExtra("points", 0);

        Button button = (Button) findViewById(R.id.button_test_finish);
        button.setEnabled(false);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_question_8);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == -1){
                    button.setEnabled(false);
                } else {
                    if(checkedId == R.id.radiobutton_question_8_answer_2){
                        point = 1;
                    } else {
                        point = 0;
                    }
                    button.setEnabled(true);
                }
            }
        });
    }

    public void OnClick(View view){
        Intent intent = new Intent(AnswerActivity8.this, MainActivity.class);
        intent.putExtra("points", sum_previous_points + point);
        startActivity(intent);
        finish();
    }
}