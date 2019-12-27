# TitleBar

TitleBar封装,使用简单方便

# 1、xml布局
```
    <com.suntront.liblite.widget.TitleBar
        android:id="@+id/title_bar"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:layout_alignParentTop="true"
                android:background="@color/main_color"
                android:elevation="3dp"/>
```

# 2、Activity
```
    title_bar.setTitle("我的订单");
    addBack(title_bar);
```

# 3、添加Action
```
        title_bar.setActionTextColor(Color.WHITE);
        title_bar.addAction(new TitleBar.TextAction("添加") {
            @Override
            public void performAction(View view) {
                Intent intent = new Intent(BindListActivity.this, BindActivity.class);
                startActivity(intent);
            }
        });
```
