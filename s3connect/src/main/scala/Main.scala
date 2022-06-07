package s3connect

import java.io.ByteArrayInputStream

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectMetadata
import org.apache.commons.io.IOUtils
import java.util.Base64

object S3Connect extends App {

  val accessKey = "AKIAQTIILA2TO7KHPMTJ"
  val secretKey = "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"

  // s3 client with minio endpoint
  val s3Credential = new BasicAWSCredentials(accessKey, secretKey)
  val s3Client = AmazonS3ClientBuilder
    .standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("s3.amazonaws.com", "us-east-1"))
    .withPathStyleAccessEnabled(true)
    .withCredentials(new AWSStaticCredentialsProvider(s3Credential))
    .build()

  /**
    * Put object into minio storage
    *
    * @param bucket bucket name
    * @param id     object id
    * @param blob   object blob
    */
  def put(bucket: String, id: String, blob: Array[Byte]): Unit = {
    // create bucket if not exists
    if (!s3Client.doesBucketExistV2(bucket)) {
      s3Client.createBucket(bucket)
    }

    // list buckets
    println(s3Client.listBuckets())

    // put object
    val bais = new ByteArrayInputStream(blob)
    val metadata = new ObjectMetadata()
    metadata.setContentLength(bais.available())
    s3Client.putObject(bucket, id, bais, metadata)

    bais.close()
  }

  /**
    * Get object from minio storage
    *
    * @param bucket bucket name
    * @param id     object it
    */
  def get(bucket: String, id: String): Unit = {
    // get object as byte array
    val obj = s3Client.getObject(bucket, id)
    val blob = IOUtils.toByteArray(obj.getObjectContent)
    println(blob.length)

    // get object stat
    println(obj.getBucketName)
    println(obj.getObjectMetadata.getContentLength)
  }

  /**
    * remove object from minio storage
    *
    * @param bucket bucket name
    * @param id     object it
    */
  def delete(bucket: String, id: String): Unit = {
    // remove object
    s3Client.deleteObject(bucket, id)

    // get size of the bucket
    println(List(s3Client.listObjects(bucket)).size)
  }

  // create blob as a stream with base64 encoded string
  val payload = "Z290IG5vdGhpbmcgbXVjaCB0byBzYXksIHNvIGhlbGxv"
  val blob = Base64.getDecoder().decode(payload)

  // put object
  put("arcatest1", "0122", blob)

  // get object
  get("arcatest1", "0122")

  // delete object
  delete("arcatest1", "0122")


  // put object to sub folder
  put("arc000", "labs/3422", blob)

  // get object in sub folder
  get("arc000", "labs/3422")

  // delete object in sub folder
  delete("arc000", "labs/3422")

}