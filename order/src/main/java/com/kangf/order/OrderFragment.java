package com.kangf.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plumcookingwine.irouter.annotation.IRouter;
import com.plumcookingwine.irouter.annotation.Parameter;
import com.plumcookingwine.irouter.api.manager.ParameterManager;
import com.plumcookingwine.order.R;

@IRouter(path = "/order/frag_test")
public class OrderFragment extends Fragment {

    @Parameter
    int test;

    private TextView tvParams;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 注入参数
        ParameterManager.getInstance().loadParameter(this);

        tvParams = getView().findViewById(R.id.tv_params);

        tvParams.setText("params ==== " + test);


    }
}
