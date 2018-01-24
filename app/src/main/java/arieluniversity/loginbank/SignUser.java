package arieluniversity.loginbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import BankClass.Client;

public class SignUser extends AppCompatActivity {
    private TextView hello_user, user_name_tag , Pass_tag , adress_tag ,city_tag ,email_tag , verificationQ_tag , first_name_tag , last_name_tag;
    private EditText user_name_real,  adress_real, city_real , email_real , verificationQ_real , Pass_real, verificationQ_tagHint ,first_name_real , last_name_real;
    private Button editDetails_button;
    private String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_user);
        picturePath="";
        user_name_tag = (TextView) findViewById(R.id.user_name_tag);
        Pass_tag = (TextView) findViewById(R.id.Pass_tag);
        adress_tag = (TextView) findViewById(R.id.adress_tag);
        city_tag = (TextView) findViewById(R.id.city_tag);
        email_tag = (TextView) findViewById(R.id.email_tag);
        verificationQ_tag = (TextView) findViewById(R.id.verificationQ_tag);
        first_name_tag = (TextView) findViewById(R.id.first_name_tag);
        last_name_tag = (TextView) findViewById(R.id.last_name_tag);

        first_name_real = (EditText) findViewById(R.id.first_name_real);
        last_name_real = (EditText) findViewById(R.id.last_name_real);
        verificationQ_tagHint = (EditText) findViewById(R.id.verificationQ_tagHint);
        user_name_real = (EditText) findViewById(R.id.user_name_real);
        Pass_real =  (EditText) findViewById(R.id.Pass_real);
        adress_real = (EditText) findViewById(R.id.adress_real);
        city_real = (EditText) findViewById(R.id.city_real);
        email_real = (EditText) findViewById(R.id.email_real);
        verificationQ_real = (EditText) findViewById(R.id.verificationQ_real);


        editDetails_button = (Button) findViewById(R.id.editDetails_button);
    }

    public void SaveDetailsClick(View view){
//        if (true){
//            Client temp = new Client(first_name_real.getText().toString(), last_name_real.getText().toString(), user_name_real.getText().toString(), Pass_real.getText().toString(), city_real.getText().toString(), adress_real.getText().toString(), email_real.getText().toString(), verificationQ_real.getText().toString(), verificationQ_tagHint.getText().toString(),picturePath);
//            Login.c.addClient(temp);
//            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            DatabaseReference ref = firebaseDatabase.getReference();
//            ref.child("Users").child(temp.getUserName()).push();
//            ref.child("Users").child(temp.getUserName()).setValue(temp);
//
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//        }
//        else


                if (first_name_real.getText().toString().compareTo("") == 0) {
                    Toast.makeText(SignUser.this, "נא להכניס שם פרטי", Toast.LENGTH_SHORT).show();
                } else {
                    if (last_name_real.getText().toString().compareTo("") == 0) {
                        Toast.makeText(SignUser.this, "נא להכניס שם משפחה", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Pass_real.getText().toString().compareTo("") == 0) {
                            Toast.makeText(SignUser.this, "נא להכניס סיסמא", Toast.LENGTH_SHORT).show();
                        } else {
                            if (adress_real.getText().toString().compareTo("") == 0) {
                                Toast.makeText(SignUser.this, "נא להכניס שם רחוב", Toast.LENGTH_SHORT).show();
                            } else {
                                if (city_real.getText().toString().compareTo("") == 0) {
                                    Toast.makeText(SignUser.this, "נא להכניס שם עיר", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (email_real.getText().toString().compareTo("") == 0) {
                                        Toast.makeText(SignUser.this, "נא להכניס אימייל", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (verificationQ_tagHint.getText().toString().compareTo("") == 0) {
                                            Toast.makeText(SignUser.this, "נא להכניס שאלת הזדהות", Toast.LENGTH_SHORT).show();
                                        } else {

                                            if (verificationQ_real.getText().toString().compareTo("") == 0) {
                                                Toast.makeText(SignUser.this, "נא להכניס תשובה לשאלת הזדהות", Toast.LENGTH_SHORT).show();
                                            } else {

                                                final Client temp = new Client(first_name_real.getText().toString(), last_name_real.getText().toString(), city_real.getText().toString(), adress_real.getText().toString(), email_real.getText().toString(), verificationQ_real.getText().toString(), verificationQ_tagHint.getText().toString(),picturePath,"User");
                                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                mAuth.createUserWithEmailAndPassword(email_real.getText().toString() ,Pass_real.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                                            addClientToFireBase(currentFirebaseUser.getUid(),temp);
                                                        }
                                                    }
                                                });

                                                Login.c.addClient(temp);
                                                Intent intent = new Intent(this, Login.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


    private void addClientToFireBase(String key,Client temp) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference();
        ref.child("Users").child(key).push();
        ref.child("Users").child(key).setValue(temp);
    }

    public void BackBtnClick(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }
}
