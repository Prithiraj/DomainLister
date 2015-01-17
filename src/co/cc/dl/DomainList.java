package co.cc.dl;


//import java.util.logging.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import edu.cmu.lemurproject.WarcFileInputFormat;



public class DomainList extends Configured implements Tool{

	/**
	 * Contains Amazon S3 bucket holding the commoncrawl data
	 */
	private static final String CC_BUCKET="aws-publicdatasets";
	
	//private static final Logger LOG = Logger.getLogger(DomainList.class);
	/*private static class CSVOutputFormat extends TextOutputFormat<Text, LongWritable>{
		public RecordWriter<Text, LongWritable> getRecordWriter(FileSystem ignored, JobConf job, String name, Progressable progress)
		throws IOException{
			
			Path file = FileOutputFormat.getTaskOutputPath(job, name);
			FileSystem fs= file.getFileSystem(job);
			FSDataOutputStream fileOut=fs.create(file, progress);
			return new CSVRecordWriter(fileOut);
			
		}
	}
	protected static class CSVRecordWriter 
	implements RecordWriter<Text, LongWritable>{
		protected DataOutputStream outStream;
		
		public CSVRecordWriter(DataOutputStream out){
			this.outStream = out;
		}

		public void close(Reporter arg0) throws IOException {
			// TODO Auto-generated method stub
			outStream.close();
		}

		public synchronized void write(Text key, LongWritable value) throws IOException {
			// TODO Auto-generated method stub
			CsvRecordOutput csvOutput = new CsvRecordOutput(outStream);
			csvOutput.writeString(key.toString(), "word");
			csvOutput.writeLong(value.get(),"occurences");
		}
	}*/
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new DomainList(), args);
		System.exit(res);
	}
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String AccessKey = args[0]; //1
		String SecretKey = args[1]; //2
		String InputPath = args[2]; //3
		String OutputPath = args[3]; //4
		
		Configuration conf = getConf();
		conf.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem");
		conf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem");
		conf.set("fs.s3.awsAccessKeyId",AccessKey);
		conf.set("fs.s3.awsSecretAccessKey",SecretKey);
		
		JobConf job = new JobConf(conf, DomainList.class);
		job.setJarByClass(getClass());
		job.setNumReduceTasks(1);
		
		//Path in = new Path(args[2]);
		//Path out = new Path(args[3]);
		
		// Specify job specific parameters
		// Specify various job-specific parameters     
        job.setJobName("my-app");
        FileInputFormat.setInputPaths(job, new Path(InputPath));
        FileOutputFormat.setOutputPath(job, new Path(OutputPath));
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        
        job.setInputFormat(WarcFileInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        
        job.setMapperClass(DomainListMapper.class);
        job.setCombinerClass(DomainListReducer.class);
        job.setReducerClass(DomainListReducer.class);
		
		return 0;
	}

}