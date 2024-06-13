# AIGC辅助老年人认知训练

1. ### 下载源码

   

   ```
   yum -y group install "Development Tools"
   ```

   

   ```
   git clone https://github.com/Robin-Jason/CognitiveTrainingForElderly.git
   cd CognitiveTrainingForElderly
   ```

   

   ### **构建Jar包文件**

   

   创建一个存放`Maven仓库`的卷,使得Maven的依赖被缓存，可加快构建速度。

   ```
   docker volume create --name CognitiveTraining-maven-repo
   ```

   

   使用 Maven Docker 镜像来编译和打包 Spring Boot 应用。

   `pom.xml`文件中增加镜像地址

   ```
     <repositories>
   		<repository>
   			<id>huaweicloud</id>
   			<name>huawei</name>
   			<url>https://mirrors.huaweicloud.com/repository/maven/</url>
   		</repository>
   		<repository>
   			<id>aliyunmaven</id>
   			<name>aliyun</name>
   			<url>https://maven.aliyun.com/repository/public</url>
   		</repository>
   	</repositories>
   	<pluginRepositories>
   		<pluginRepository>
   			<id>public</id>
   			<name>aliyun nexus</name>
   			<url>https://maven.aliyun.com/nexus/content/groups/public/</url>
   			<releases>
   				<enabled>true</enabled>
   			</releases>
   			<snapshots>
   				<enabled>false</enabled>
   			</snapshots>
   		</pluginRepository>
   	</pluginRepositories>
   ```

   

   ```
   docker run -it --rm --name CognitiveTraining-maven \
       -v CognitiveTraining-maven-repo:/root/.m2 \
       -v "$PWD/src/springboot-server":/usr/src/mymaven \
       -w /usr/src/mymaven \
       maven:3.8.4-jdk-11 mvn clean install package -e -Dmaven.test.skip=true
   ```
