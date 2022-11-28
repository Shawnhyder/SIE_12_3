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

public class clubCreatePost extends AppCompatActivity implements View.OnClickListener{

    private EditText titleEditText, descriptionEditText;
    private Button uploadButton,cancelButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create_post);

        auth = FirebaseAuth.getInstance();

        titleEditText = (EditText) findViewById(R.id.title);
        titleEditText.setOnClickListener(this);

        descriptionEditText = (EditText) findViewById(R.id.description);
        descriptionEditText.setOnClickListener(this);

        uploadButton = (Button) findViewById(R.id.upload);
        uploadButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                startActivity(new Intent(this, clubhome.class));
                break;
            case R.id.upload:
                verifyUpload();
                break;

        }
    }

    private void verifyUpload() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            titleEditText.setError("Please enter a title!");
            titleEditText.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError("Please enter a description");
            descriptionEditText.requestFocus();
            return;
        }

        UploadData(title,description);



    }

    private void UploadData(String title, String description) {
        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filepath = "IEPosts/"+"IEpost_"+timeStamp;

        StorageReference refrence = FirebaseStorage.getInstance().getReference().child(filepath);
        FirebaseUser user = auth.getCurrentUser();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("uId",user.getUid());
        hashMap.put("uEmail",user.getEmail());
        hashMap.put("pId",timeStamp);
        hashMap.put("pTitle",title);
        hashMap.put("pDescription",description);
        hashMap.put("pTime",timeStamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("IEPosts");
        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(clubCreatePost.this,"post uploaded",Toast.LENGTH_LONG).show();
                        titleEditText.setText("");
                        descriptionEditText.setText("");

                        startActivity(new Intent(clubCreatePost.this,clubhome.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(clubCreatePost.this,"Post did not upload! Please try again",Toast.LENGTH_LONG).show();
                    }
                });

    }
}