package com.myappcompany.rob.employeemanagementapplication20.Dao;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Users users);

    @Update
    void update(Users users);

    @Delete
    void delete(Users users);

    @Query("SELECT * FROM USERS ORDER BY employeeID ASC")
    LiveData<List<Users>> getAllUsers(); // Change return type

    @Query("SELECT * FROM USERS WHERE employeeID = :id")
    LiveData<Users> getUsersById(int id);

    @Query("SELECT * FROM USERS WHERE username = :username AND passcode = :encryptedPasscode")
    LiveData<Users> getUserByUsernameAndPassword(String username, String encryptedPasscode);

    @Query("SELECT * FROM users WHERE username = :username")
    List<Users> getUsersByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    LiveData<Users> getUserByUsername(String username);


}
