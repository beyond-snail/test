package com.example.administrator.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.id_et_title)
    EditText idEtTitle;
    @BindView(R.id.id_btn1)
    Button idBtn1;
    @BindView(R.id.id_btn2)
    Button idBtn2;
    @BindView(R.id.id_webview)
    WebView idWebview;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        idWebview.loadUrl("file:///android_asset/test");


        WebSettings settings = idWebview.getSettings();
        //启用js，若值为false，那么html中的js将不可用
        settings.setJavaScriptEnabled(true);
        //不缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        ////下面这两个采用默认。如果需要自己对页面响应以及一些浏览器自带方法重写，可以继承WebChromeClient、WebViewClient
        idWebview.setWebViewClient(new WebViewClient());
        idWebview.setWebChromeClient(new WebChromeClient());


        /**
         * 注册JavascriptInterface，其中"DEMO"的名字随便取，如果你用"DEMO"，那么在html中只要用  DEMO.方法名()
         * 即可调用MyJavascriptInterface里的同名方法，参数也要一致
         */

        idWebview.addJavascriptInterface(new MyJavaScriptInterface(), "demo");
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    idEtTitle.setText(msg.obj.toString());
                    break;
                case 1:
                    String data = "我会在js的回调中被alert";
                    //js为弱类型语言，传递string型注意加引号
                    idWebview.loadUrl("javascript:callbacks[" + msg.arg1 + "]('" + data + "')");
                    break;
            }
        }
    };



    /**
     * 自定义JavascriptInterface，只要这个类里面的方法带有@JavaScriptInterface注解，才可以被js调用
     */
    public class MyJavaScriptInterface{
        public MyJavaScriptInterface(){

        }

        @JavascriptInterface
        public void toast(String str){
            Toast.makeText(WebViewActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        //@JavascriptInterface中不能处理和ui有关的东西，所以用handler
        @JavascriptInterface
        public void setTitle(String str){
            Message msg = Message.obtain();
            msg.obj = str;
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void setTitleAndCallback(String text,int index){
            Message msg = Message.obtain();
            msg.what=0;
            msg.obj=text;
            //@JavascriptInterface中不能处理和ui有关的东西，所以用handler
            mHandler.sendMessage(msg);
            Message msg2= Message.obtain();
            msg2.what=1;
            msg2.arg1=index;
            mHandler.sendMessage(msg2);
        }
    }



    @OnClick({R.id.id_btn1, R.id.id_btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn1:
                //调用当前html中的js方法，直接loadUrl("javascript:方法名")即可，可以传递参数，单个，多个参数都可以，这里演示单个参数
                idWebview.loadUrl("javascript:setText('"+idEtTitle.getText()+"')");
                break;
            case R.id.id_btn2:
                idWebview.loadUrl("javascript:getText()");
                break;
        }
    }
}
