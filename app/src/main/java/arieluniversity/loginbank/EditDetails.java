package arieluniversity.loginbank;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import BankClass.Client;

public class EditDetails extends AppCompatActivity {
    private TextView hello_user,user_id_tag, user_name_tag , Pass_tag , adress_tag ,city_tag ,email_tag , verificationQ_tag ;
    private EditText   adress_real, city_real , email_real , verificationQ_real ,  verificationQ_tagHint;
    private Button editDetails_button ,Edit_Users_btn ,buttonTakePicture, adminMenu, OtherCoin ;
    Client client;
    boolean inEdit;
    Drawable saveNature;
    private  static final  int RESULT_LOAD_IMAGE = 1,CAMERA_PIC_REQUEST=2;
    private String picturePath;
    ImageView imageView ;
    String myuser="@'",mypass="2";
    String err;
    Firebase firebase;
    String firstName, lastName,userName,Password;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        File file1 =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Bank");
        file1.mkdir();
        userName = getIntent().getStringExtra("userName");
        Password = getIntent().getStringExtra("password");
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        try {
//
//            //firebase = new Firebase("https://bank-login.firebaseio.com/");
//        }
//        catch (Exception e){
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG ).show();
//        }
//
//
//        DatabaseReference f= database.getRoot();
//        DatabaseReference namee= f.child("name");
//        namee.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                myuser =dataSnapshot.getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                myuser ="error";
//            }
//        });
//        userName = myuser;
//        DatabaseReference pass= f.child("password");
//        pass.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                mypass =dataSnapshot.getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                mypass="error";
//            }
//        });
//        Password = mypass;

