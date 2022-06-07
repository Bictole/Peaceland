package saveaggregate

//import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import org.apache.spark.sql.SparkSession
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
  def putOnS3() = {
    try{

      // find these info on IAM
      val accessKey = "AKIAS5I4RNJFG2HX6DUU"
      val secretKey = "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"

      val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("save_aggregate")
          .getOrCreate()
      //spark.sparkContext.setLogLevel("ERROR")

      spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

      //val filePath = "/home/arcanix/Downloads/addresses.csv"
      //val filePath = "s3a://peaceland/addresses.csv"
      val filePath = "/home/alexandrel/Peaceland/save_aggregate/test.json"
      val fSource = Source.fromFile(filePath)
      val jsonRaw = fSource.getLines.mkString
      fSource.close()
      val df = spark.read.option("multiLine", true).json(filePath)
      df.show(false)
      df.write.format("json").option("header", true).mode("Overwrite").save("s3a://peaceland-avem/test.json")
      /**val content = spark.read
        .option("header", "true")
        .option("inferSchema", "true")
        .csv(filePath)

      */

      //content.show(5, false)
    } catch {
      case ex1: FileNotFoundException => println("file not found")
      case ex2: IOException => println("IO Exception")
      case ex3: Exception => println("Not sure")
    }
  }
  
  putOnS3()
  //println("Hello, World!")
}