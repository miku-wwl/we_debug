核心代码:
``` java
public class DecryptHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        // 1. 读取加密的请求体
        byte[] encryptedData = StreamUtils.copyToByteArray(inputMessage.getBody());
        
        // 2. 解密数据
        byte[] decryptedData = decryptCipher.doFinal(encryptedData);
        String json = new String(decryptedData, StandardCharsets.UTF_8);
        
        // 3. 将 JSON 反序列化为目标对象
        return objectMapper.readValue(json, clazz);
    }
   ...
}
```

### 前端案例（JavaScript，假设用 Fetch 发送加密请求）
```javascript
// 假设使用 CryptoJS 加密（需引入 CryptoJS 库）
import CryptoJS from "crypto-js";

// 前端数据
const user = { username: "test", password: "123456" };
const jsonData = JSON.stringify(user);
// 模拟加密（需与后端解密算法一致）
const encryptedData = CryptoJS.AES.encrypt(jsonData, "secretKey").toString();

// 发送请求
fetch("http://localhost:8080/api/user", {
  method: "POST",
  body: encryptedData, // 发送加密后的内容
  headers: {
    "Content-Type": "application/octet-stream" // 告知后端是二进制加密数据
  }
});
```

---

### 后端案例（Spring Boot 配置 + Controller）
1. **配置 `DecryptHttpMessageConverter`**  
   ```java
   @Configuration
   public class WebConfig {
       @Bean
       public DecryptHttpMessageConverter decryptHttpMessageConverter() {
           return new DecryptHttpMessageConverter();
       }

       @Bean
       public HttpMessageConverters customConverters() {
           return new HttpMessageConverters(decryptHttpMessageConverter());
       }
   }
   ```

2. **Controller 接收解密后的对象**  
   ```java
   @RestController
   public class UserController {
       @PostMapping("/api/user")
       public String handleUser(@RequestBody User user) { // 自动触发解密和反序列化
           return "Received: " + user.getUsername();
       }
   }
   ```

3. **假设 `User` 类**  
   ```java
   public class User {
       private String username;
       private String password;
       // getter/setter
   }
   ```

---
