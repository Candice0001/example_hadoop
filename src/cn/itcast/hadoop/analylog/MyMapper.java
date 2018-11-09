package cn.itcast.hadoop.analylog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

	LogParser logParser = new LogParser();

	Text outputValue = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
		String[] parsed = logParser.parse(value.toString());// 把行文本解析成数组
		
		// 1 过滤掉静态资源
		if (parsed[2].startsWith("GET /static/") || parsed[2].startsWith("GET /uc_server")) {
			
			return;
		}
		
		// 2. 过滤掉开头的指定字符串
		if (parsed[2].endsWith("GET /")) {
			parsed[2] = parsed[2].substring("GET /".length());
		}else if (parsed[2].startsWith("POST /")) {
			parsed[2] = parsed[2].substring("POST /".length());
		}
		
		//3. 过滤掉结尾特定字符串
		if (parsed[2].endsWith(" HTTP/1.1")) {
			parsed[2] = parsed[2].substring(0, parsed[2].length() - " HTTP/1.1".length());
		}
		
		// step4.只写入前三个记录类型项
        outputValue.set(parsed[0] + "\t" + parsed[1] + "\t" + parsed[2]);
        context.write(key, outputValue); //返回<行号,处理后的文本数据>
		
	}

	

}
