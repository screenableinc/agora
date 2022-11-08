package com.screenable.agora.customviews;



import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.screenable.agora.R;


public class HorinSelect extends LinearLayout {
    ImageView next;
    ImageView prev;
    TextView text;
    public HorinSelect(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    public HorinSelect(Context context){
        super(context);
        init(context);
    }
    private void init(Context context){
        inflate(context, R.layout.number_selector,this);

        initComponents();

    }
    private void prev(){
        if(getText()==1){
//            do nothing
        }else {
            String newInt = Integer.toString(getText()-1);
            setText(newInt);
        }
    }
    private void next(){
        String newInt = Integer.toString(getText()+1);
        setText(newInt);

    }
    public String getCount(){
        return text.getText().toString();
    }
    private void initComponents(){
        next=findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        text = findViewById(R.id.text);
        setText("1");
        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    public void setText(CharSequence text) {
        this.text.setText(text);

    }
    public int getText(){

        return Integer.parseInt(this.text.getText().toString());
    }
}

