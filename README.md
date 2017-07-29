# 恒生拼车系统安卓客户端
此客户端可供拼车系统的司机和乘客分别进行使用。
## 登录账号
司机以手机号作为登录的账号，单个手机号只能用来注册一个对应的司机用户。

乘客以公司工号作为登录账号，单个公司工号只能用来注册一个乘客用户。

## 司机状态更新
在司机登录入安卓端后，手机会在联网的前提下对数据库内的司机实时位置进行更新，并在接立即单后更改司机的接单状态，不会继续向司机分发立即单的数据。

## 订单状态更新
乘客在下单后，会通过安卓端向服务器发送订单数据，然后服务端向司机分发订单