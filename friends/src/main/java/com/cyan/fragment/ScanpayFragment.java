package com.cyan.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
import com.cyan.community.R;
import com.cyan.scanpay.InstallPlugin;
import com.cyan.scanpay.PayOrder;

import cn.bmob.v3.BmobUser;



/**
 * User: 杨月(1298375722@qq.com)
 * Date: 2016-05-25
 * Time: 10:28
 * 类说明：
 */
public class ScanpayFragment extends PreferenceFragment
{

    BmobPay bmobPay;
    Bitmap whole;
    PopupWindow choseDialog;
    PayOrder order;

    private Button pay;

    public static ScanpayFragment newInstance()
    {
        ScanpayFragment scanpayFragment = new ScanpayFragment();
        return scanpayFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BmobPay.init(getActivity(), "9c3147854c788ad6895cbdff4f386770");
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentView = inflater.inflate(R.layout.activity_scanpay, null);
        initView(fragmentView);

        return fragmentView;
    }

    private void initView(View fragmentRootView)
    {
        pay = (Button) fragmentRootView.findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                choseType();
            }
        });


        bmobPay = new BmobPay(getActivity());
        order = new PayOrder();
        if (BmobUser.getCurrentUser(getActivity()) != null) {
            order.setUsername(BmobUser.getCurrentUser(getActivity()).getUsername());
        }
    }

     public void choseType() {

            View typeDialog = getActivity().getLayoutInflater().inflate(
                    R.layout.dialog_paytype, null);
            typeDialog.findViewById(R.id.alipay).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            pay(true);
                        }
                    });
            typeDialog.findViewById(R.id.wxpay).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            pay(false);
                        }
                    });
         choseDialog = new PopupWindow(typeDialog,
                 WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Toast.makeText(getActivity(), "请选择支付方式", Toast.LENGTH_SHORT).show();
         choseDialog.showAtLocation((View) fragmentView, Gravity.CENTER, 0, 0 );

    }



    public void pay(final boolean byAli) {
        Toast.makeText(getActivity(), "正在申请支付。。请稍候。。", Toast.LENGTH_SHORT).show();

        order.setPaid(false);
        order.save(getActivity());

        PayListener listener = new PayListener() {

            @Override
            public void unknow() {
                Toast.makeText(getActivity(), "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void succeed() {
                Toast.makeText(getActivity(),
                        "支付操作完成!请等待服务器校验通过即可满足您的要求!", Toast.LENGTH_SHORT)
                        .show();
                checkAgain();
            }

            @Override
            public void orderId(String arg0) {
                order.setOrderId(arg0);
                order.update(getActivity());
            }

            @Override
            public void fail(int arg0, String arg1) {
                Toast.makeText(getActivity(), "支付失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
                if (!byAli && arg0 == -3) {
                    Toast.makeText(getActivity(), "您尚未安裝微信支付插件", Toast.LENGTH_LONG)
                            .show();
                    InstallPlugin.installBmobPayPlugin(getActivity(),
                            InstallPlugin.ASSETS_PLUGIN);
                }
            }
        };

        if (byAli) {
            bmobPay.pay(Double.parseDouble("10.0"),
                    "支付宝", listener);
           /* bmobPay.pay(Double.parseDouble(order.getPrice().toString()),
                    order.getName(), listener);*/
        } else {
            bmobPay.pay(Double.parseDouble("10.0"),
                    "微信", listener);
           /* bmobPay.payByWX(Double.parseDouble(order.getPrice().toString()),
                    order.getName(), listener);*/
        }

    }

    void checkAgain() {
        bmobPay.query(order.getOrderId(), new OrderQueryListener() {

            @Override
            public void succeed(String arg0) {
                if (arg0.equals("NOTPAY")) {
                    Toast.makeText(getActivity(), "支付失败，很抱歉你只能看这么一点了",
                            Toast.LENGTH_SHORT).show();
                } else {
                    order.setPaid(true);
                    order.update(getActivity());
                    Toast.makeText(getActivity(),
                            order.getName() + "!", Toast.LENGTH_SHORT)
                            .show();

                    }
            }

            @Override
            public void fail(int arg0, String arg1) {
                Toast.makeText(getActivity(), "查询失败，很抱歉你只能看这么一点了",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
