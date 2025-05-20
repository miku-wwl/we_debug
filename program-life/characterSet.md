开发平台接口一直返回错误。
``` java
public static String sign(Map<String, String> reqParams, String appSecret) throws NoSuchAlgorithmException {
    //参数排序
    Map<String, String> sortedParams = new TreeMap<>(reqParams);
    //参数拼接成a=1&b=2形式待签名字符串
    String signcontent = sortedParams.entrySet().stream()
            .map(o -> o.getKey() + "=" + o.getValue())
            .collect(Collectors.joining("&"));
    // 拼接appSecret
    signcontent = appSecret + signcontent + appSecret;
    //sha256签名
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(signcontent.getBytes());
    byte[] bytes = digest.digest();
    return Hex.encodeHexString(bytes);
}
```


将参数调整为英文问题解决。
``` java
@Test
public void testCreateProduct() throws Exception {
    Product product = new Product();
    product.setOuterId("5300012");
    // product.setName("手机");
    product.setName("phone");
    product.setPrice("1999.00");
    // 其它参数省略...
    Map<String, String> reqParams = Util.toMop(product);
    reqParams.put("app_id", APP_ID);
    reqParams.put("sign", Util.sign(reqParams, APP_SECRET));
    String res = Util.doPost(reqParams);
    System.out.println(res);
}
```

字符集导致接口

signcontent.getBytes 根据**字符集**将字符串编码成字节串

好 -> ba c3 (GBK)
好 -> e5 a5 bd (UTF-8)

windows 默认GBK， Mac 默认UTF-8


解决方案: digest.update(signcontent.getBytes());  ->  digest.update(signcontent.getBytes(StandardCharsets.UTF_8));


一些老的项目写法
``` java
String userName = request.getParameter("userName");
userName = new String(userName.getBytes("IS0-8859-1"), "UTF-8");
```

旧版本 Tomcat使用ISO-8859-1, 如果没有配置转换成UTF-8, 需要在代码中修改。
``` xml
<Connector port="8080" protocol="HTTP/1. 1"
connectionTimeout="20000"
redirectPort="8443"
URIEncoding="UTF-8" />
<!-- URIEncoding指定URL参数解析编码（默认ISO-8859-1） -->
```


问题总结:
| 问题场景 | 风险描述 | 具体示例 | 解决方案 |
|----------|----------|----------|----------|
| 使用默认 `getBytes()` 方法 | 字符集依赖JVM默认设置（如Windows默认GBK，Linux默认UTF-8），导致跨环境二进制数据不一致 | `String s = "中文"; byte[] bytes = s.getBytes(); // 可能使用GBK编码` | 始终显式指定字符集：`s.getBytes(StandardCharsets.UTF_8)` |
| Tomcat 8之前URL解码问题 | HTTP请求参数默认使用ISO-8859-1解码，导致中文参数乱码 | URL参数：`name=张三`<br>服务器获取：`request.getParameter("name") // 得到乱码` | 在server.xml中配置URIEncoding：`<Connector URIEncoding="UTF-8" .../>` |
| 错误的编码转换 | 使用错误的中间编码进行转换（如先转ISO-8859-1再转UTF-8） | `new String(str.getBytes("ISO-8859-1"), "UTF-8") // 两次转换导致永久乱码` | 直接使用正确的编码读取数据，避免中间转换 |