package com.myappcompany.rob.employeemanagementapplication20.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.myappcompany.rob.employeemanagementapplication20.Dao.TimeDao;
import com.myappcompany.rob.employeemanagementapplication20.Dao.UserDao;
import com.myappcompany.rob.employeemanagementapplication20.Entities.TimeEntries;
import com.myappcompany.rob.employeemanagementapplication20.Entities.Users;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private TimeDao mTimeDao;
    private UserDao mUserDao;
    private LiveData<List<Users>> mAllUsers;
    private LiveData<List<TimeEntries>> mAllTimeEntries;
    private MediatorLiveData<List<Users>> mUsersLiveData = new MediatorLiveData<>();
    private MediatorLiveData<List<TimeEntries>> mTimeEntriesLiveData = new MediatorLiveData<>();
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        EmployeeDatabaseBuilder db = EmployeeDatabaseBuilder.getDatabase(application);
        mUserDao = db.userDao();
        mTimeDao = db.timeDao();
        mAllUsers = mUserDao.getAllUsers();
    }

    public LiveData<Users> getUsersById(int id) {
        return mUserDao.getUsersById(id);
    }

    public LiveData<List<Users>> getAllUsers() {
        return mAllUsers;
    }

    public void insert(Users users) {
        databaseExecutor.execute(() -> {
            mUserDao.insert(users);
        });
    }

    public void update(Users users) {
        databaseExecutor.execute(() -> {
            mUserDao.update(users);
        });
    }

    public void delete(Users users) {
        databaseExecutor.execute(() -> {
            mUserDao.delete(users);
        });
    }

    public void insert(TimeEntries timeEntries) {
        databaseExecutor.execute(() -> {
            mTimeDao.insert(timeEntries);
        });
    }

    public void update(TimeEntries timeEntries) {
        databaseExecutor.execute(() -> {
            mTimeDao.update(timeEntries);
        });
    }

    public void delete(TimeEntries timeEntries) {
        databaseExecutor.execute(() -> {
            mTimeDao.delete(timeEntries);
        });
    }

    public LiveData<Users> getUserByUsernameAndPassword(String username, String encryptedPasscode) {
        return mUserDao.getUserByUsernameAndPassword(username, encryptedPasscode);
    }

    public void updateAdminUser(Users adminUser) {
        databaseExecutor.execute(() -> {
            mUserDao.update(adminUser);
        });
    }

    public LiveData<TimeEntries> getLastTimeEntry(int employeeID) {
        return mTimeDao.getLastTimeEntry(employeeID);
    }

    public LiveData<List<TimeEntries>> getTimeEntriesForEmployee(int employeeId) {
        return mTimeDao.getTimeEntriesForEmployee(employeeId);
    }

    public void deleteAllTimeEntries() {
        databaseExecutor.execute(() -> {
            mTimeDao.deleteAllTimeEntries();
        });
    }

    public void initializeDatabase() {
        databaseExecutor.execute(() -> {
            String defaultEncryptedPasscode = CryptoUtils.encryptToBase64("012345");
            if (defaultEncryptedPasscode != null) {
                List<Users> adminUsers = mUserDao.getUsersByUsername("admin");
                if (adminUsers == null || adminUsers.isEmpty()) {
                    Users adminUser = new Users();
                    adminUser.setUsername("admin");
                    adminUser.setPassCode(defaultEncryptedPasscode);
                    adminUser.setEmployeeID(1);
                    insert(adminUser);
                } else {
                    Users existingAdmin = adminUsers.get(0);
                    existingAdmin.setEmployeeID(1);
                    update(existingAdmin);
                }
            }
        });
    }
}

