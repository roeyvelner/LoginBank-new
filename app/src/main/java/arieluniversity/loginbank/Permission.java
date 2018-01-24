package arieluniversity.loginbank;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import BankClass.Client;
import BankClass.Clients;

public class Permission extends AppCompatActivity {
    TextView user_id_tag ,verificationQ_tag,city_tag,email_tag,adress_tag,last_name_tag,first_name_tag;
    ImageView imgView ;
    Button buttonLoadPicture,buttonTakePicture,btnSaveChanges;
    EditText adress_real,first_name_real,last_name_real,city_real,verificationQ_tagHint,verificationQ_real,email_real;
    ListView listViewUSER ,listViewPRIMISSION;
    FirebaseDatabase firebase;
    ArrayList items_Users_list,Uid;
    ArrayAdapter<String> itemsUsers;
    Clients C;
    Client client;
    int Index;
    RadioButton user,manager;
    private  static final  int RESULT_LOAD_IMAGE = 1,CAMERA_PIC_REQUEST=2;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        listViewUSER =(ListView) findViewById(R.id.listview_users_name);
        items_Users_list = new ArrayList();
        Uid = new ArrayList();
        C = new Clients();
        user_id_tag = (TextView ) findViewById(R.id.user_id_tag);
        verificationQ_tag = (TextView ) findViewById(R.id.verificationQ_tag);
        city_tag = (TextView ) findViewById(R.id.city_tag);
        email_tag = (TextView ) findViewById(R.id.email_tag);
        adress_tag = (TextView ) findViewById(R.id.adress_tag);
        last_name_tag = (TextView ) findViewById(R.id.last_name_tag );
        first_name_tag = (TextView ) findViewById(R.id.first_name_tag);

        imgView =(ImageView) findViewById(R.id.imgView);

        buttonLoadPicture = (Button) findViewById(R.id.buttonLoadPicture);
        btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        buttonTakePicture = (Button) findViewById(R.id.buttonTakePicture);


        adress_real = (EditText) findViewById(R.id.adress_real);
        city_real = (EditText) findViewById(R.id.city_real);
        verificationQ_tagHint = (EditText) findViewById(R.id.verificationQ_tagHint);
        verificationQ_real = (EditText) findViewById(R.id.verificationQ_real);
        last_name_real = (EditText) findViewById(R.id.last_name_real);
        first_name_real = (EditText) findViewById(R.id.first_name_real);
        email_real = (EditText) findViewById(R.id.email_real);

        user = (RadioButton) findViewById(R.id.radioUser);
        manager = (RadioButton) findViewById(R.id.radioManger);




        //set invisible until choose user
        user_id_tag.setVisibility(View.GONE);
        verificationQ_tag.setVisibility(View.GONE);
        city_tag.setVisibility(View.GONE);
        email_tag.setVisibility(View.GONE);
        adress_tag.setVisibility(View.GONE);
        last_name_tag.setVisibility(View.GONE);
        first_name_tag.setVisibility(View.GONE);

        imgView.setVisibility(View.GONE);

        buttonLoadPicture.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.GONE);
        buttonTakePicture.setVisibility(View.GONE);



        adress_real.setVisibility(View.GONE);
        city_real.setVisibility(View.GONE);
        verificationQ_tagHint.setVisibility(View.GONE);
        verificationQ_real.setVisibility(View.GONE);
        last_name_real.setVisibility(View.GONE);
        first_name_real.setVisibility(View.GONE);
        email_real.setVisibility(View.GONE);
        user.setVisibility(View.GONE);
        manager.setVisibility(View.GONE);

        addUsers();




    }
    public void addUsers(){
        firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
        DatabaseReference ref = firebase.getReference();
        ref=ref.getRoot();
        //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot child : dataSnapshots) {
                    Client c = child.getValue(Client.class);
                    Uid.add(child.getKey());
                    C.addClient(c);
                    items_Users_list.add(c.getFirstName() + " " + c.getLastName());
                }
                itemsUsers = new ArrayAdapter<String>(Permission.this, android.R.layout.simple_list_item_1, items_Users_list);

                listViewUSER.setAdapter(itemsUsers);
                listViewUSER.setSelection(5);
                listViewUSER.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Index =position;
                        fillDetails();
                    }
                });



