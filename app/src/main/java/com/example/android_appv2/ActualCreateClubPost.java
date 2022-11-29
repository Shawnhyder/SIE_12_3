package com.example.android_appv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ActualCreateClubPost extends AppCompatActivity implements View.OnClickListener {


    Button createButton, cancelButton;
    EditText groupNameEditText, descriptionEditText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_create_club_post);

        auth = FirebaseAuth.getInstance();

        createButton = (Button) findViewById(R.id.create);
        createButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);

        groupNameEditText = (EditText) findViewById(R.id.clubName);
        groupNameEditText.setOnClickListener(this);

        descriptionEditText = (EditText) findViewById(R.id.description);
        descriptionEditText.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                startActivity(new Intent(this, ActualClubHome.class));
                break;
            case R.id.create:
                verifyUpload();

        }
    }

    private void verifyUpload() {

        String clubName = groupNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (clubName.isEmpty()) {
            groupNameEditText.setError("Please enter a club name!");
            groupNameEditText.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError("Please enter a description");
            descriptionEditText.requestFocus();
            return;
        }

        UploadData(clubName,description);

    }

    private void UploadData(String clubName, String description) {

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filepath = "ClubPosts/"+"Clubpost_"+timeStamp;

        StorageReference refrence = FirebaseStorage.getInstance().getReference().child(filepath);
        FirebaseUser user = auth.getCurrentUser();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("uId",user.getUid());
        hashMap.put("uEmail",user.getEmail());
        hashMap.put("pId",timeStamp);
        hashMap.put("pClubTitle",clubName);
        hashMap.put("pDescription",description);
        hashMap.put("pTime",timeStamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ClubPosts");
        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActualCreateClubPost.this,"post uploaded",Toast.LENGTH_LONG).show();
                        groupNameEditText.setText("");
                        descriptionEditText.setText("");

                        startActivity(new Intent(ActualCreateClubPost.this,ActualClubHome.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualCreateClubPost.this,"Post did not upload! Please try again",Toast.LENGTH_LONG).show();
                    }
                });
    }
}