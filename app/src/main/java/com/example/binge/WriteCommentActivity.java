package com.example.binge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.binge.Models.CommentModel;
import com.example.binge.Models.MovieModel;
import com.example.binge.databinding.ActivityWriteCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class WriteCommentActivity extends AppCompatActivity {

    MovieModel movieModel;
    ActivityWriteCommentBinding binding;
    Uri uriTemp;
    ActivityResultLauncher<String> activityResultLauncherImage;
    ProgressDialog progressDialog;

    //Firebase
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        getSupportActionBar().hide();

        //To access the intent value provided by MovieDetails activity
        movieModel = getIntent().getParcelableExtra("movie");

        binding = ActivityWriteCommentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(WriteCommentActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Posting Comment");
        progressDialog.setMessage("Please wait a moment...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //////////////////////////////////////////////
        //  Cancel message and return to MovieDetails
        //////////////////////////////////////////////
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WriteCommentActivity.this,MovieDetails.class);
                intent.putExtra("movie", movieModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Set Hint of Edit text
        binding.commentText.setHint("Add a comment about "+movieModel.getTitle());

        /////////////////////////////
        //  Enable and disable Post btn when we type
        /////////////////////////////
        binding.commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!binding.commentText.getText().toString().isEmpty())
                {
                    binding.postBtn.setEnabled(true);
                    binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(WriteCommentActivity.this,R.drawable.follow_btn_bg));
                    binding.postBtn.setTextColor(view.getContext().getResources().getColor(R.color.white));
                }
                else
                {
                    binding.postBtn.setEnabled(false);
                    binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(WriteCommentActivity.this,R.drawable.following_btn));
                    binding.postBtn.setTextColor(view.getContext().getResources().getColor(R.color.black_overlay));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //////////////////////////////////////////
        //  Open Add image Intent when add image icon clicked
        /////////////////////////////////////////
        binding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncherImage.launch("image/*");
            }
        });
        activityResultLauncherImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                uriTemp = uri;
                binding.postBtn.setEnabled(true);
                binding.postBtn.setBackgroundDrawable(ContextCompat.getDrawable(WriteCommentActivity.this,R.drawable.follow_btn_bg));
                binding.postBtn.setTextColor(view.getContext().getResources().getColor(R.color.white));
                binding.commentImage.setVisibility(View.VISIBLE);
                binding.commentImage.setImageURI(uri);
            }
        });


        ////////////////////////////////////
        ////    Post Button is Clicked
        ////////////////////////////////////
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//        CustomeLoadDialog customeLoadDialog = new CustomeLoadDialog(WriteCommentActivity.this);
//                customeLoadDialog.startCustomeLoadDialog();

//            progressDialog.show();
                binding.progressBar2.setVisibility(View.VISIBLE);

                if(uriTemp==null)
                {
//                    Toast.makeText(WriteCommentActivity.this, "onnum illa vrooo", Toast.LENGTH_SHORT).show();
                    CommentModel commentModel = new CommentModel();
                    commentModel.setCommentMsg(binding.commentText.getText().toString());
                    commentModel.setCommentBy(FirebaseAuth.getInstance().getUid());
                    commentModel.setCommentAt(new Date().getTime());
                    commentModel.setMovieId(Integer.toString(movieModel.getMovie_id()));
                    String tempKey = FirebaseDatabase.getInstance().getReference().child("Comments")
                            .child(Integer.toString(movieModel.getMovie_id()))
                            .push().getKey();
                    commentModel.setCommentId(tempKey);

                    FirebaseDatabase.getInstance().getReference().child("Comments")
                            .child(Integer.toString(movieModel.getMovie_id()))
                            .child(tempKey).setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.progressBar2.setVisibility(View.INVISIBLE);
                            Toast.makeText(WriteCommentActivity.this, "Your comment is posted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WriteCommentActivity.this,MovieDetails.class);
                            intent.putExtra("movie", movieModel);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                }
                else
                {
                    String imgKey;
                    imgKey =  FirebaseDatabase.getInstance().getReference().child("Comments")
                                .child(Integer.toString(movieModel.getMovie_id()))
                                .push().getKey();
                    final StorageReference reference = storage.getReference().child("comment_photo")
                                .child(imgKey);

                        reference.putFile(uriTemp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        CommentModel commentModel = new CommentModel();
                                        commentModel.setCommentMsg(binding.commentText.getText().toString());
                                        commentModel.setCommentBy(FirebaseAuth.getInstance().getUid());
                                        commentModel.setCommentImg(uri.toString());
                                        commentModel.setCommentAt(new Date().getTime());
                                        commentModel.setCommentId(imgKey);
                                        commentModel.setMovieId(Integer.toString(movieModel.getMovie_id()));


                                        FirebaseDatabase.getInstance().getReference().child("Comments")
                                                .child(Integer.toString(movieModel.getMovie_id()))
                                                .child(imgKey).setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                binding.progressBar2.setVisibility(View.INVISIBLE);
                                                Toast.makeText(WriteCommentActivity.this, "Your comment is posted", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(WriteCommentActivity.this,MovieDetails.class);
                                                intent.putExtra("movie", movieModel);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                }
            }
        });
    }
}