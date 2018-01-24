package arieluniversity.loginbank;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Color;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.SnapshotHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import BankClass.*;




public class Login extends AppCompatActivity {
    EditText userName,Password;
    Button logIn;
    TextView forgotPassword,newUser;
    FirebaseDatabase firebase;
    boolean flag;
    public static Clients c;
    public static boolean IsOnPauseOrOnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String error = getIntent().getStringExtra("errorUserNotExist");
        if (error!=null){
            Toast.makeText(Login.this,getIntent().getStringExtra("errorUserNotExist"),Toast.LENGTH_SHORT).show();
        }
        userName = (EditText) findViewById(R.id.user_name);
        Password = (EditText) findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.login_button);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        newUser= (TextView) findViewById(R.id.new_user);
        c=new Clients();
        IsOnPauseOrOnStop=false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        IsOnPauseOrOnStop=false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        IsOnPauseOrOnStop=false;
    }




//
//        DatabaseReference pass= f.child("password");
//        pass.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mypass =dataSnapshot.getValue().toString();
//                Password.setText( mypass);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    @Override
    protected void onPause(){
        super.onPause();
        IsOnPauseOrOnStop=true;
    }
    @Override
    protected void onStop(){
        super.onStop();
        IsOnPauseOrOnStop=true;
    }

    public void LoginClick(View view){
        CheckAndLogin();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(userName.getText().toString(),Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, " התחברות הצליחה", Toast.LENGTH_LONG).show();
                    firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
                    DatabaseReference ref = firebase.getReference();
                    ref=ref.getRoot();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
                    ref.child("Users").child(currentFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
//                            flag = false;
//                            for (DataSnapshot child : dataSnapshots) {
//                                Client c = child.getValue(Client.class);
//                                if (c.getEmail().equals(userName.getText().toString())  &&   c.getPassword().equals(Password.getText().toString()) ){
//                                    Login.c.addClient(c);
//                                    flag = true;
//                                    break;
//                                }
                             //   Toast.makeText(Login.this,child.getValue().toString(),Toast.LENGTH_LONG).show();
                            //}
                            flag = false;
                            if (dataSnapshot.exists()){
                                flag = true;
                                Client c = dataSnapshot.getValue(Client.class);
                                Login.c.addClient(c);
                            }
                            doFromLoginClick();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Login.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }

    private void CheckAndLogin() {
    }


    private void doFromLoginClick() {
        if (!Login.IsOnPauseOrOnStop) {
            if (flag) {
                Intent intent = new Intent(this, EditDetails.class);
                intent.putExtra("userName", userName.getText().toString());
                intent.putExtra("password", Password.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(Login.this, "משתמש לא קיים", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ForgotPasswordClick(View view) {



//        Intent i =new Intent(Intent.ACTION_SEND);
//        i.setData(Uri.parse("mailto:"));
//        String [] to = {"roeyvelner@gmail.com","roeyvelner@gmail.com"};
//        i.putExtra(Intent.EXTRA_EMAIL,to);
//        i.putExtra(Intent.EXTRA_SUBJECT,"hellllo");
//        i.putExtra(Intent.EXTRA_TEXT,"my name");
//        i.setType("message/rfc822");
//        Intent chooser = Intent.createChooser(i,"Send Email");
//        startActivity(chooser);
//





        forgotPassword.setTextColor(Color.BLACK);
        Intent intent = new Intent(this, forgotPassword.class);
        intent.putExtra("userName",userName.getText().toString());
        startActivity(intent);

    }
    public void NewUserClick(View view){
        newUser.setTextColor(Color.BLACK);
        Intent intent = new Intent(this, SignUser.class);
        startActivity(intent);
    }
    public void getToMapClick(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void getToaboutClick(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
