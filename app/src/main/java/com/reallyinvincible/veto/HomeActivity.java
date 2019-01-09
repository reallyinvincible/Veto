package com.reallyinvincible.veto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class HomeActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomAppBar = findViewById(R.id.bottom_bar);
        bottomAppBar.replaceMenu(R.menu.bottom_bar_menu);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetKeyFragment bottomSheetKeyFragment = new BottomSheetKeyFragment();
                bottomSheetKeyFragment.show(getSupportFragmentManager(), "KeyFragment");
            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_share_key){
                    startActivity(new Intent(HomeActivity.this, SoundShareActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_item_receive_key){
                    startActivity(new Intent(HomeActivity.this, DemoActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
