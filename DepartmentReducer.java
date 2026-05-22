import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DepartmentReducer extends Reducer<Text, Text, Text, Text> {
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long total = 0;
        long count = 0;
        for (Text val : values) {
            long salary = Long.parseLong(val.toString());
            if (salary > 0) {
                total += salary;
                count++;
            }
        }
        if (count > 0) {
            long avg = total / count;
            context.write(key, new Text("Total: " + total + "   Avg: " + avg + "   Employees: " + count));
        }
    }
}