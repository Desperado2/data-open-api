## 整体架构
![module.png](module.png)

## 一、整体模块说明
数据开放平台整体分为对外展示端和内部数据开发端，两者共同组成了整个数据开放平台，推动数据开放更加的便捷、快速、安全。
对外展示端：
对外展示端，主要展示目前对外开放的API，以及API的详细对接信息，通过浏览展示的API信息，数据使用方可以来筛选自己所需要的API，进行API的申请，并且根据API的描述信息完成自主对接。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1683175988041-c548a7b0-2d32-44b9-8b89-ec3a27b6d372.png#averageHue=%23c17a42&clientId=ud463d76c-6b8d-4&from=paste&height=463&id=u81367b24&originHeight=757&originWidth=1383&originalType=binary&ratio=1&rotation=0&showTitle=false&size=337190&status=done&style=none&taskId=u89f14b67-41bf-4e07-9649-55a721365b4&title=&width=846)
内部数据开发端：
内部数据开发端，主要进行数据的对外开放，通过简单的配置即可完成对数据的开放，开发的数据信息实时显示到对外展示端，轻松完成数据开放的开发功能。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681179265045-38fbf3ad-fbbb-43f8-8c82-df484c187c22.png#averageHue=%23edcfa4&clientId=u8182f372-3d58-4&from=paste&height=455&id=u67767d1a&originHeight=757&originWidth=1383&originalType=binary&ratio=1&rotation=0&showTitle=false&size=89150&status=done&style=none&taskId=u00e75d80-ab24-47a3-b341-a444f18bf5c&title=&width=832)
## 二、部署说明
### 1、数据平台部署依赖
该平台目前强依赖于JDK、MySQL，弱依赖于Redis、Nginx。意味着在部署该平台之前必须安装JDK和MySQL，而Redis和Nginx是可选的。

- JDK：推荐使用open-jdk 8
- MySQL：推荐使用MySQL 8.0版本
- Redis：推荐使用最新版本
- Nginx：推荐使用最新版本。
### 2、数据平台部署步骤

1. 解压tar.gz包
```bash
tar -zxf open-data-platform.tar.gz
```

2. 初始化数据库
- 创建数据库：创建一个数据库，并且指定该数据库对应的账号密码，
- 初始化数据库，执行目录下面的sql文件夹下面的open_platform.sql文件。
```bash
source open-data-platform/sql/open_platform.sql
```

3. 修改配置文件，具体修改点如下：
```properties
# 系统访问协议
server.protocol=http 
# 系统启动之后的访问地址
server.address=127.0.0.1
# 系统启动之后的访问端口
server.port=8080


# 钉钉机器人tokenId  如果没有或者无法连接外网，请注释该配置
dingtalk.tokenId=ac30f966fc10bdc51400a14b0343498fa6487bb556b5e3f913ab4b31ae70b4e9
# 钉钉机器人秘钥
dingtalk.secret=SEC410977bee089c554fc05e70154131aab571224b3aaa4422734b155be64c1bd9c

# 邮箱相关 如果没有或者无法连接外网，请注释该配置
# 邮箱host
spring.mail.host=smtp.163.com
# 邮箱端口
spring.mail.port=25
# 邮箱账号
spring.mail.username=1111@163.com
# 邮箱地址
spring.mail.fromAddress=22222@163.com
# 邮箱授权码
spring.mail.password=xxxxxxxxxxx
# 发件人昵称
spring.mail.nickname=xxx

# 数据库地址
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/open_platform?useSSL=false
# 数据库账号
spring.datasource.username=user_name
# 数据库密码
spring.datasource.password=password

# REDIS配置，如不使用Redis请不要配置相关属性
# Redis数据库
spring.redis.database=0
# Redis数据库地址
spring.redis.host=127.0.0.1
# Redis数据库端口
spring.redis.port=6379
# Redis数据库密码
spring.redis.password=password


# 缓存类型，取值范围为[local、redis],如未配置redis，请修改值为local
open.data.platform.base.cache-type=redis
# 是否开启限流
open.data.platform.base.access-limiter-open=false
# 限流类型，取值范围为[local、redis],如未配置redis，请修改值为local
open.data.platform.base.access-limiter-type=redis


# 对外开放API的统一前缀，必须以/开头，不能以/结尾
open.data.platform.base.open-api-base-register-path=/open-api
# 对外开放API的访问地址，必须为完整URL，不能以/结尾
open.data.platform.base.open-api-service-address=http://127.0.0.1:15124


# cookies 的域名
open.data.platform.cookie.domain=127.0.0.1
# cookies 的有效期，单位秒。默认7200s
open.data.platform.cookie.timeout=7200

# 用户登录状态的延期时间，默认1800s，即用户登录剩余30分钟的时候，将其重置为2小时
open.data.platform.user.token.refresh-token-end-time=1800
# 用户登录状态的有效期，默认7200s  即2小时
open.data.platform.user.token.timeout=7200


```

