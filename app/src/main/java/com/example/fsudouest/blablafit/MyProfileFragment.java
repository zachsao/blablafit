package com.example.fsudouest.blablafit;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    private static final int RC_PHOTO_PICKER = 2;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePhotosStorageReference;
    FirebaseUser user;
    ImageView profile_pic;
    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_my_profile, container, false);

        user =FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mProfilePhotosStorageReference = mFirebaseStorage.getReference().child("profile_pictures");

        TextView pseudo = rootview.findViewById(R.id.pseudo);
        TextView nom = rootview.findViewById(R.id.nom);
        TextView email = rootview.findViewById(R.id.email);
        profile_pic = rootview.findViewById(R.id.profile_pic_image_view);

        pseudo.setText(user.getDisplayName());
        nom.setText("Nom: "+ user.getDisplayName());
        //prenom.setText("Prénom: "+preferences.getString("prénom",null));
        email.setText("Email: "+user.getEmail());
        if(user.getPhotoUrl()!=null){
            Glide.with(getContext())
                    .load(user.getPhotoUrl().toString())
                    .into(profile_pic);
        }

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPhotoUpdateDialog();
            }
        });

        Button deconnexion = rootview.findViewById(R.id.deco_button);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        return rootview;
    }


    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    private void createPhotoUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.update_profile_dialog_title));
        builder.setMessage(getString(R.string.update_photo_dialog_message));
        // Add the button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                chooseImageFromGallery();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            profile_pic.setImageURI(selectedImageUri);
            // Get a reference to store file at profile_pictures/<FILENAME>
            final StorageReference photoRef = mProfilePhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            photoRef.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        UserProfileChangeRequest photoUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();
                        user.updateProfile(photoUpdate);
                    } else {
                        Toast.makeText(getActivity(),"Upload Failed - please try again",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(getActivity());
    }

}
