package arieluniversity.loginbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about = (TextView) findViewById(R.id.about);
        about.setText("האפליקציה נוצרה על מנת לספק למשתמש אפשרות לעשות פעולות בחשבונו כגון משיכת כספים הפקדה, לקיחת משכנתא ועוד.הזכויות שמורות לבנק BitCoin ©");
    }
}
