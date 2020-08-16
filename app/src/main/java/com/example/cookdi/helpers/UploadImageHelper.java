//package com.example.cookdi.helpers;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.provider.MediaStore;
//
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.IOException;
//
//import static androidx.core.app.ActivityCompat.startActivityForResult;
//
//public class UploadImageHelper {
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageReference = storage.getReference();
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null )
//        {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//                imageView.setTag("UpdatedTag");
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    private void uploadImage() {
//        if(filePath != null)
//        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//            UUID = UUID.randomUUID();
//
//            final StorageReference ref = storageReference.child("images/"+ UUID.toString());
//            ref.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//
//                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    imgURL = String.valueOf(uri);
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                        }
//                    });
//
//        }
//
//    }
//
//    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//    void submitEvent(){
//        submit.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View view) {
//                if(TextHelper.isTextEmpty(editText.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(imageView.getTag() != "UpdatedTag"){
//                    Toast.makeText(getApplicationContext(), "Please enter a picture", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                uploadImage();
//                startActivity(new Intent(ReportActivity.this, ReportActivitySucceed.class));
//            }
//        });
//    }
//
//    //Permission memory
//    public  boolean isStoragePermissionGranted() {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
//                return true;
//            } else {
//
//                Log.v(TAG,"Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
//            return true;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//            //resume tasks needing this permission
//        }
//    }
//
//}