package arieluniversity.loginbank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import BankClass.Client;

public class forgotPassword extends AppCompatActivity {

    private EditText ValAns,user_name_real;
    private TextView ValQ,thePass;
    private Button forgot_button , Verification_button ,Back_button;
    private Client temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ValAns = (EditText ) findViewById(R.id.ValAns);
        user_name_real = (EditText ) findViewById(R.id.user_name_real);

        thePass = (TextView) findViewById(R.id.thePass);
        ValQ = (TextView) findViewById(R.id.ValQ);


        forgot_button = (Button) findViewById(R.id.forgot_button);
        Verification_button = (Button) findViewById(R.id.Verification_button);
        Verification_button.setVisibility(View.INVISIBLE);
        Back_button = (Button) findViewById(R.id.Back_button );

    }

    public void forgotBtnClick(View view){
        temp =Login.c.getClient(user_name_real.getText().toString());
        if (temp!= null){
            ValQ.setVisibility(View.VISIBLE);
            ValAns.setVisibility(View.VISIBLE);
            ValQ.setText("שאלת הזיהוי : " + temp.getVerificationQuestion() + " ?");
            Verification_button.setVisibility(View.VISIBLE);
        }
        else{
            ValQ.setVisibility(View.INVISIBLE);
            ValAns.setVisibility(View.INVISIBLE);
            ValQ.setText("");
            Verification_button.setVisibility(View.INVISIBLE);
            Toast.makeText(forgotPassword.this,"משתמש לא קיים",Toast.LENGTH_SHORT).show();
        }
    }
    public void VerificationBtnClick(View view){
        if (temp.getVerificationAnswer().compareTo(ValAns.getText().toString())==0){
            thePass.setVisibility(View.VISIBLE);
            thePass.setText("סיסמתך היא : " );
        }
        else{
            Toast.makeText(forgotPassword.this,"תשובה שגויה",Toast.LENGTH_SHORT).show();
        }
    }
    public void BackBtnClick(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }
}
