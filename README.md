# SimpleGsonRequest
*A simple gson request use volley.Sgr use builder pattern to implement volley request.*


#### What is Sgr
The full name is simple gson request.

It's implement Volley request like builder pattern.

Sgr auto set every request's tag and will auto cancel the request which set tag when the activity onDestory().

## Features

```java
        Sgr.builder(this, BaseModule.class) //context must implement INetWork,it will auto to set the request tag.Or you can use .tag() function to set tag.
                .url()              //the url you will request.
                .method()           //default is post you could set get or post.
                .gson()             //default is new Gson you could set it as you want.
                .requestParams()    //set request parmas.
                .setHeaders()       //set request headers.
                .finishListener()   //either fault or success request all will callback this function
                .errorListener()    //only fault request will call this function
                .successListener()  //only success request will call this function
                .clickView()        //you could set some view it can't double click when the request is not return
                .stateView()        //set stateView to sgr.Sgr will help to show some http state
                .timeout()          //set time out time.By default wifi is 15' others is 60'
                .post2Queue();      //the last function to add the request to queue
```

So the simple request is like

```java
        Sgr.builder(this, BaseModule.class)
                .url("http://**************")
                .finishListener(new GsonRequest.FinishListener<BaseModule>() {
            @Override
            public void onFinishResponse(boolean isSuccess, BaseModule response, VolleyError error) {
                        if (isSuccess) {
                            //do something with response
                        } else {
                            //deal some error info
                        }
            }
        }).post2Queue();
```

## How to use

###First of all
You must init Sgr in your application like

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SgrVolley.Init(this);
    }
}
```

###Second 
In your base activity you must implements INetWork.

  INetWork have a method is getVolleyTag.This function get the tag to volley request.The tag will add to every request , and it cancel all the request with this tag when the activity destory.

Third in your base activity override onDestory() function,and call 

```java
        SgrVolley.getInstance().cancelPendingRequests(getVolleyTag());
```

So the BaseActivity like

```java
public class BaseActivity extends Activity implements INetWork {
    protected String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = ((Object) this).getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        SgrVolley.getInstance().cancelPendingRequests(getVolleyTag());
        super.onDestroy();
    }

    @Override
    public String getVolleyTag() {
        return TAG +  this.hashCode();
    }
}
```

###The last step 
add some request in your activity

```java
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StateView stateView = (StateView) findViewById(R.id.stateView);
        stateView.setOnRetryListener(new StateView.RetryListener() {
            @Override
            public void onRetry() {
                Toast.makeText(MainActivity.this, "test retry", Toast.LENGTH_SHORT).show();
            }
        });

        Sgr.builder(this, BaseModule.class).stateView(stateView)
                .url("http://**********")
                .finishListener(new GsonRequest.FinishListener<BaseModule>() {
            @Override
            public void onFinishResponse(boolean isSuccess, BaseModule response, VolleyError error) {


            }
        }).post2Queue();
    }

}
```

The StateView help you to show some error infomation like NO_NETWORK,ERROR_404 and so on.

