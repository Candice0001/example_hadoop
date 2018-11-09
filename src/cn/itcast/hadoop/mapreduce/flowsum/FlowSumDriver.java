package cn.itcast.hadoop.mapreduce.flowsum;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

/**
 * 统计每个手机号的上行总流量，下行总流量，总流量 
 * 
 * Title: FlowSumDriver.java
 * Description: 
 * @author chengge
 * @date 2018年8月26日
 *
 */
public class FlowSumDriver {
	
	private static Logger logger = Logger.getLogger(FlowSumDriver.class);

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		logger.info("starting....");
		
		// 通过job来封装本次mr的相关信息
		Configuration conf = new Configuration();
		// 设置本地运行模式
		conf.set("mapreduce.framework.name", "local");

		Job job = Job.getInstance();

		// 指定本次mr job jar运行的主类
		job.setJarByClass(FlowSumDriver.class);

		// 指定本次mr 所用的mapper reducer类分别是什么
		job.setMapperClass(FlowSumMapper.class);
		job.setReducerClass(FlowSumReducer.class);

		// 指定本次mr mapper阶段的输出 k v 类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		// 指定本次mr 最终输出的k v 类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);

		// job.setNumReduceTasks(2);

		// 指定mr 输入的数据路径，和最终输出结果存放在什么位置
		FileInputFormat.setInputPaths(job, "D:/studyPro/studyhadoop/flowsum/input");
		FileOutputFormat.setOutputPath(job, new Path("D:/studyPro/studyhadoop/flowsum/output"));

		// 提交
		// job.submit();

		// 提交程序，并监控和打印程序执行情况
		boolean flag = job.waitForCompletion(true);
		// 退出程序
		System.exit(flag ? 0 : 1);
		logger.info("ending....");
	}

}
