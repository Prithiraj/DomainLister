package co.cc.dl;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.archive.io.ArchiveReader;

public class DomainListMapper extends MapReduceBase 
	implements Mapper<Text, ArchiveReader, Text, LongWritable>{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public void map(Text arg0, ArchiveReader ar,
			OutputCollector<Text, LongWritable> output, Reporter rep)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