        if (Login.c.getClient(userName,Password) == null) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("errorUserNotExist","משתמש לא קיים--");
            startActivity(intent);
            this.finish();
        }
        else {
            client = new Client(Login.c.getClient(userName, Password));

            inEdit = false;
            hello_user = (TextView) findViewById(R.id.hello_user);
            user_name_tag = (TextView) findViewById(R.id.user_name_tag);
            Pass_tag = (TextView) findViewById(R.id.Pass_tag);
            adress_tag = (TextView) findViewById(R.id.adress_tag);
            city_tag = (TextView) findViewById(R.id.city_tag);
            email_tag = (TextView) findViewById(R.id.email_tag);
            verificationQ_tag = (TextView) findViewById(R.id.verificationQ_tag);
            user_id_tag = (TextView) findViewById(R.id.user_id);


            verificationQ_tagHint = (EditText) findViewById(R.id.verificationQ_tagHint);

            adress_real = (EditText) findViewById(R.id.adress_real);
            city_real = (EditText) findViewById(R.id.city_real);
            email_real = (EditText) findViewById(R.id.email_real);
            verificationQ_real = (EditText) findViewById(R.id.verificationQ_real);


            editDetails_button = (Button) findViewById(R.id.editDetails_button);
            buttonTakePicture =  (Button) findViewById(R.id.buttonTakePicture);
            Edit_Users_btn =  (Button) findViewById(R.id.Edit_Users);
            OtherCoin =  (Button) findViewById(R.id.OtherCoin);
            adminMenu =  (Button) findViewById(R.id.adminMenu);


            imageView = (ImageView) findViewById(R.id.imgView);


            setText();
        }
    }

    private void setText() {
        hello_user.setText(" שלום "+client.getFirstName().toString() + " " + client.getLastName() + " ! ");
        verificationQ_tagHint.setText(client.getVerificationQuestion());


        adress_real.setText(client.getStreetAdress());
        city_real.setText(client.getCityAdress());
        email_real.setText(client.getEmail());
        verificationQ_real.setText(client.getVerificationAnswer());
        verificationQ_tagHint.setText(client.getVerificationQuestion());
        picturePath = client.getpicturePath();
        switch (client.getJob()){
            case "User":
                user_id_tag.setText("משתמש רגיל");
                adminMenu.setVisibility(View.VISIBLE);
                adminMenu.setBackground(Edit_Users_btn.getBackground());
                adminMenu.setText("הראה תיק משכנתאות");

                break;
            case "Administrator":
                user_id_tag.setText("מנהל");
                //Edit_Users_btn.setVisibility(View.VISIBLE);
                adminMenu.setVisibility(View.VISIBLE);
                break;
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        StorageReference riversRef = mStorageRef.child("images/" + currentFirebaseUser.getUid() + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getPath()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    if(exception.getMessage().compareTo("Object does not exist at location.")==0)
                        Toast.makeText(EditDetails.this,"There was no image",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(EditDetails.this,"There was a problem while loading the image",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(EditDetails.this,"There was a problem while loading the image",Toast.LENGTH_LONG).show();
        }
//        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
//        Toast.makeText(this,riversRef.getPath(),Toast.LENGTH_LONG).show();
//        Glide.with(this).using().load(riversRef).into(imageView);
//        try {
//            File localFile = File.createTempFile("images", "jpg");
//
//            riversRef.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            // Successfully downloaded data to local file
//                            // ...
//                            imageView.setImageBitmap(BitmapFactory.decodeFile(taskSnapshot.getStorage().getPath()));
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(Exception exception) {
//                    // Handle failed download
//                    // ...
//                }
//            });
////        }/*
////        catch (Exception e){
////
////        }*/

        if (picturePath.compareTo("")!=0){

        }
    }
    public void EditDetailsClick(View view){
        if (!inEdit){
            inEdit = true;
            editDetails_button.setText("שמור שינויים");
            verificationQ_real.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);




            adress_real.setEnabled(true);
            city_real.setEnabled(true);

            verificationQ_tagHint.setEnabled(true);

            verificationQ_real.setEnabled(true);


        }
        else{
            inEdit=false;

            client.setVerificationAnswer(verificationQ_real.getText().toString());
            client.setStreetAdress(adress_real.getText().toString());
            client.setCityAdress(city_real.getText().toString());
            client.setVerificationQuestion(verificationQ_tagHint.getText().toString());
            client.setpicturePath(picturePath);
            Login.c.setClient(client);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference ref = firebaseDatabase.getReference();

            editDetails_button.setText("ערוך פרטים");
            verificationQ_real.setInputType(129);


            adress_real.setEnabled(false);

            city_real.setEnabled(false);


            verificationQ_tagHint.setEnabled(false);

            verificationQ_real.setEnabled(false);
        }
    }
    public void getToMapClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void BackBtnClick(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }


    public void LoadPictureClick(View view) {
        try {
            //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Files.getContentUri("external"));
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        catch (Exception e){
            err=e.getMessage();
            Toast.makeText(this,err ,Toast.LENGTH_SHORT).show();
        }
    }
    public void TakePictureClick(View view){



        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&  data!= null ) {
            switch (requestCode ){
                case  RESULT_LOAD_IMAGE:
                    Uri pickedImage = data.getData();
                    // Let's read picked image path using content resolver
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    cursor.close();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    StorageReference riversRef = mStorageRef.child("images/" + currentFirebaseUser.getUid() + ".jpg");
                    riversRef.putFile(pickedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          //          if (picturePath.substring(picturePath.length()-3).compareTo("png")==0   ||  picturePath.substring(picturePath.length()-3).compareTo("jpg")==0)
                                        Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה!",Toast.LENGTH_LONG).show();
                            //        else
                              //          Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה! למרות שסיומת הקובץ אינה jpg/png",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception exception) {
                                    Toast.makeText(EditDetails.this,"there was an error - " + exception.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                    break ;
                case CAMERA_PIC_REQUEST:
                    try {
                        String[] pro = new String[]{
                                MediaStore.Images.ImageColumns._ID,
                                MediaStore.Images.ImageColumns.DATA,
                                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.ImageColumns.DATE_TAKEN,
                                MediaStore.Images.ImageColumns.MIME_TYPE
                        };
                        final Cursor c = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pro, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
                        if (c.moveToFirst()) {
                            String location = c.getString(1);
                            File ff = new File(location);
                            if (ff.exists()) {
                                Bitmap b = BitmapFactory.decodeFile(location);
                                imageView.setImageBitmap(b);

                            }
                            Uri selectedImage = Uri.fromFile(ff);
                            FirebaseUser currentFirebaseUser2 = FirebaseAuth.getInstance().getCurrentUser();
                            riversRef = mStorageRef.child("images/" + currentFirebaseUser2.getUid() + ".jpg");
                            riversRef.putFile(selectedImage)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //if (picturePath.substring(picturePath.length()-3).compareTo("png")==0   ||  picturePath.substring(picturePath.length()-3).compareTo("jpg")==0)
                                            Toast.makeText(EditDetails.this, "הוספת התמונה הצליחה!", Toast.LENGTH_LONG).show();
                                            //else
                                            //Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה! למרות שסיומת הקובץ אינה jpg/png",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception exception) {
                                            Toast.makeText(EditDetails.this, "there was an error - " + exception.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                            break;

                        }
                    }
                    catch (Exception e){
                        Toast.makeText(EditDetails.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }


//
//
//                    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//                    bmpFactoryOptions.inJustDecodeBounds = false;
//
//                    //imageFilePath image path which you pass with intent
//                    Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg", bmpFactoryOptions);
//
//                    // Display it
//                    imageView.setImageBitmap(bmp);
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg");
//
////
////                    Save(" before 1 ".split(System.getProperty("line.separator")));
////                    Bitmap photo = (Bitmap) data.getExtras().get("data");
////                    Save(" before 2 ".split(System.getProperty("line.separator")));
////                    imageView.setImageBitmap(photo);
////                    Save(" before 3 ".split(System.getProperty("line.separator")));
////                    Uri selectedImage = data.getData();
////                    Save(" before 4 ".split(System.getProperty("line.separator")));
//                    FirebaseUser currentFirebaseUser2 = FirebaseAuth.getInstance().getCurrentUser() ;
//                    Save(" before 5 ".split(System.getProperty("line.separator")));
//                    riversRef = mStorageRef.child("images/" + currentFirebaseUser2.getUid() + ".jpg");
//                    Save(" before 6 ".split(System.getProperty("line.separator")));
//                    riversRef.putFile(selectedImage)
//                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Save(" before 7 ".split(System.getProperty("line.separator")));
//                                    if (picturePath.substring(picturePath.length()-3).compareTo("png")==0   ||  picturePath.substring(picturePath.length()-3).compareTo("jpg")==0)
//                                        Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה!",Toast.LENGTH_LONG).show();
//                                    else
//                                        Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה! למרות שסיומת הקובץ אינה jpg/png",Toast.LENGTH_LONG).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(Exception exception) {
//                                    Toast.makeText(EditDetails.this,"there was an error - " + exception.getMessage(),Toast.LENGTH_LONG).show();
//                                }
//                            });
//                    Save(" before 8 ".split(System.getProperty("line.separator")));
//                    break ;
            }

        }
        else{
            if (requestCode  == CAMERA_PIC_REQUEST && data !=null){
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = false;
                Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg", bmpFactoryOptions);

                // Display it
                imageView.setImageBitmap(bmp);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg");
                Uri selectedImage = Uri.fromFile(file);
//
//                    Save(" before 1 ".split(System.getProperty("line.separator")));
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    Save(" before 2 ".split(System.getProperty("line.separator")));
//                    imageView.setImageBitmap(photo);
//                    Save(" before 3 ".split(System.getProperty("line.separator")));
//                    Uri selectedImage = data.getData();
//                    Save(" before 4 ".split(System.getProperty("line.separator")));
                FirebaseUser currentFirebaseUser2 = FirebaseAuth.getInstance().getCurrentUser() ;
                StorageReference riversRef = mStorageRef.child("images/" + currentFirebaseUser2.getUid() + ".jpg");
                riversRef.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if (picturePath.substring(picturePath.length()-3).compareTo("png")==0   ||  picturePath.substring(picturePath.length()-3).compareTo("jpg")==0)
                                    Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה!",Toast.LENGTH_LONG).show();
                                //else
                                  //  Toast.makeText(EditDetails.this,"הוספת התמונה הצליחה! למרות שסיומת הקובץ אינה jpg/png",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
                                Toast.makeText(EditDetails.this,"there was an error - " + exception.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }



    }
    public void getToAdminClick(View view){
        Intent intent = new Intent(this, MangerMain.class);
        intent.putExtra("user",userName);
        intent.putExtra("password",Password);
        if (adminMenu.getText().toString().compareTo("הראה תיק משכנתאות")==0)
            intent.putExtra("admin",false);
        else
            intent.putExtra("admin",true);
        startActivity(intent);
    }

    public void Edit_Users_OnClick(View view){
        Intent intent = new Intent(this, Permission.class);
        startActivity(intent);
    }
    public void GotoCoins(View view){
        Intent intent = new Intent(this, Coin.class);
        intent.putExtra("admin",false);
        startActivity(intent);
    }




    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void Save(String[] data) {
        File file = new  File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Bank" + "/Log_pic.txt");
        String temp2[];
        if (file.exists()) {
            String temp[] = Load(file);
            temp2 = new String[temp.length + data.length];
            int j = 0;
            int k = 0;
            for (int i = 0; j < temp.length || k < data.length; i++) {
                if (j < temp.length) {
                    temp2[i] = temp[j];
                    j++;
                } else {
                    temp2[i] = data[k];
                    k++;
                }
            }
        }
        else
            temp2 = data;
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try{
            try{
                for (int i = 0; i<temp2.length; i++){
                    fos.write(temp2[i].getBytes());
                    if (i < temp2.length-1) {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally{
            try{
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    public static String[] Load(File file) {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }

}
