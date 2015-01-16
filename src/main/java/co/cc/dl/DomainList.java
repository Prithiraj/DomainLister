package co.cc.dl;


import java.util.logging.*;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.record.CsvRecordOutput;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.jets3t.service.Jets3tProperties;
import org.jets3t.service.impl.rest.httpclient.JetS3tRequestAuthorizer;

import edu.cmu.lemurproject.WarcFileInputFormat;


public class DomainList extends Configured implements Tool{

	/**
	 * Contains Amazon S3 bucket holding the commoncrawl data
	 */
	private static final String CC_BUCKET="aws-publicdatasets";
	//private static final Logger LOG = Logger.getLogger(DomainList.class);
	private static class CSVOutputFormat extends TextOutputFormat<Text, LongWritable>{
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
	}
	public static void main(String[] args) {
		int res = ToolRunner.run(new Configuration(), new DomainList(), args);
		System.exit(res);
	}
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		conf.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem");
		conf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem");
		conf.set("fs.s3.awsAccessKeyId",args[0]);
		conf.set("fs.s3.awsSecretAccessKey",args[1]);
		
		return 0;
	}

}
