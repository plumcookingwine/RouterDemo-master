package com.kangf.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.plumcookingwine.irouter.annotation.IRouter;
import com.plumcookingwine.irouter.annotation.Parameter;
import com.plumcookingwine.irouter.api.manager.ParameterManager;
import com.plumcookingwine.order.R;

@IRouter(path = "/order/ooo")
public class OrderActivity extends AppCompatActivity {

    @Parameter
    int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ParameterManager.getInstance().loadParameter(this);

        Toast.makeText(this, "parameter === " + test, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("test", 193443);
        setResult(100, intent);
        finish();
    }
}
