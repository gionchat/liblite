# GsonLite

Google Gson序列化、反序列化工具类封装

# 1、GSON String 序列化成class
```
GetAppToken getAppToken = (GetAppToken) GsonLite.toObject(data, GetAppToken.class);
```

# 2、GSON String 序列化成List
```
List<Student> students = GsonLite.toObject("data", new TypeToken<Student[]>() {}.getType())
```
