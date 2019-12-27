# PreferenceLite

SharedPreference封装食用用简单方便

# 1、存值
```
PreferencesLite.setData("userinfo", GsonLite.toJson(result.getUserInfo()));
```

# 2、取值
```
String userinfo = (String) PreferencesLite.getData("userinfo", "");
```
