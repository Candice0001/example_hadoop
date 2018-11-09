package cn.itcast.hadoop.mapreduce.flowsum;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 在mr程序中也可以使用自定义的类型，而前提是需要实现hadoop的序列化机制
 * 
 * Title: FlowSumMapper.java
 * Description: 
 * @author chengge
 * @date 2018年8月24日
 *
 */

public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

	Text k = new Text();
	FlowBean v = new FlowBean();
	
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {
		
		String line = value.toString();//一行一行读取的
		System.out.println("line:"+line);
		
		String[] fields = line.split(", ");//返回的是字段的数组,注意分隔符
		
		String phonenum = fields[1];
		
		long upFlow = Long.parseLong(fields[fields.length-3]);
		
		long downFlow = Long.parseLong(fields[fields.length-2]);
		
		k.set(phonenum);
		v.set(upFlow, downFlow);
		
		//context.write(new Text(phonenum),new FlowBean(upFlow,downFlow));
		context.write(k,v);
		
	}
	
	

}
