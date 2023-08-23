package com.myappcompany.rob.employeemanagementapplication20.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.myappcompany.rob.employeemanagementapplication20.Adapter.DateConverter;
import com.myappcompany.rob.employeemanagementapplication20.Dao.TimeDao;
import com.myappcompany.rob.employeemanagementapplication20.Dao.UserDao;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;

@Database(entities = {Users.class, TimeEntries.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class EmployeeDatabaseBuilder extends RoomDatabase {

    public abstract TimeDao timeDao();
    public abstract UserDao userDao();

    public static volatile EmployeeDatabaseBuilder INSTANCE;

    static EmployeeDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (EmployeeDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EmployeeDatabaseBuilder.class, "EmployeeDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
