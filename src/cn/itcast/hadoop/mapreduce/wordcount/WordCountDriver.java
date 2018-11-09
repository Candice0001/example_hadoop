package cn.itcast.hadoop.mapreduce.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 这个类就是mr程序运行时候的主类，本类中组装了一些程勋运行时候所需要的信息
 * 比如：使用的是哪一个mpper类满一个reducer类，输入数据在哪里，输出数据在什么地方
 * 
 * 
 * Title: WordCountDriver.java
 * Description: 
 * @author chengge
 * @date 2018年8月20日
 *
 */
public class WordCountDriver {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//通过job来封装本次mr的相关信息
		Configuration conf = new Configuration();
		//设置本地运行模式
		conf.set("mapreduce.framework.name", "local");
		
		Job job = Job.getInstance();
		
		//指定本次mr job jar运行的主类
		job.setJarByClass(WordCountDriver.class);
		
		//指定本次mr 所用的mapper reducer类分别是什么
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		//指定本次mr mapper阶段的输出 k v 类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		//指定本次mr 最终输出的k v 类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		/**
		 * reducetask个数和最终输出文件的个数（文件被分成几部分）有关系 对等
		 * 默认：只有一个reduceTask  part-r-0000
		 * 
		 * 若手动改变了reduceNum的值:
		 *  job.setNumReduceTasks(N) 输出结果文件就被分成N份
		 *  
		 * 分区规则：
		 *  默认的分区规则：根据mapper输出<key,value>中的key的        key.hashcode % reduceNum   --- key 的哈希取模 
		 * 
		 */
		//job.setNumReduceTasks(1);
		
		/**
		 * combiner与reducer的业务逻辑一样，combiner组件不能影响结果
		 * combiner和reducer都是聚合处理，前者是局部聚合，后者是全局
		 */
		//job.setCombinerClass(WordCountReducer.class);
		
		//指定mr 输入的数据路径，和最终输出结果存放在什么位置
		FileInputFormat.setInputPaths(job, "hdfs://localhost:9000/user/hdfs1/");
		FileOutputFormat.setOutputPath(job, new Path("D:/studyPro/studyhadoop/output1/"));
		
		//提交
		//job.submit();
		
		//提交程序，并监控和打印程序执行情况
		boolean flag = job.waitForCompletion(true);
		//退出程序
		System.exit( flag ? 0:1);
		
		
	}
	
	/**
	 * Mapper任务执行流程
	 * 
	 * 逻辑切片对待处理的任务进行任务划分
	 * 
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 一：集群运行模式
	 * 1.将 mr 程序交给yarn集群，yarn ：用来做资源调度的；
	 * 
	 * 二：本地运行模式：
	 * 	1. 需要本地安装hadoop,配置hadoop环境。
	 * 	2.本地运行模式便于调试分布式应用程序；
	 */
}
