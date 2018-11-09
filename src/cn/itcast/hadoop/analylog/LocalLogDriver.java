package cn.itcast.hadoop.analylog;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import cn.itcast.hadoop.mapreduce.flowsum.FlowSumDriver;

public class LocalLogDriver {

	private static Logger logger = Logger.getLogger(FlowSumDriver.class);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		logger.info("starting .... ");
		
		//通过job封装本次Mr的相关信息
		Configuration configuration = new Configuration();
		//设置本地运行模式
		configuration.set("mapreduce.framework.name", "local");
		
		Job job = Job.getInstance();
		//指定本次mr job jar 运行的主类
		job.setJarByClass(LocalLogDriver.class);
		
		//指定本次mr 所用的mapper reducer类分别是什么
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		//指定本次mapper阶段的输出 k v 类型
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		//指定本次mr 所用reducer 阶段输出k v 类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		//指定mr 输入数据路径 输出数据路径
		FileInputFormat.setInputPaths(job, "D:/studyPro/studyhadoop/log/input");
		FileOutputFormat.setOutputPath(job, new Path("D:/studyPro/studyhadoop/log/output"));
		
		// 提交程序，并监控和打印程勋执行情况
		boolean flag = job.waitForCompletion(true);
		//退出程序
		System.exit(flag ? 0:1);
		
	}
	
}
