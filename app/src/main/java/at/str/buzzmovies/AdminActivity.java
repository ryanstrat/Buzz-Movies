package at.str.buzzmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements UserListCallback{
    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserAdapter;
    private RecyclerView.LayoutManager mUserListLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mUserList = (RecyclerView) findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);

        mUserListLayout = new LinearLayoutManager(this);
        mUserList.setLayoutManager(mUserListLayout);

        AccountController.getAccountList(this.getApplicationContext(), this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getMenuInflater().inflate(R.menu.admin_options_menu, optionMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.overflow_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Directs app to the LoginActivity page.
     */
    private void logout() {
        localStore.setCurrentAccount(null);
        Intent toLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(toLoginActivity);
    }

    /**
     * Populates the UI with a list of user accounts
     * @param accounts List of accounts that should be displayed in the UI
     */
    public void populateUserList(ArrayList<Account> accounts) {
        Log.i("Admin", "UserList Callback");
        mUserAdapter = new UserAdapter(accounts);
        mUserList.setAdapter(mUserAdapter);
    }


    public void handleBanAccountClick(View view) {
        Account account = (Account) view.getTag();

        account.setStatus(toggleStatus(account.getStatus()));


        AccountController.setAccountStatus(this.getApplicationContext(), account);
    }


    /**
     * Toggles account status between active an banned
     * @param status status. Either "active" or "banned"
     * @return returns the oppostive of what was passed in. Null if input is invalid
     * @throws NullPointerException If input String is null
     */
    public String toggleStatus(String status) {
        if (status == null) {
            throw new NullPointerException("Status string can't be null");
        }

        switch(status) {
            case ("banned"):
                return "active";
            case ("active"):
                return "banned";
            default:
                return null;
        }
    }
}
