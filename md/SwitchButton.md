# SwitchButton

SwitchButton封装

# 1、xml
```
                <com.suntront.liblite.widget.SwitchButton
                    android:id="@+id/switch_btn"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_alignParentRight="true"
                    android:layout_centerVertical="true"
                    android:layout_marginRight="15dp"
                    android:textColor="@color/main_color" />
```

# 2、java
```
    @ViewInject(R.id.switch_btn)
    private SwitchButton switch_btn;
    
    switch_btn.setChecked(false);
```
