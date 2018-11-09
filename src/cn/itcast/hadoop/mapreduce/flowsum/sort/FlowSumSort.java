package cn.itcast.hadoop.mapreduce.flowsum.sort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import cn.itcast.hadoop.mapreduce.flowsum.FlowBean;
import cn.itcast.hadoop.mapreduce.flowsum.FlowSumDriver;

/**
 * 按照总流量倒序输出
 * 1.重写compareTo方法 （compareTo：默认正序）
 * 
 * Title: FlowSumSort.java
 * Description: 
 * @author chengge
 * @date 2018年8月26日
 *
 */
public class FlowSumSort {
	
	public static class FlowSumSortMapper extends Mapper<LongWritable, Text, FlowBean, Text>{
		Text v = new Text();
		FlowBean k = new FlowBean();
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowBean, Text>.Context context)
				throws IOException, InterruptedException {
			
			String line = value.toString();
			
			String[] fields = line.split("\t");
			
			String phoneNum = fields[0];
			
			long upFlow = Long.parseLong(fields[1]);
			
			long downFlow = Long.parseLong(fields[2]);
			
			v.set(phoneNum);
			k.set(upFlow, downFlow);
			
			context.write(k, v);
			
		}
		
	}
	
	public static class FlowSumSortReducer extends Reducer<FlowBean, Text, Text, FlowBean>{

		@Override
		protected void reduce(FlowBean key, Iterable<Text> values, Reducer<FlowBean, Text, Text, FlowBean>.Context context)
				throws IOException, InterruptedException {
			
			context.write(values.iterator().next(), key);
			
		}
	}

	private static Logger logger = Logger.getLogger(FlowSumDriver.class);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		logger.info("FlowSumSort starting ...");
		
		// 通过job来封装本次mr的相关信息
		Configuration conf = new Configuration();
		// 设置本地运行模式
		conf.set("mapreduce.framework.name", "local");

		Job job = Job.getInstance();

		// 指定本次mr job jar运行的主类
		job.setJarByClass(FlowSumSort.class);

		// 指定本次mr 所用的mapper reducer类分别是什么
		job.setMapperClass(FlowSumSortMapper.class);
		job.setReducerClass(FlowSumSortReducer.class);

		// 指定本次mr mapper阶段的输出 k v 类型
		job.setMapOutputKeyClass(FlowBean.class);
		job.setMapOutputValueClass(Text.class);

		// 指定本次mr 最终输出的k v 类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);

		// job.setNumReduceTasks(2);

		// 指定mr 输入的数据路径，和最终输出结果存放在什么位置
		FileInputFormat.setInputPaths(job, "D:/studyPro/studyhadoop/flowsum/output");
		FileOutputFormat.setOutputPath(job, new Path("D:/studyPro/studyhadoop/flowsumsort/output"));

		// 提交
		// job.submit();

		// 提交程序，并监控和打印程序执行情况
		boolean flag = job.waitForCompletion(true);
		// 退出程序
		System.exit(flag ? 0 : 1);
		logger.info("ending....");
	}
}
