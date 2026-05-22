
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

class DepartmentPartitioner extends Partitioner<Text, Text> {
    public int getPartition(Text key, Text value, int numPartitions) {
        String dept = key.toString();
        if (dept.equals("IT")) return 0;
        if (dept.equals("HR")) return 1;
        if (dept.equals("Sales")) return 2;
        if (dept.equals("Finance")) return 3;
        return 4;
    }
}

public class DepartmentDriver {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: DepartmentDriver <input path> <output path>");
            System.exit(1);
        }
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Department Salary WITH Combiner");
        
        job.setJarByClass(DepartmentDriver.class);
        job.setMapperClass(DepartmentMapper.class);
        job.setCombinerClass(DepartmentReducer.class);  
        job.setReducerClass(DepartmentReducer.class);
        job.setPartitionerClass(DepartmentPartitioner.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(4);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
