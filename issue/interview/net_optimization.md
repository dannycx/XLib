# 网络优化

## 了解网络类型
### 网络连接类型
1. 单  工通信：数据只能一方发送到另一方。（例：UDP协议，Android广播）
2. 半双工通信：数据可以从A发送到B，也可以从B发到A，但同一时刻，只能有一个方向的数据传输。（例：Http协议，向服务器发起请求，服务器返回响应）
3. 全双工通信：任意时刻，都会存在A到B和B到A的双向传输。（例：Socket协议，长连接通道，IM聊天，视频通话）

* Http1.0协议默认关闭长连接属于半双工通信，可增加http请求头字段"Connection:Keep-Alive",Http1.1默认开启，可增加HTTP请求头字段"Connection:close"

### DNS 解析
* 添加trace日志除了可以看到net timeout错误，还可以看到UnknowHostException：DNS解析失败，未解析获取到服务器ip地址

#### 添加DNS解析策略
```
Object HttpTool {
    private const val TAG = "HttpTool"
    private const val URL = "https://www.xxx.com"

    fun initHttp() {
	val client = OkHttpClient.Builder().build()
	Request.Builder().url(URL).build().also {
            kotlin.runCatching {
                client.newCall(it).execute()
            }.onFailure {
                Log.e(TAG, "initHttp: error: $it")
            }
        }
    }
}
```
* 很明显xxx.com域名不存在，就会报如下错误：
```
java.net.UnknownHostException: Unable to resolve host "www.xxx.com": No address associated with hostname
```
* 一旦遇到如上问题：域名被劫持修改，整个服务会处于宕机状态，体验很差。
* 优化方案：通过OkHttp提供的自定义DNS解析器优化处理
```
package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

// 路由寻址
public interface Dns {
    /**
     * 使用｛@link InetAddress#getAllByName｝请求底层操作系统查找IP地址。
     * 大多数自定义｛@link Dns｝实现都应委派到此实例。
     */
    Dns SYSTEM = (hostname) -> {
        if (hostname == null) {
            throw new UnknownHostException("hostname == null");
        } else {
            try {
                return Arrays.asList(InetAddress.getAllByName(hostname));
            } catch (NullPointerException var3) {
                UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                unknownHostException.initCause(var3);
                throw unknownHostException;
            }
        }
    };

    /**
     * 返回｛@code hostname｝的IP地址，按OkHttp尝试的顺序排列。
     * 如果连接到某个地址失败，OkHttp将重试与下一个地址的连接，直到要么建立了连接，要么耗尽了IP地址集，要么超出了限制。
     */
    List<InetAddress> lookup(String var1) throws UnknownHostException;
}

// 自定义寻址策略
class MyDns: Dns {
    private const val TAG = "MyDns"

    override fun lookup(hostname: String): MutableList<InetAddress> {
        val result = mutableListOf<InetAddress>()
        var systemAddressList: MutableList<InetAddress>? = null

        // 通过系统DNS解析
        kotlin.runCatching {
            systemAddressList = Dns.SYSTEM.lookup(hostname)
        }.onFailure {
            Log.e(TAG, "lookup: it")
        }

        if (systemAddressList != null && systemAddressList!!.isNotEmpty()) {
            result.addAll(systemAddressList!!)
        } else {
            // 系统DNS解析失败，走自定义路由
            result.add(InetAddress.getByName(www.google.com))

            // 一台服务器支持多域名
            // result.addAll(InetAddress.getAllByName(ip).toList())
        }
    }
}

使用：
val client = OkHttpClient.Builder().dns(MyDns()).build()
```
* 使用如上方案，www.xxx.com域名解析失败就会替换为www.google.com，从而避免请求失败问题

### 接口数据适配策略
* 数据格式异常：服务器定义int类型返回字符串"" 或 空数组返回null...
```
List适配：空解析为空数组
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.Collections

class ListTypeAdapter: JsonDeserializer<List<*>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<*> {
        return try {
            if (json?.isJsonArray == true) {
                Gson().fromJson(json, typeOfT)
            } else {
                Collections.EMPTY_LIST
            }
        } catch (e: Exception) {
            Collections.EMPTY_LIST
        }
    }
}


String适配：是否为基础类型（String，Number，Boolean），基础类型正常转
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class StringTypeAdapter: JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
        return try {
            if (json?.isJsonPrimitive == true) {
                Gson().fromJson(json, typeOfT)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}

使用：
GsonBuilder()
    .registerTypeAdapter(String::class.java, StringTypeAdapter())
    .registerTypeAdapter(List::class.java, ListTypeAdapter())
    .create().also {
        GsonConverterFactory.create(it)
    }
```

## Https协议
* 保证http数据传输的可靠性。s：SSL/TLS协议
+ App(Http) ：        TCP IP
+ App(Https)：SSL/TLS TCP IP
* Https协议相比于Http协议在TCP协议前会先走SSL/TLS协议

### 对称加密与非对称加密
* https能保证传输可靠性，是因为对数据进行了加密，http为明文传输，数据易被窃取和冒充
* http优化过程中，对数据进行了加密传输，https因此诞生

#### 对称加密
* 对称加密和解密采用一把钥匙，需双方约定，发送方通过密钥加密数据，接收方使用同一密钥解密获取传递数据。
* 流程：发送方 A 发送用密钥加密后的数据 ---> 接收方 B 接收加密数据并用密钥解密

* 优缺点
+ 流程简单，解析数据快。
- 安全性差，密钥管理有风险（因需约定同一key，key存在传输劫持风险，统一存储存在被攻击风险）。

#### 非对称加密
* 两把密钥：私钥 + 公钥，公钥任何人可知，发送方使用公钥加密，接收方可以用只有自己知道的私钥解密并获取数据。
* 流程：接收方B发送公钥至发送方A ---> A发送数据，公钥加密 ---> B接收数据，私钥解密

* 优缺点
+ 私钥只有自己知道。
- 流程繁琐，解密速度慢。

### 公钥的安全保障
#### CA证书
* 生成规则
* 接收方公钥+接受方身份信息+其他信息--->Hash算法--->消息摘要--->CA机构私钥加密--->数字签名

* 最终发送给接收方是数字证书：数字签名+公钥+接收方的个人信息等
* 合法性验证：比较数字证书的消息摘要（Hash算法和CA公钥解密）是否一致

### https的传输流程
* 一个https请求，包含2次http传输：以请求www.google.com流程为例
1. 客户端向服务端发起请求，要访问google，先于google服务器建立连接
2. 服务端有公钥和私钥，公钥可以发给客户端，然后给客户端发送一个SSL证书，证书包含：CA签名、公钥、google的信息
3. 客户端接收到SSL证书后，对CA签名解密，判断证书合法性，不合法就断开连接，合法则生成一个随机数，作为数据对称加密的密钥，通过公钥加密发送至服务端
4. 服务端接收到客户端加密数据后，通过私钥解密，拿到对称加密的密钥，然后将google相关数据通过对称加密密钥加密，发送到客户端
5. 客户端通过解密拿到服务端的数据，请求结束

* https非完全非对称加密：对称加密密钥传递有风险，前期通过非对称加密传递对称加密密钥，后续数据传递都是通过对称加密，以提高数据解析效率。
