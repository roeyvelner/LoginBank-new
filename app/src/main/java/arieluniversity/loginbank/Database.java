package arieluniversity.loginbank;

import com.firebase.client.Firebase;

/**
 * Created by User on 26/12/2017.
 */

public class Database extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
