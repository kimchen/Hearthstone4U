package st.kimsmik.guidehearthstone4u;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public List<IMenuFragment> menuFragments = new ArrayList<>();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private IMenuFragment nowFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));
        CardManager.ins().initCards(this);
        UserData.ins().init(this);
        menuFragments.add(new CardAtlasFragment());
        menuFragments.add(new CustomDeckFragment());
        menuFragments.add(new RecommandDeckFragment());
        menuFragments.add(new ArenaFragment());
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position < 0 || position >= menuFragments.size())
            return;
        nowFragment = menuFragments.get(position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, (Fragment) menuFragments.get(position))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_report) {
            Uri uri = Uri.parse("mailto:ckkimchen@gmail.com");
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        mTitle = title;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(nowFragment != null && nowFragment.onKeyDown(keyCode,event))
            return true;
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            String title = String.format(getString(R.string.exit_alert), getString(R.string.app_name));
            builder.setTitle(title).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    finish();
                    System.exit(0);
                }
            }).setNegativeButton(R.string.no, null).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
