package com.myappcompany.rob.employeemanagementapplication20.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import java.util.List;

@Dao
public interface TimeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TimeEntries timeEntries);

    @Update
    void update(TimeEntries timeEntries);

    @Delete
    void delete(TimeEntries timeEntries);

    @Query("SELECT * FROM TIMEENTRIES WHERE employeeID = :id")
    LiveData<TimeEntries> getTimeEntryById(int id);

    @Query("SELECT * FROM TIMEENTRIES WHERE employeeID = :employeeID ORDER BY recordID DESC LIMIT 1")
    LiveData<TimeEntries> getLastTimeEntry(int employeeID);

    @Query("SELECT * FROM timeentries WHERE employeeID = :employeeId ORDER BY date ASC")
    LiveData<List<TimeEntries>> getTimeEntriesForEmployee(int employeeId);

    @Query("SELECT * FROM timeentries WHERE employeeID != 1")
    LiveData<List<TimeEntries>> getAllTimeEntriesForNonAdminEmployees();

    @Query("DELETE FROM TIMEENTRIES")
    void deleteAllTimeEntries();

}