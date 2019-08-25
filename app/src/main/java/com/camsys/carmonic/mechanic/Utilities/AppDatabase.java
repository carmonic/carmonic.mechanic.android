package com.camsys.carmonic.mechanic.Utilities;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.camsys.carmonic.mechanic.DAO.UserDAO;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;

@Database(entities = {Users.class}, version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDAO getUserDAO();
}