4. 启动
```properties
# 启动必须在根目录下进行
./bin/platform-daemon.sh start
```

5. 查看状态
```properties
./bin/platform-daemon.sh status

# 启动成功状态为
Begin status ......
  [  RUNNING  ]
End status .

# 未启动状态为
Begin status ......
  [  STOP  ]
End status .
```

6. 停止
```properties
# 启动必须在根目录下进行
./bin/platform-daemon.sh stop
```

7. 启动报错日志路径
```properties
tail -n 100 logs/open-data-platform.out
```

## 三、开发人员使用手册
### 1.简易报表
简易报表模块，提供了目前的一些数据统计和分析，具体统计分析数据如下：

- API数量：目前对外的开放的所有API数量，不包括以下下线的API和不开放的API。
- 用户数量：表示目前已经注册并激活并且未被禁用用户数量。
- 订阅数量：表示目前所有用户已经申请的API的数量。
- 调用次数：表示当前对外开放的API被外部调用的次数。
- 成功次数：表示当前对外开放的API被外部调用的次数中成功的次数。
- 失败次数：表示当前对外开放的API被外部调用的次数中失败的次数。
- 请求成功率：请求成功率=(成功次数/调用次数)* 100%.
- 面积图：表示了最近7天的调用次数、成功次数、失败次数的变化趋势。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681179439418-a7496e6d-0566-42a7-b026-d6f98aac735c.png#averageHue=%238cf9aa&clientId=u8182f372-3d58-4&from=paste&height=448&id=u090108bf&originHeight=757&originWidth=1383&originalType=binary&ratio=1&rotation=0&showTitle=false&size=103065&status=done&style=none&taskId=u8aeb9b29-ab38-4545-b64b-eb4124484af&title=&width=818)
### 2.用户管理
用户管理模块包含了角色管理和用户管理两个功能，分别管理角色和用户。下面分布说明：
角色管理：
角色管理用于系统的角色相关管理工作。其中admin角色为数据开发者角色，其余角色全为数据使用者角色，普通用户注册之后，会默认赋予数据使用者的角色，赋予的角色取决的当前角色列表中的默认角色。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180021080-168ce614-5ea2-4ce8-ab5d-7f0a3a3292c4.png#averageHue=%23fefdfd&clientId=u8182f372-3d58-4&from=paste&height=228&id=u42c74b4d&originHeight=348&originWidth=1133&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23369&status=done&style=none&taskId=u6126e218-79fb-4c3e-b373-a830a472a76&title=&width=742)
用户管理：
用户管理用于管理用户信息，包用户的新增和修改、激活等操作。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1683177214979-d2527858-51df-429a-8b85-a1e52f7f40ad.png#averageHue=%23fefefd&clientId=ud463d76c-6b8d-4&from=paste&height=201&id=u472881be&originHeight=404&originWidth=1582&originalType=binary&ratio=1&rotation=0&showTitle=false&size=64100&status=done&style=none&taskId=u15b5fc72-609f-4914-842b-2f68f817443&title=&width=789)
创建用户的流程如下：

- 新增用户

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1683177392275-e6d3b510-eaad-44aa-96e9-7f32efab6031.png#averageHue=%23fefefe&clientId=ud463d76c-6b8d-4&from=paste&height=337&id=ub36cb0d8&originHeight=337&originWidth=928&originalType=binary&ratio=1&rotation=0&showTitle=false&size=13163&status=done&style=none&taskId=u1ef3689c-6649-415f-b75f-b5afccad605&title=&width=928)

