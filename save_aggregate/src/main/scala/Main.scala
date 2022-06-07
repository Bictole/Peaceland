package saveaggregate

//import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import org.apache.spark.sql.SparkSession
import java.io.FileNotFoundException
import java.io.IOException

/*import play.api.libs.json._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}*/


object Main extends App {
  def putOnS3() = {
    try {
      // find these info on IAM
      val accessKey = "AKIAQTIILA2TO7KHPMTJ"
      val secretKey = "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"

      val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("save_aggregate")
          .getOrCreate()
      spark.sparkContext.setLogLevel("ERROR")

      spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
      spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")

      //val filePath = "/home/arcanix/Downloads/addresses.csv"
      val filePath = "s3a://arcatest0/addresses.csv"

      val content = spark.read
        .option("header", "true")
        .option("inferSchema", "true")
        .csv(filePath)

      content.show(5, false)
    } catch {
      case ex1: FileNotFoundException => println("file not found")
      case ex2: IOException => println("IO Exception")
      case ex3: Exception => println("Not sure")
    }

  }
  
  putOnS3()
  println("Hello, World!")
}