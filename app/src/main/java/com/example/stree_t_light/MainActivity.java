package com.example.stree_t_light;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public void help_Function(View view)
    {
        Toast.makeText(getApplicationContext(),"Gand Mara Help Nahi Karunga!",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,MapsActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
