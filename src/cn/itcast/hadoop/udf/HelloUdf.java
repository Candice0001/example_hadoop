package cn.itcast.hadoop.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * hive UDF 开发
 * 开发自己的hql 函数
 * 
 * 		需要 hadoop-common-2.8.4.jar ，hive-exec-1.2.1.jar两个jar包
 * 		1. 需要继承UDF,execute函数内部编写自己的逻辑代码：
 * 		2. 打包成jar包
 * 		3. 启动hive服务器，必须是在 ${hive_home}/bin 下启动hive（运行 hive 命令）,进入hive命令行
 * 		4. 把 jar包添加到hive的classpath ：  hive>add JAR /home/hadoop/Documents/myUdf.jar;
 * 		5. 创建临时函数，与开发好的 Java class 关联；  create temporary function myUpper as 'cn.itcast.hadoop.udf.HelloUdf';
 * 		6. 现在即可值hive中使用自定义的hive函数了；  select myUpper('asd'); select t_t1.id,myUpper(t_t1.desc) from t_t1;
 * 	说明：该临时函数只在当前hive实例中可用 
 * 
 * Title: HelloUdf.java
 * Description: 
 * @author chengge
 * @date 2018年9月27日
 *
 */
public class HelloUdf extends UDF {

	//实现我们自己的业务逻辑函数
	public String evaluate(String str) {
		if (str == null) {
			return null;
		}else {
			return str.toUpperCase();
		}
	}
	
	public static void main(String[] args) {
		String ss = new HelloUdf().evaluate("Abc");
		System.out.println(ss);
	}
	//

}