- 激活用户

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1683177417345-0032a56d-7d02-4b1f-92fe-ac1e83aae5b8.png#averageHue=%2386c854&clientId=ud463d76c-6b8d-4&from=paste&height=204&id=ucf0881ff&originHeight=264&originWidth=1296&originalType=binary&ratio=1&rotation=0&showTitle=false&size=46132&status=done&style=none&taskId=u43543220-4d30-4f68-aad8-ed712b1f772&title=&width=1000)

- 查询密码（注意：密码只在激活之后10分钟内有效，请在10分钟之内获取）

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1683177436870-a45f1aec-8015-46f3-ac22-d0cc9972f1b4.png#averageHue=%23efefef&clientId=ud463d76c-6b8d-4&from=paste&height=181&id=u6556fe25&originHeight=181&originWidth=411&originalType=binary&ratio=1&rotation=0&showTitle=false&size=13466&status=done&style=none&taskId=u0b01c5c1-e719-4ee6-bcc0-80f5c58692a&title=&width=411)
### 3.数据源管理
数据源管理用于管理数据开放过程中涉及到的数据源，目前支持的数据源有：MYSQL、DB2、ORACLE、PostgreSQL、presto、SQLServer、ClickHouse、StarRocks、Doris、ElasticSearch。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180402898-8a5d73fa-f13c-49fe-ac04-0773f96820e3.png#averageHue=%23fdfcfc&clientId=u8182f372-3d58-4&from=paste&height=778&id=u37441b97&originHeight=778&originWidth=909&originalType=binary&ratio=1&rotation=0&showTitle=false&size=95780&status=done&style=none&taskId=u7b38f31f-e76f-4222-a8e1-d0d00e951f5&title=&width=909)
数据源的配置，主需要配置数据源相应的连接信息即可。配置的数据源可以在API开发中被使用。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180277892-79c1ab6f-3cc4-4647-a6d2-7e6e7f003d78.png#averageHue=%23fefefe&clientId=u8182f372-3d58-4&from=paste&height=406&id=uf0dccfce&originHeight=406&originWidth=689&originalType=binary&ratio=1&rotation=0&showTitle=false&size=17503&status=done&style=none&taskId=u5cf9dce8-cd98-4192-9c8f-5a8d3c2f658&title=&width=689)
### 4.开放API管理
开放API管理，目前包含了API分类管理和API管理两个模块。
API分类管理：
API分类管理用于管理API的分类信息，分类信息主要左右有两个方面，一是对API进行分类便于API的筛选与查询，二是用于展示端对API进行归类展示。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180564341-91229145-1dd8-4dc5-ab24-88b0d354571f.png#averageHue=%23fefefd&clientId=u8182f372-3d58-4&from=paste&height=521&id=u15f36942&originHeight=521&originWidth=1066&originalType=binary&ratio=1&rotation=0&showTitle=false&size=46482&status=done&style=none&taskId=u5f349baa-096e-4d86-ae3d-558f9b5ff5f&title=&width=1066)
API管理：
API管理用于对外开发的API的整个管理流程，其包含了API的整个生命周期的管理，主要功能包括：

- API的基础信息维护：包括API的名称、描述、调用信息、参数信息、返回值信息、状态等。
- API的开发：用于开发具体的API逻辑。
- API的生命周期管理：包括API多环境的上下架、对外申请的管理。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180753701-c0632406-cd7a-4143-8d06-917d61015f30.png#averageHue=%23fefaf9&clientId=u8182f372-3d58-4&from=paste&height=483&id=ud02d966d&originHeight=674&originWidth=1567&originalType=binary&ratio=1&rotation=0&showTitle=false&size=90265&status=done&style=none&taskId=u0c1f7494-1b62-4751-9614-681cc40d9b0&title=&width=1123)
### 5.数据管理
数据管理模块，包含了申请审核和请求日志两个模块。
申请审核：
针对用户的API申请进行审核，可以通过或拒绝，通过之后也可以禁用，禁用之后用户无法在使用该接口。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681180880418-e63a459e-46c6-4834-938a-3f41f749e810.png#averageHue=%23e2c39d&clientId=u8182f372-3d58-4&from=paste&height=421&id=u07753cfc&originHeight=421&originWidth=1039&originalType=binary&ratio=1&rotation=0&showTitle=false&size=44254&status=done&style=none&taskId=u641de396-d8bc-47cd-927d-dce5b39c111&title=&width=1039)

