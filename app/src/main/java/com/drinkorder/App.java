package com.drinkorder;
import android.app.Application;
import com.drinkorder.data.SeedInitializer;
import com.drinkorder.data.db.AppDatabase;

public class App extends Application {
  @Override public void onCreate(){
    super.onCreate();
    SeedInitializer.runIfFirstLaunch(this, AppDatabase.get(this));
  }
}
