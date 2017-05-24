package pl.adamchodera.databasesshowcase;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        // do this once, for example in your Application class
//        new DaoMaster.DevOpenHelper(this, "notes-db", null);
//        db = helper.getWritableDatabase();
//        daoMaster = new DaoMaster(db);
//        daoSession = daoMaster.newSession();
//// do this in your activities/fragments to get hold of a DAO
//        noteDao = daoSession.getNoteDao();
    }
}