//
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(Permission.this,listViewUSER.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//                    }
//                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Permission.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void fillDetails(){
        client = new Client(C.GetClientByIndex(Index));
        user_id_tag.setVisibility(View.VISIBLE);
        verificationQ_tag.setVisibility(View.VISIBLE);
        city_tag.setVisibility(View.VISIBLE);
        email_tag.setVisibility(View.VISIBLE);
        adress_tag.setVisibility(View.VISIBLE);
        last_name_tag.setVisibility(View.VISIBLE);
        first_name_tag.setVisibility(View.VISIBLE);

        imgView.setVisibility(View.VISIBLE);

        buttonLoadPicture.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.VISIBLE);
        buttonTakePicture.setVisibility(View.VISIBLE);


        adress_real.setVisibility(View.VISIBLE);
        city_real.setVisibility(View.VISIBLE);
        verificationQ_tagHint.setVisibility(View.VISIBLE);
        verificationQ_real.setVisibility(View.VISIBLE);
        last_name_real.setVisibility(View.VISIBLE);
        first_name_real.setVisibility(View.VISIBLE);
        email_real.setVisibility(View.VISIBLE);
        user.setVisibility(View.VISIBLE);
        manager.setVisibility(View.VISIBLE);
        setText();

    }


    public void LoadPictureClick(View view) {
        try {
            //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Files.getContentUri("external"));
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        catch (Exception e){
            String err=e.getMessage();
            Toast.makeText(this,err ,Toast.LENGTH_SHORT).show();
        }
    }
    public void TakePictureClick(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    public void radioManagerClick(View view){
        user.setChecked(false);
    }
    public void radioUserClick(View view){
        manager.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&  data!= null ) {
            switch (requestCode ){
                case  RESULT_LOAD_IMAGE: case CAMERA_PIC_REQUEST:
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        final String picturePath = cursor.getString(columnIndex);
                        cursor.close();


                        //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                        StorageReference riversRef = mStorageRef.child("images/" + Uid.get(Index) + ".jpg");

                        riversRef.putFile(selectedImage)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if (picturePath.substring(picturePath.length() - 3).compareTo("png") == 0 || picturePath.substring(picturePath.length() - 3).compareTo("jpg") == 0)
                                            Toast.makeText(Permission.this, "הוספת התמונה הצליחה!", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(Permission.this, "הוספת התמונה הצליחה! למרות שסיומת הקובץ אינה jpg/png", Toast.LENGTH_LONG).show();
                                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        //Toast.makeText(EditDetails.this,downloadUrl.toString(),Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                        Toast.makeText(Permission.this, "there was an error - " + exception.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });


                        imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        client.setpicturePath(picturePath);
                        Login.c.setClient(client);
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference ref = firebaseDatabase.getReference();
                        ref.child("Users").child(Uid.get(Index).toString()).push();
                        ref.child("Users").child(Uid.get(Index).toString()).setValue(client);
                        break;
                    }
                    catch (Exception e){
                        Toast.makeText(Permission.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

            }

        }



    }



    private void setText() {
        verificationQ_tagHint.setText(client.getVerificationQuestion());


        adress_real.setText(client.getStreetAdress());
        city_real.setText(client.getCityAdress());
        email_real.setText(client.getEmail());
        verificationQ_real.setText(client.getVerificationAnswer());
        verificationQ_tagHint.setText(client.getVerificationQuestion());
        first_name_real.setText(client.getFirstName());
        last_name_real.setText(client.getLastName());
        switch (client.getJob()){
            case "User":
                user.setChecked(true);
                manager.setChecked(false);
                break;
            case "Administrator":
                manager.setChecked(true);
                user.setChecked(false);
                break;
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        StorageReference riversRef = mStorageRef.child("images/" + Uid.get(Index) + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            imgView.setImageBitmap(BitmapFactory.decodeFile(localFile.getPath()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    if(exception.getMessage().compareTo("Object does not exist at location.")==0)
                        Toast.makeText(Permission.this,"There was no image",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Permission.this,"There was a problem while loading the image",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(Permission.this,"There was a problem while loading the image",Toast.LENGTH_LONG).show();
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


    }
    public void btnSaveChanges_onClick(View view){
        Client c_new = new Client(client);
        c_new.setFirstName(first_name_real.getText().toString());
        c_new.setLastName(last_name_real.getText().toString());
        c_new.setCityAdress(city_real.getText().toString());
        c_new.setStreetAdress(adress_real.getText().toString());
        c_new.setVerificationQuestion(verificationQ_tagHint.getText().toString());
        c_new.setVerificationAnswer(verificationQ_real.getText().toString());
        if (user.isChecked()) c_new.setJob("User");
        if (manager.isChecked()) c_new.setJob("Administrator");
        C.setClient(Index,c_new);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference();
        ref.child("Users").child(Uid.get(Index).toString()).push();
        ref.child("Users").child(Uid.get(Index).toString()).setValue(c_new);
        hideAll();
    }
    public void hideAll(){
        //set invisible until choose user
        user_id_tag.setVisibility(View.GONE);
        verificationQ_tag.setVisibility(View.GONE);
        city_tag.setVisibility(View.GONE);
        email_tag.setVisibility(View.GONE);
        adress_tag.setVisibility(View.GONE);
        last_name_tag.setVisibility(View.GONE);
        first_name_tag.setVisibility(View.GONE);

        imgView.setVisibility(View.GONE);

        buttonLoadPicture.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.GONE);
        buttonTakePicture.setVisibility(View.GONE);


        adress_real.setVisibility(View.GONE);
        city_real.setVisibility(View.GONE);
        verificationQ_tagHint.setVisibility(View.GONE);
        verificationQ_real.setVisibility(View.GONE);
        last_name_real.setVisibility(View.GONE);
        first_name_real.setVisibility(View.GONE);
        email_real.setVisibility(View.GONE);
        user.setVisibility(View.GONE);
        manager.setVisibility(View.GONE);
    }
}

