package pl.adamchodera.databasesshowcase;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import pl.adamchodera.databasesshowcase.data.DaoMaster;
import pl.adamchodera.databasesshowcase.data.DaoSession;

public class MyApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        // do this once, for example in your Application class
        final DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "Tasks.db");
        final SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