请求日志：
请求日志信息记录了该平台所有对外开放API的请求日志信息，包括URL、环境、API、用户、状态、响应时长、返回数据量、错误提示、请求时间等信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681181488919-e8a80df0-7501-4375-96e7-4711246be78a.png#averageHue=%23fefefd&clientId=u8182f372-3d58-4&from=paste&height=493&id=udf3ae706&originHeight=746&originWidth=1577&originalType=binary&ratio=1&rotation=0&showTitle=false&size=91195&status=done&style=none&taskId=uf0b4de78-9187-4138-9459-5a25f1bf267&title=&width=1042)

## 四、数据使用者功能说明
### 1.简易报表
简易报表模块，提供了目前的一些数据统计和分析，具体统计分析数据如下：

- 订阅数量：当前用户订阅的API数量。
- 已通过：表示申请已经通过的API数量。
- 待审核：表示申请等待审核的API数量。
- 调用次数：表示当前申请的AP调用的次数。
- 成功次数：表示当前申请的API调用的次数中成功的次数。
- 失败次数：表示当前申请的API调用的次数中失败的次数。
- 请求成功率：请求成功率=(成功次数/调用次数)* 100%.
- 面积图：表示了最近7天的调用次数、成功次数、失败次数的变化趋势。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681186044149-87a1bb93-fcba-47d9-a826-a1e237a42e81.png#averageHue=%236dcdb0&clientId=u8182f372-3d58-4&from=paste&height=487&id=u359e05f3&originHeight=777&originWidth=1581&originalType=binary&ratio=1&rotation=0&showTitle=false&size=85188&status=done&style=none&taskId=u79923026-84ae-4444-997b-b5d890dc50f&title=&width=991)
### 2.数据管理
数据管理包括了：我的秘钥KEY、我申请的接口、请求日志 三部分。
我的秘钥KEY：
我的秘钥KEY中管理用户的唯一身份认证的APPKEY和用于API参数签名的APPSECRET信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681181898345-05fe4575-e99d-412e-affe-78773f524b20.png#averageHue=%23fefefd&clientId=u8182f372-3d58-4&from=paste&height=172&id=u6426cacf&originHeight=172&originWidth=1081&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12694&status=done&style=none&taskId=uab7d3c57-3dd0-4920-b5c1-fb19e30e676&title=&width=1081)
我申请的接口
其中管理用户当前申请的接口信息，可以查看API的状态和申请状态信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681181948154-10134359-4e5a-486d-9e35-af1dcadca5ef.png#averageHue=%23fefafa&clientId=u8182f372-3d58-4&from=paste&height=382&id=u7017f675&originHeight=382&originWidth=1076&originalType=binary&ratio=1&rotation=0&showTitle=false&size=31333&status=done&style=none&taskId=uf0460cd4-07ae-4a25-8f6a-d5ae7bf10a9&title=&width=1076)
请求日志：
请求日志信息记录了该平台所有对外开放API的请求日志信息，包括URL、环境、API、用户、状态、响应时长、返回数据量、错误提示、请求时间等信息。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681182048054-39998dab-6d16-4b4f-ad5c-1699e83c8bd6.png#averageHue=%23fefdfd&clientId=u8182f372-3d58-4&from=paste&height=419&id=ueae13fe2&originHeight=581&originWidth=1560&originalType=binary&ratio=1&rotation=0&showTitle=false&size=84677&status=done&style=none&taskId=u705446fb-a996-41f4-8a69-2cecc7b86f4&title=&width=1125)
### 3.签名测试
签名测试用于针对自实现的签名进行测试，用于确定自实现的签名算法的正确性。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681182133597-e6038dda-96f3-4ea1-9914-765ab02eb3f8.png#averageHue=%23b7d4b1&clientId=u8182f372-3d58-4&from=paste&height=607&id=uaeed8e5b&originHeight=607&originWidth=1098&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23496&status=done&style=none&taskId=ua3689253-4e5d-4b72-b5d7-c3d80e1da4e&title=&width=1098)
## 五、API开发说明
当我们配置完一个API的基础信息之后，就可以开始对应API的开发，在API列表页面点击 “开发”按钮。即可进行API开发页面；下面是对API开发的说明。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681182621210-aa399a95-2278-440d-a107-d64d26ec4be5.png#averageHue=%23fefefd&clientId=u8182f372-3d58-4&from=paste&height=705&id=u0dad1c83&originHeight=705&originWidth=1123&originalType=binary&ratio=1&rotation=0&showTitle=false&size=67884&status=done&style=none&taskId=u7090252d-46f7-4034-a0d5-bfe64997eb4&title=&width=1123)
### 1.多环境的支持
目前可以同时支持三种环境，分布为test(测试环境)、pre(预发布环境)、prod(生产环境)，可以灵活使用，只要能进行区分就行，多环境数据源的配置，在开发页面的数据源中进行指定。
在该开发页面进行测试时，可以自己指定环境，正式是API调用中通过请求头传递execute-env参数，进行执行环境的确定，不同环境将使用不同的数据源，即完成一个接口支持三个数据源的能力。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681182834178-d56aa060-2db3-4825-8ee4-616e831a8bb1.png#averageHue=%23fefdfd&clientId=u8182f372-3d58-4&from=paste&height=299&id=u8491a959&originHeight=299&originWidth=648&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24447&status=done&style=none&taskId=u5341e29f-97bc-4407-9843-aa3ee401d3b&title=&width=648)
### 2.内置函数
为了更好的进行API逻辑的编写，内部内置了一些用于操作数据源的函数，见内置函数说明。
### 3.多种API语法
API的逻辑书写目前支持三种语法，分别为GROOVY、JAVASCRIPT、SQL，三种语法说明如下：
#### SQL
SQL语法支持标准的SQL语句、以及Mybatis语法，返回的值为标准的JSONARRAY格式，Mybatis语法可以参数：[https://mybatis.org/mybatis-3/zh/dynamic-sql.html](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
标准SQL示例：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681183162749-382cec13-8f4e-4eab-a694-d91347bac92e.png#averageHue=%23fcfcfb&clientId=u8182f372-3d58-4&from=paste&height=519&id=uf9581ca4&originHeight=519&originWidth=1122&originalType=binary&ratio=1&rotation=0&showTitle=false&size=43174&status=done&style=none&taskId=u1c00b943-849f-4e6e-9e59-1d11bd4d77b&title=&width=1122)
Mybatis语句示例：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681183269278-b77520bb-1231-48b5-9827-56fa73bcb719.png#averageHue=%23fcfbfb&clientId=u8182f372-3d58-4&from=paste&height=501&id=u3811417e&originHeight=501&originWidth=1108&originalType=binary&ratio=1&rotation=0&showTitle=false&size=49135&status=done&style=none&taskId=u9e4dfeba-66ad-410b-bdde-46df33c79e7&title=&width=1108)
#### GROOVY
GROOVY通过标准的GROOVY语法进行脚本的编写，通过内置的函数实现脚本中对数据源数据的获取操作，可以使用标准的GROOVY语法对返回值进行操作。groovy语法参考:[https://www.w3cschool.cn/groovy/index.html](https://www.w3cschool.cn/groovy/index.html)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681184175874-31f0e157-f7a9-4fd4-bd96-34d009e09c09.png#averageHue=%23fcfcfb&clientId=u8182f372-3d58-4&from=paste&height=397&id=u139c020e&originHeight=397&originWidth=1035&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36137&status=done&style=none&taskId=u28203e98-3b83-4e08-ae42-13291cea7c8&title=&width=1035)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681184367776-900661ea-dc7c-4107-90d7-a9bc1b68098e.png#averageHue=%23fcfbfa&clientId=u8182f372-3d58-4&from=paste&height=635&id=ub70611d0&originHeight=635&originWidth=1123&originalType=binary&ratio=1&rotation=0&showTitle=false&size=65379&status=done&style=none&taskId=u5e12f4c8-fa99-45c1-999e-a03d78c8047&title=&width=1123)

#### JAVASCRTPT
JAVASCRTPT通过标准的JAVASCRTPT语法进行脚本的编写，通过内置的函数实现脚本中对数据源数据的获取操作，可以使用标准的JAVASCRTPT语法对返回值进行操作。JAVASCRTPT语法参考:[https://www.runoob.com/js/js-tutorial.html](https://www.runoob.com/js/js-tutorial.html)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681184646549-ca57d3a7-1c72-481c-a353-958248c04bcc.png#averageHue=%23fcfbfa&clientId=u8182f372-3d58-4&from=paste&height=560&id=uaf71df29&originHeight=560&originWidth=1108&originalType=binary&ratio=1&rotation=0&showTitle=false&size=64544&status=done&style=none&taskId=uac0c7e05-b138-4099-bd11-0fedb744d16&title=&width=1108)
### 4.自定义返回结构
接口的返回格式支持自定义，首先勾选需要进行自定义，然后可以进行自定义，自定义的语法如下：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/167378/1681184723248-1eab01da-8356-47e8-a840-c9b870735244.png#averageHue=%23fefefc&clientId=u8182f372-3d58-4&from=paste&height=321&id=udf755ad8&originHeight=321&originWidth=585&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11598&status=done&style=none&taskId=ud9d0fe79-a336-4166-9453-1fa40d1e46e&title=&width=585)
```json
@returnCode: 表示接口的返回码
@dataSize：表示返回的数据量
@message：表示接口返回的提示信息
@results：表示返回结果

自定义的结构为JSON，该JSON的值为上面的四种，其key可以进行自定义。

如可以自定义为如下：
{
  "returnCode": "@returnCode",
  "dataSize": "@dataSize",
  "message": "@message",
  "dataList": "@results"
}
```
## 六、内置函数列表
为了更简单的进行开发，系统默认内嵌了一些函数，可以直接在Groovy脚本中使用，提高开发效率。函数列表如下：
### 1.数据库操作类
##### db.count
函数定义 Number db.count(datasourceCode, sql)

- 参数定义：
   - datasourceCode:SQL执行的数据源，类型：STRING， 此参数可以缺失，缺失使用环境数据源。
   - sql：执行的SQL语句，类型：STRING，此参数必填
- 返回类型：Number
- 作用：执行数据库的count语句，返回统计结果
```groovy
def dataCount = db.count('select count(1) from table')  =1
def dataCount = db.count('mysql_001','select count(1) from table')  =2
```
##### db.selectOne
函数定义 Object db.selectone(datasourceCode, sql)

- 参数定义：
   - datasourceCode:SQL执行的数据源，类型：STRING， 此参数可以缺失，缺失使用环境数据源。
   - sql：执行的SQL语句，类型：STRING，此参数必填
- 返回类型：Number
- 作用：执行数据库的select语句，返回一条结果，请保障该SQL只返回一条数据。
```groovy
def dataCount = db.count('select name from table')  = {'name': 'Jack'}
def dataCount = db.count('mysql_001','select name from table')  = {'name': 'Jack'}
```
##### db.selectList
函数定义 Array<Object> db.selectList(datasourceCode, sql)

- 参数定义：
   - datasourceCode:SQL执行的数据源，类型：STRING， 此参数可以缺失，缺失使用环境数据源。
   - sql：执行的SQL语句，类型：STRING，此参数必填
- 返回类型：Number
- 作用：执行数据库的select语句，返回多条数据结构
```groovy
def dataCount = db.count('select name from table')  = [{'name': 'Jack'},{'name': 'Tom'}]
def dataCount = db.count('mysql_001','select name from table')  = [{'name': 'Jack'}]
```
### 2.ELASTICSEARCH操作类
##### es.search
函数定义 Object es.search(datasourceCode, index, dsl)

- 参数定义：
   - datasourceCode:SQL执行的数据源，类型：STRING， 此参数可以缺失，缺失使用环境数据源。
   - index：ElasticSearch索引名称，类型：STRING，此参数必填
   - dsl：ElasticSearch的查询DSL语句，类型STRING，此参数必填
- 返回类型：Object
- 作用：执行ElasticSearch查询，返回查询结果
```groovy
def esHits = es.search('myIndex', '{"query": {"match_all": {}}}')

def esHits = es.search('elasticsearch_001', 'myIndex', '{"query": {"match_all": {}}}')
```
### 3.执行环境类
##### env.isTest
函数定义 boolean env.isTest()

- 参数定义：五
- 返回类型：boolean
- 作用：判断当前环境是否为test环境，是返回true，否返回false
```groovy
def isTest = env.isTest()  =  false
```
##### env.isPre
函数定义 boolean env.isPre()

- 参数定义：无
- 返回类型：boolean
- 作用：判断当前环境是否为pre环境，是返回true，否返回false
```groovy
def isPre = env.isPre()  =  false
```
##### env.isProd
函数定义 boolean env.isProd()

- 参数定义：无
- 返回类型：boolean
- 作用：判断当前环境是否为prod环境，是返回true，否返回false
```groovy
def isProd = env.isProd()  =  true
```
##### env.getExecuteEnv
函数定义 String env.getExecuteEnv()

- 参数定义：无
- 返回类型：String
- 作用：获取当前的执行环境，测试返回test，预发布返回pre，正式返回prod
```groovy
def env = env.getExecuteEnv()  =  "test"
```
### 4.GIS工具函数类
##### GeoUtils.toPolygonGeoJson
函数定义 Object GeoUtils.toPolygonGeoJson(dataList, coordinatesFiled, geometryType,removeCoordinateFromFeatures)

- 参数定义：
   - dataList：要进行转换的数据list，此参数必填，类型：Array
   - coordinatesFiled：表示地理位置信息的字段名称，类型：STRING。如果为coordinates则可以不填。
   - geometryType：要返回的图形类型，此参数必填，类型：STRING
   - removeCoordinateFromFeatures：是否将地理信息数据从properties中删除。类型：boolean
- 返回类型：Object
- 作用：将数据库查询结果转换为GeoJson格式。不适用于Point类型。
```groovy
# 支持如下三种调用方式
def geoJson = GeoUtils.toPolygonGeoJson(dataList, geometryTyp)
def geoJson = GeoUtils.toPolygonGeoJson([{"coordinates", "[[128.00,21.00],[129.23,23,45]]"}], 'line')

def geoJson = GeoUtils.toPolygonGeoJson(dataList, coordinatesFiled, geometryTyp)
def geoJson = GeoUtils.toPolygonGeoJson([{"coord", "[[128.00,21.00],[129.23,23,45]]"}],'coord', 'line')

def geoJson = GeoUtils.toPolygonGeoJson(dataList, coordinatesFiled, geometryTyp,removeCoordinateFromFeatures)
def geoJson = GeoUtils.toPolygonGeoJson([{"coord", "[[128.00,21.00],[129.23,23,45]]"}],'coord', 'line', true)
```
##### GeoUtils.toPointGeoJson
函数定义 Object GeoUtils.toPointGeoJson(dataList, longitudeField, latitudeField,removeCoordinateFromFeatures)

- 参数定义：
   - dataList：要进行转换的数据list，此参数必填，类型：Array
   - longitudeField：表示经度信息的字段名称，类型：STRING。如果为longitude则可以不填。
   - latitudeField：表示维度信息的字段名称，类型：STRING。如果为latitude则可以不填。
   - removeCoordinateFromFeatures：是否将地理信息数据从properties中删除。类型：boolean
- 返回类型：Object
- 作用：将数据库查询结果转换为GeoJson格式。只适用于Point类型。
```groovy
# 支持如下三种调用方式
def geoJson = GeoUtils.toPointGeoJson(dataList)
def geoJson = GeoUtils.toPolygonGeoJson([{"longitude":128.00,"latitude":21.00]"}])

def geoJson = GeoUtils.toPointGeoJson(dataList, removeCoordinateFromFeatures)
def geoJson = GeoUtils.toPolygonGeoJson([{"longitude":128.00,"latitude":21.00]"}],true)

def geoJson = GeoUtils.toPointGeoJson(dataList, longitudeField, latitudeField,removeCoordinateFromFeatures)
def geoJson = GeoUtils.toPolygonGeoJson([{"lon":128.00,"lat":21.00]"}],'lon', 'lat', true)
```
