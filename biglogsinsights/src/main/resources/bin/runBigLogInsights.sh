base_dir=/home/milap/Documents/Projects/biglogsinsights/target/big-logs-insights-1.0-SNAPSHOT-bin/big-logs-insights-1.0-SNAPSHOT/
echo "path: "${base_dir}lib/big-logs-insights.jar
~/Downloads/spark-3.0.0-preview-bin-hadoop2.7/bin/spark-submit \
  --class com.projects.biglogs.LogTracker \
  --master local[2] \
  --jars ${base_dir}lib/scopt_2.12.jar \
  ${base_dir}lib/big-logs-insights.jar \
  -p ${base_dir}input -d 2018-12-09

  #/home/milap/Documents/Projects/biglogsinsights/target/big-logs-insights-1.0-SNAPSHOT-full.jar \  #-p /home/milap/Documents/Projects/biglogsinsights/src/main/resources -d 2018-12-09