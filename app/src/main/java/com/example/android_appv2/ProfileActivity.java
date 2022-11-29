package com.example.android_appv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button informationExchange,formClubButton;


    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        informationExchange = (Button) findViewById(R.id.informationExchange);
        informationExchange.setOnClickListener(this);

        formClubButton = (Button) findViewById(R.id.formClub);
        formClubButton.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_logout) {
            auth.signOut();
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        }

        if(item.getItemId() == R.id.viewProfile) {
            startActivity(new Intent(ProfileActivity.this,ViewProfile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.informationExchange:
                startActivity(new Intent(this,clubhome.class));
                break;
            case R.id.formClub:
                startActivity(new Intent(this,ActualClubHome.class));
        }
    }
}