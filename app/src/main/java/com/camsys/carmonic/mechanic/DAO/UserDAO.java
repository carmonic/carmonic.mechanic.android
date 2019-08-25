package com.camsys.carmonic.mechanic.DAO;

import androidx.room.*;
import com.camsys.carmonic.mechanic.Model.UserModel;
import com.camsys.carmonic.mechanic.Model.Users;

@Dao
public interface UserDAO {
    @Insert
    public long insert(Users user);

    @Update
    public void update(Users user);

    @Delete
    public void delete(Users contact);

    @Query("SELECT * FROM user")
    public Users getUser();

    @Query("Delete FROM user")
    public int DeleteUser();

    @Query("SELECT * FROM user WHERE email = :email")
    public Users getUserByEmail(String email);
}
