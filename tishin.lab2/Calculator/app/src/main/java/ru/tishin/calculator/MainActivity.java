package ru.tishin.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int OPERATION_KIND_NONE = 0;
    private static final int OPERATION_KIND_PLUS = 1;
    private static final int OPERATION_KIND_MINUS = 2;
    private static final int OPERATION_KIND_MULTIPLIER = 3;
    private static final int OPERATION_KIND_DIVIDER = 4;
    private static final int OPERATION_KIND_SQUARE = 5;
    private static final int OPERATION_KIND_SQRT = 6;

    private double result = 0;
    private double firstVal = 0;
    private double secondVal = 0;
    private int operation_kind = 0;

    private String convertToString(double value){
        String str = Double.toString(value);
        int str_length = str.length();
        if(str.indexOf(".") == (str_length - 2) && str.lastIndexOf("0") == (str_length - 1)) {
            return str.substring(0, str_length - 2);
        } else {
            return str;
        }
    }

    private void clearResults(){
        result = 0;
        firstVal = 0;
        secondVal = 0;
        operation_kind = OPERATION_KIND_NONE;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getText(R.string.text_default));

    }

    private void applyEquals(){
        if (operation_kind == OPERATION_KIND_NONE)
            return;
        TextView textView = (TextView) findViewById(R.id.textView);

        switch (operation_kind){
            case OPERATION_KIND_PLUS:
                result = firstVal + secondVal;
                break;
            case OPERATION_KIND_MINUS:
                result = firstVal - secondVal;
                break;
            case OPERATION_KIND_MULTIPLIER:
                result = firstVal * secondVal;
                break;
            case OPERATION_KIND_DIVIDER:
                if (secondVal != 0.0){
                    result = firstVal / secondVal;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Сan't divide by zero", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();
                    return;
                }
                break;
            case OPERATION_KIND_SQUARE:
                result = firstVal * firstVal;
                break;
            case OPERATION_KIND_SQRT:
                result = Math.sqrt(firstVal);
                break;
            default:
                break;
        }

        firstVal = result;
        textView.setText(convertToString(result));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button_erase);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clearResults();
            }

        });

        button = (Button) findViewById(R.id.button_equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyEquals();
            }
        });
    }

    public void OnClick(View view){
        TextView textView = findViewById(R.id.textView);
        String text_buffer = textView.getText().toString();

        if(text_buffer.equals(getText(R.string.text_default))) {
            text_buffer = "";
        }

        switch (view.getId()){
            case R.id.button_0:
                text_buffer += "0";
                break;
            case R.id.button_1:
                text_buffer += "1";
                break;
            case R.id.button_2:
                text_buffer += "2";
                break;
            case R.id.button_3:
                text_buffer += "3";
                break;
            case R.id.button_4:
                text_buffer += "4";
                break;
            case R.id.button_5:
                text_buffer += "5";
                break;
            case R.id.button_6:
                text_buffer += "6";
                break;
            case R.id.button_7:
                text_buffer += "7";
                break;
            case R.id.button_8:
                text_buffer += "8";
                break;
            case R.id.button_9:
                text_buffer += "9";
                break;
            case R.id.button_dot:
                if(text_buffer.isEmpty()){
                    text_buffer = "0";
                } else {
                    if(!text_buffer.contains(".")) {
                        text_buffer += ".";
                    }
                }
                break;
            default:
                break;
        }

        if(operation_kind != OPERATION_KIND_NONE) {
            try {
                secondVal = Double.valueOf(text_buffer);
            }
            catch (NumberFormatException e)
            {
                secondVal = 0;
            }
        }

        textView.setText(text_buffer);
    }

    public void OnClickOperation(View view){
        TextView textView = (TextView) findViewById(R.id.textView);
        String text_buffer = textView.getText().toString();

        if(operation_kind == OPERATION_KIND_NONE) {
            try {
                firstVal = Double.valueOf(text_buffer);
            } catch (NumberFormatException e) {
                firstVal = 0;
            }
        }

        switch (view.getId()){
            case R.id.button_plus:
                operation_kind = OPERATION_KIND_PLUS;
                break;
            case R.id.button_minus:
                operation_kind = OPERATION_KIND_MINUS;
                break;
            case R.id.button_multiplier:
                operation_kind = OPERATION_KIND_MULTIPLIER;
                break;
            case R.id.button_divider:
                operation_kind = OPERATION_KIND_DIVIDER;
                break;
            case R.id.button_square:
                operation_kind = OPERATION_KIND_SQUARE;
                firstVal *= firstVal;
                break;
            case R.id.button_sqrt:
                operation_kind = OPERATION_KIND_SQRT;
                firstVal = Math.sqrt(firstVal);
                break;
            default:
                return;
        }

        if(operation_kind == OPERATION_KIND_SQUARE || operation_kind == OPERATION_KIND_SQRT) {
            textView.setText(convertToString(firstVal));
        } else {
            textView.setText(getText(R.string.text_default));
        }
    }

}