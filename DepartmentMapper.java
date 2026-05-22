import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DepartmentMapper extends Mapper<LongWritable, Text, Text, Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();
    
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (line.startsWith("employee_id")) return;
        String[] parts = line.split(",");
        if (parts.length < 3) return;
        String department = parts[1].trim();
        long salary = Long.parseLong(parts[2].trim());
        if (salary > 0) {
            outKey.set(department);
            outValue.set(String.valueOf(salary));
            context.write(outKey, outValue);
        }
    }
}