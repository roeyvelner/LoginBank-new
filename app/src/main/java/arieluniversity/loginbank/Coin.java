package arieluniversity.loginbank;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

import BankClass.Client;

public class Coin extends AppCompatActivity {

    ListView listViewCoin ;
    FirebaseDatabase firebase;
    ArrayList items_Coin_list;
    ArrayAdapter<String> itemsCoins;
    boolean isAdmin;
    int Index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        isAdmin = getIntent().getBooleanExtra("admin",false);

        listViewCoin =(ListView) findViewById(R.id.listViewCoin);
        items_Coin_list = new ArrayList();



        firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
        DatabaseReference ref = firebase.getReference();
        ref=ref.getRoot();
        //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
        ref.child("Currency Rates").addValueEventListener(new ValueEventListener() {
            //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                items_Coin_list = new ArrayList();
                for (DataSnapshot child : dataSnapshots) {
                    Double coin = child.getValue(Double.class);
                    items_Coin_list.add(child.getKey() + " : " + coin + " שח ");

                }
                itemsCoins = new ArrayAdapter<String>(Coin.this, android.R.layout.simple_list_item_1, items_Coin_list);

                listViewCoin.setAdapter(itemsCoins);
                listViewCoin.setSelection(5);
                if (isAdmin) {
                    listViewCoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            openEditCoin( listViewCoin.getItemAtPosition(position).toString());
                        }
                    });
                }
                };




            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Coin.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void openEditCoin(String CoinValue) {
        final String name = CoinValue.substring(0,CoinValue.indexOf(":",0)-1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Coin.this);
        View mview = getLayoutInflater().inflate(R.layout.coin_edit, null);
        TextView tv1= (TextView) mview.findViewById(R.id.Coin_name);
        tv1.setText(tv1.getText().toString() + name );
        final EditText ed = (EditText) mview.findViewById(R.id.Edit_text_coin);
        Button b = (Button) mview.findViewById(R.id.NotifyBtn);

        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed.getText().toString().compareTo("")==0) {
                    dialog.hide();
                }
                else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference ref = firebaseDatabase.getReference();
                    ref.child("Currency Rates").child(name).push();
                    ref.child("Currency Rates").child(name).setValue(Double.parseDouble(ed.getText().toString()));
                    dialog.hide();
                }
            }
        });
        dialog.show();
    }
}
