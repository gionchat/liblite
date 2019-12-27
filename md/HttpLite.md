# HttpLite

支付宝支付，微信支付封装,使用简单方便

# 1、初始化库（建议在application里初始化）
```
    String RSA_PRIVATE="6e**********eo5ff9==";
        //支付宝支付配置
        PayConfig.alipay()
                .appId("2017******3659")
                .rsa(RSA_PRIVATE)
                .notifyUrl("http://www.xx**xx.com/api/trade/notify/alipay")
                .intro("支付宝订单-")
                .init();

        //微信支付配置
        PayConfig.wxpay()
                .appId("wx62*******87")
                .mchId("15******1")
                .apiKey("Rd88G******I0ci406")
                .notifyUrl("http://www.xx**xx.com/api/trade/notify/wxpay")
                .intro("微信订单-")
                .init();
```

# 2、在支付的地方调用pay，ps:微信支付需要在包名下添加 wxapi.WXPayEntryActivity
```
        //支付宝支付
        String tradenumber = "这里是实际交易订单号-服务器返回";
        double money=0.01;//需要支付的金额
        PayTools.aliPay(this)
                .tradeNumber(tradenumber)
                .money(money)
                .intro("订单-")//（非必填）
                .pay(new AlipayUtil.AlipayCallBack() {
                    @Override
                    public void success(String ordernumber, String resultInfo) {

                    }

                    @Override
                    public void fail(String resultInfo) {

                    }

                });

        
    
        //微信支付
        String tradenumber = "这里是实际交易订单号-服务器返回";
        double money=0.01;//需要支付的金额
         PayTools.wxPay(this)
                .tradeNumber(tradenumber)
                .intro("订单-")//（非必填）
                .money(money)
                .pay();//支付回调在WXPayEntryActivity


```
