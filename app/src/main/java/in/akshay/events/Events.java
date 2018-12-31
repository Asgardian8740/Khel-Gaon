package in.akshay.events;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Events extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
