package com.plumcookingwine.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plumcookingwine.irouter.annotation.IRouter;
import com.plumcookingwine.irouter.api.manager.RouterManager;

@IRouter(path = "/user/test")
public class UserActivity extends AppCompatActivity {

    TextView tvReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tvReturn = findViewById(R.id.tv_return);


        Fragment fragment = (Fragment) RouterManager.getInstance().build("/order/frag_test")
                .withInt("test", 123123123)
                .navigation();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mContainer, fragment, "test")
                .commit();
    }

    public void toOrder(View view) {
        RouterManager.getInstance().build("/order/ooo")
                .withInt("test", 123)
                .navigation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(this, "data=null", Toast.LENGTH_LONG).show();
            return;
        }
        if (requestCode == 100) {
            int a = data.getIntExtra("test", -1);
            tvReturn.setText("return === " + a);
        }
    }
}
