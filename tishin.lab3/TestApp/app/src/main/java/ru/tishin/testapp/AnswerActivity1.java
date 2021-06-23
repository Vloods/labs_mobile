package ru.tishin.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AnswerActivity1 extends AppCompatActivity {
    private int point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer1);

        Button button = (Button) findViewById(R.id.button_next_question_1);
        button.setEnabled(false);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_question_1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == -1){
                    button.setEnabled(false);
                } else {
                    if(checkedId == R.id.radiobutton_question_1_answer_4){
                        point = 1;
                    } else {
                        point = 0;
                    }
                    button.setEnabled(true);
                }
            }
        });
    }

    public void OnClick(View view) {
        Intent intent = new Intent(AnswerActivity1.this, AnswerActivity2.class);
        intent.putExtra("points", point);
        startActivity(intent);
        finish();
    }
}