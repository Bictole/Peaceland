package saveaggregate

//import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SaveMode 
import play.api.libs.json._
import java.io.FileNotFoundException
import java.io.IOException
import scala.io.Source
/*import play.api.libs.json._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}*/

object Main extends App {
  def experiment() = {
      //val filePath = "s3a://arcatest0/test.json"
      //val filePath = "s3a://peaceland/addresses.csv"
      //val filePath = "/home/alexandrel/Peaceland/save_aggregate/test.json"

      val filePath = "/home/arcanix/school/spark/Peaceland/save_aggregate/test.json"

      // find these info on IAM
      val accessKey = "AKIAQTIILA2TLU7Y64XW"
      val secretKey = "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"
      val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("save_aggregate")
          .getOrCreate()

      spark.sparkContext.setLogLevel("ERROR")

      spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

      val df = spark.read.option("multiLine", true).json(filePath)
      df.show(false)

      putOnS3(df, "s3a://arcatest0/test.csv")
     val content = getFromS3(spark, filePath)

      content.show(5, false)
  }

  def getFromS3(configuredSpark: SparkSession, inAddress: String) = {
    configuredSpark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .json("s3a://arcatest0/test.json")
  }

  def putOnS3(df: DataFrame, outAddress: String) = {
    df.write.mode(SaveMode.Overwrite).parquet(outAddress)
  }
  
  experiment()
  //println("Hello, World!")
}