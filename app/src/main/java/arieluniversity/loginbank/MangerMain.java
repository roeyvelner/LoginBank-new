package arieluniversity.loginbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import BankClass.Client;

public class MangerMain extends AppCompatActivity {
    String user,pass;
    ListView listMashcanta ;
    FirebaseDatabase firebase;
    ArrayList items_Mashcanta_list,Mashcanta_Names,Mashcanta_Uid;
    ArrayAdapter<String> itemsMashcanta,itemsMashcanta2;
    String nameForMaschantaFromDB="";
    long sumMort,paidMort;
    long countOfMort;
    boolean isAdmin;
    int pos;
    ArrayList users;

    ArrayList usersMort;
    ArrayList usersMortName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_main);
        countOfMort=0;
        user = getIntent().getStringExtra("user");
        pass = getIntent().getStringExtra("password");
        isAdmin = getIntent().getBooleanExtra("admin",false);
        if (! isAdmin){
            TextView title,order,manager;
            Button button2,button3;
            title = (TextView) findViewById(R.id.title);
            order = (TextView) findViewById(R.id.order);
            manager = (TextView) findViewById(R.id.manager);
            button2 = (Button) findViewById(R.id.button2);
            button3 = (Button) findViewById(R.id.button3);
            order.setVisibility(View.GONE);
            manager.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            title.setText("פירוט המשכנתא");
            firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
            listMashcanta = (ListView) findViewById(R.id.listMashcanta);
            DatabaseReference ref = firebase.getReference();
            ref = ref.getRoot();
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
            ref.child("Mortgages").child(currentFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        countOfMort = 1;
                        items_Mashcanta_list = new ArrayList();
                        Mashcanta_Uid = new ArrayList();
                        Mashcanta_Names = new ArrayList();
                        Mashcanta_Uid.add(dataSnapshot.getKey());
                        sumMort = (long) dataSnapshot.child("SumMort").getValue();
                        paidMort = (long) dataSnapshot.child("PaidMort").getValue();
                        getName(dataSnapshot.getKey());
                        items_Mashcanta_list.add("סך כל המשכנתא - " + sumMort + "\n" + "סך כל התשלומים ששולמו - " + paidMort + "\n" + "הסכום שנותר לתשלום - " + (sumMort - paidMort) + " שח ");
                    }
                    else{
                        Toast.makeText(MangerMain.this, "אין לך תיק משכנתאות- משתמש ADMIN יכול לפתוח לך", Toast.LENGTH_SHORT).show();
                    }
                }

                ;


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MangerMain.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {


            listMashcanta = (ListView) findViewById(R.id.listMashcanta);

            listMashcanta.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            setListViewHeightBasedOnChildren(listMashcanta);
            items_Mashcanta_list = new ArrayList();


            firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
            DatabaseReference ref = firebase.getReference();
            ref = ref.getRoot();
            //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
            ref.child("Mortgages").addValueEventListener(new ValueEventListener() {
                //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                        countOfMort = dataSnapshot.getChildrenCount();
                        items_Mashcanta_list = new ArrayList();
                        Mashcanta_Uid = new ArrayList();
                        Mashcanta_Names = new ArrayList();
                        for (DataSnapshot child : dataSnapshots) {
                            Mashcanta_Uid.add(child.getKey());
                            if (child.child("SumMort").exists())
                                sumMort = (long) child.child("SumMort").getValue();
                            if (child.child("PaidMort").exists())
                                paidMort = (long) child.child("PaidMort").getValue();
                            getName(child.getKey());
                            items_Mashcanta_list.add("סך כל המשכנתא - " + sumMort + "\n" + "סך כל התשלומים ששולמו - " + paidMort + "\n" + "הסכום שנותר לתשלום - " + (sumMort - paidMort) + " שח ");


                        }
                    }
                }

                ;


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MangerMain.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public  void PermissionClick(View view){
        Intent intent = new Intent(this, Permission.class);
        startActivity(intent);
//        readData("Permissions");
    }

    public  void Coinclick(View view){
        Intent intent = new Intent(this, Coin.class);
        intent.putExtra("admin",true);
        startActivity(intent);
//        readData("Currency Rates");
    }

    public void readData(final String path){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
                    DatabaseReference ref = firebase.getReference();
                    ref=ref.getRoot();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    //Toast.makeText(Login.this, currentFirebaseUser.getUid(), Toast.LENGTH_LONG).show();
                    ref.child(path).addValueEventListener(new ValueEventListener() {
                        //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                            for (DataSnapshot child : dataSnapshots) {
                               Toast.makeText(MangerMain.this,child.getKey() + " = " +  child.getValue().toString(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MangerMain.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }
    public void openEditMashcanta(final String Uid,int index){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MangerMain.this);
        View mview = getLayoutInflater().inflate(R.layout.coin_edit, null);
        TextView tv1= (TextView) mview.findViewById(R.id.Coin_name);
        TextView tv12= (TextView) mview.findViewById(R.id.order_pop);
        tv1.setText("תיקון סכום משכנתא : " );
        tv12.setText("הכנס גובה תשלום נוכחי עבור - " + Mashcanta_Names.get(index));
        final EditText ed = (EditText) mview.findViewById(R.id.Edit_text_coin);
        Button b = (Button) mview.findViewById(R.id.NotifyBtn);
        b.setText("שמור שינויים");
        builder.setView(mview);
        final AlertDialog dialog = builder.create();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed.getText().toString().compareTo("")==0) {
                    dialog.hide();
                }
                else {
                    paidMort =Long.parseLong(ed.getText().toString()) + paidMort;
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference ref = firebaseDatabase.getReference();
                    ref.child("Mortgages").child(Uid).push();
                    ref.child("Mortgages").child(Uid).child("PaidMort").setValue(paidMort);
                    ref.child("Mortgages").child(Uid).child("SumMort").setValue(sumMort);
                    dialog.hide();
                }
            }
        });
        dialog.show();
    }

    private void  getName(String uid) {
        firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
        DatabaseReference ref = firebase.getReference();
        ref = ref.getRoot();
        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Client c = dataSnapshot.getValue(Client.class);
                    nameForMaschantaFromDB = c.getFirstName() + " " +  c.getLastName();
                    Mashcanta_Names.add(nameForMaschantaFromDB);
                    if (countOfMort == Mashcanta_Names.size()) {
                        for (int i = 0; i < Mashcanta_Names.size(); i++) {
                            items_Mashcanta_list.set(i, Mashcanta_Names.get(i).toString() + "\n" + items_Mashcanta_list.get(i).toString());
                        }
                        itemsMashcanta = new ArrayAdapter<String>(MangerMain.this, android.R.layout.simple_list_item_1, items_Mashcanta_list);

                        listMashcanta.setAdapter(itemsMashcanta);
                        listMashcanta.setSelection(5);
                        if (isAdmin) {
                            listMashcanta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    openEditMashcanta(Mashcanta_Uid.get(position).toString(), position);
                                }
                            });
                        }
                    }
                }
                else{
                    nameForMaschantaFromDB = "Anonymus";
                    Mashcanta_Names.add(nameForMaschantaFromDB);
                    if (countOfMort == Mashcanta_Names.size()) {
                        for (int i = 0; i < Mashcanta_Names.size(); i++) {
                            items_Mashcanta_list.set(i, Mashcanta_Names.get(i).toString() + "\n" + items_Mashcanta_list.get(i).toString());
                        }
                        itemsMashcanta = new ArrayAdapter<String>(MangerMain.this, android.R.layout.simple_list_item_1, items_Mashcanta_list);

                        listMashcanta.setAdapter(itemsMashcanta);
                        listMashcanta.setSelection(5);
                        if (isAdmin) {
                            listMashcanta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    openEditMashcanta(Mashcanta_Uid.get(position).toString(), position);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MangerMain.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void addMashcanta(View view){
        openaddMashcanta();
    }



    public void openaddMashcanta(){
        users = new ArrayList() ;

        usersMort = new ArrayList() ;
        usersMortName =new ArrayList() ;


        firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
        DatabaseReference ref = firebase.getReference();
        ref = ref.getRoot();
        ref.child("Mortgages").addValueEventListener(new ValueEventListener() {
            //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : dataSnapshots) {
                        users.add(child.getKey());
                        //usersName.add(child.child("firstName").getValue()   +"" + child.child("lastName").getValue());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MangerMain.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//
        firebase = FirebaseDatabase.getInstance();//("https://bank-login.firebaseio.com/Users/");
        ref = firebase.getReference();
        ref = ref.getRoot();
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            //ref.getRoot().child("Permissions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot child : dataSnapshots) {

                        if (!users.contains(child.getKey())){
                            usersMort.add(child.getKey());
                            usersMortName.add(child.child("firstName").getValue() + " " + child.child("lastName").getValue());
                        }
                    }
                }
                openaddMashcanta2();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MangerMain.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openaddMashcanta2(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(MangerMain.this);
        View mview = getLayoutInflater().inflate(R.layout.coin_edit, null);
        TextView tv1= (TextView) mview.findViewById(R.id.Coin_name);
        final TextView tv12= (TextView) mview.findViewById(R.id.order_pop);
        tv1.setText("הוספת משכנתא למשתמש : " );
        tv12.setText("אנא בחר משתמש");




        ListView listMashcanta2 = (ListView) mview.findViewById(R.id.listMashcanta2);
        itemsMashcanta2 = new ArrayAdapter<String>(MangerMain.this, android.R.layout.simple_list_item_1, usersMortName);
        //itemsMashcanta = new ArrayAdapter<String>(MangerMain.this, android.R.layout.simple_list_item_1, items_Mashcanta_list);
        listMashcanta2.setAdapter(itemsMashcanta2);
        listMashcanta2.setSelection(5);






        if (usersMort.size()==0){
            Toast.makeText(MangerMain.this, "אין משתשמים ללא משכנתא", Toast.LENGTH_SHORT).show();
        }
        else{



            listMashcanta2.setVisibility(View.VISIBLE);
            final EditText ed = (EditText) mview.findViewById(R.id.Edit_text_coin);
            final EditText for_mashcanta_add = (EditText) mview.findViewById(R.id.for_mashcanta_add);
            for_mashcanta_add.setVisibility(View.VISIBLE);
            for_mashcanta_add.setActivated(false);
            //ed.setInputType(8194);
            Toast.makeText(MangerMain.this, ""+ ed.getInputType(), Toast.LENGTH_SHORT).show();
            ed.setHint("הכנס סכום משכנתא");


            listMashcanta2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //openEditMashcanta(Mashcanta_Uid.get(position).toString(), position);
                    for_mashcanta_add.setActivated(true);
                    ed.setActivated(true);
                    tv12.setText("הכנס פרטים עבור " + usersMortName.get(position));
                    pos = position;
                }
            });

            Button b = (Button) mview.findViewById(R.id.NotifyBtn);
            b.setText("שמור שינויים");
            builder.setView(mview);
            final AlertDialog dialog = builder.create();
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ed.getText().toString().compareTo("") == 0   ||   for_mashcanta_add.getText().toString().compareTo("") == 0) {
                        dialog.hide();
                    } else {
                        sumMort = Long.parseLong(ed.getText().toString());
                        paidMort = Long.parseLong(for_mashcanta_add.getText().toString());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference ref = firebaseDatabase.getReference();
                        ref.child("Mortgages").child(users.get(pos)+"").push();
                        ref.child("Mortgages").child(usersMort.get(pos)+"").child("PaidMort").setValue(paidMort);
                        ref.child("Mortgages").child(usersMort.get(pos)+"").child("SumMort").setValue(sumMort);
                        dialog.hide();
                    }
                }
            });
            dialog.show();
        }
    }



}
