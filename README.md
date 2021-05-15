# xxnursery
新星幼儿园-招聘网站

网站主题：本网站是以在线招聘为主题的幼儿园教师招聘网站

选题背景：1.由于人口城镇化，大量人口向城市聚拢，造成城市人口膨胀，新生儿增多，入园新生增多，造成幼儿园教师工作量增大，需要新的职工加入。 
2.国内大型招聘平台越来越多 。招聘平台不能更好的的展示幼儿园的文化和招聘信息。

######## 环境依赖
    jdk1.8.0_31
    apache-maven-3.5.2
    idea 2020.3
    git

######## 相关技术
    编辑语言：java、HTML5
    后台框架：SpringBoot、SpringMVC、Mybatis、SpringSecurity、log4j2
    前端框架：bootstrap、jquery、fileinput、markdown、datatable

######## 系统整体架构
    浏览器 -> 表现层 -> 业务逻辑层 -> 数据访问层 - 浏览器
    表现层： nursery-manage2、nursery-web
    业务逻辑层：  nursery-service-impl 
    数据访问层：  nursery-dao 

######## 项目主模块、项目启动说明
    nursery-manage2 后台管理模块 选中NurseryManage2Application类，启动main方法
    nursery-web     前端模块    选中SpringBootApplicationWeb类，启动main方法

######## 项目子模块、依赖
    nursery-iservice        定义了接口模块
    nursery-service-impl    定义了业务逻辑实现
    nursery-dao             数据持久层
    nursery-common          code参数、model数据
    nursery-beans           对象类（参数对象、数据库表、返回值）

######## mysql数据库配置信息
    数据库文件 nursery.sql
    datasource.driver-class-name=com.mysql.jdbc.Driver
    datasource.password=账号
    datasource.username=密码
    datasource.url=jdbc:mysql://localhost:3306/nursery?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false

######## 日志文件
    logging.config=classpath:log4j2-spring.xml
    配置储存路径
        <property name="FILE_PATH" value="D://log" />
        <property name="FILE_NAME" value="nursery-web" />

######## 文件存储路径
    在主项目中 config/WebMvcConfig中配置虚拟路径对应。
        上传图片
        /upload/img/**          file:D:\\IdeaProjects\\git\\xxnursery\\xxnurseryimg\\upload\\
        /upload/cover/img/**    file:D:\\IdeaProjects\\git\\xxnursery\\xxnurseryimg\\upload\\cover\\
        /upload/user/image/**   file:D:\\IdeaProjects\\git\\xxnursery\\xxnurseryimg\\user\\image\\
        上传word文件
        /upload/word/**         file:D:\\IdeaProjects\\git\\xxnursery\\xxnurseryword\\upload\\

######## 用户登录配置文件
    在主项目中 config/SecurityConfig中配置登录用户的属性信息，和权限信息

######## 用户全选、登录密码
    名称       | 账户         | 密码
    权限管理员：| admin_root  | admin
    普通管理员：| sdfsdf22551 | 123456Msq./
    普通用户：  | yonghu      | Msq123456.
    
