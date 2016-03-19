package api

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import util.EncryptionProvider

/**
  * Created by drashko on 18.03.16.
  */

@RunWith(classOf[JUnitRunner])
class EncryptionproviderTest extends FunSuite {

  val secret = "secret"

  test("Base64") {
    val encResult = new String(java.util.Base64.getEncoder.encode("Base64".getBytes("UTF-8")))
    println(encResult)

    val decoded = new String(java.util.Base64.getDecoder.decode(encResult.getBytes))
    println(decoded)

    assertResult("Base64")(decoded)
  }

  test("Encryption works") {
    val expected = """ {"t":"p","tst":1458303335,"acc":0,"_type":"location","alt":0,"lon":25.7556201238425,"lat":50.4345742660113,"batt":62,"tid":"SL"} """

    val encResult = EncryptionProvider.encrypt(expected, secret)
    println(encResult)

    val decResult = EncryptionProvider.decrypt(encResult, secret)
    println(decResult)

    assertResult(expected)(decResult)
  }
}
