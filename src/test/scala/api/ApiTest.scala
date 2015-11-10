
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import api.{Api, MqttApi}
import org.scalatest.FunSuite

class ApiTest extends FunSuite {

  val now = "22101521315800"

  test("API test date format") {
    val cal: Calendar = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_MONTH, 22);
    cal.set(Calendar.MONTH, 9);
    cal.set(Calendar.YEAR, 2015);
    cal.set(Calendar.HOUR_OF_DAY, 21);
    cal.set(Calendar.MINUTE, 31);
    cal.set(Calendar.SECOND, 58);
    cal.set(Calendar.MILLISECOND, 0);

    assertResult(now)(Api.getCurrentDate(cal.getTime()))

    cal.set(Calendar.HOUR_OF_DAY, 9);
    assertResult("22101509315800")(Api.getCurrentDate(cal.getTime()))
  }

  test("API string should match expected 1") {
    val expectedData = "imei=865733021674619&key=XXXXXXXX&d=10/08/15,23:13:52+0[100815,23135200,50.012516,14.427183,0.00,256.80,80.43,62,17]{}"
    val data: String = "865733021674619,XXXXXXXX,100815,23135200,50.012516,14.427183,0.00,256.80,80.43,62,17,19.61,1"

    val reqest = Api.buildHttpString(now, data)
    println(expectedData)
    println(reqest)

    assertResult(expectedData)(reqest)
  }

  test("API string should match expected 2") {
    val expectedData = "imei=865733021674619&key=XXXXXXXX&d=22/10/15,21:31:58+0[221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14]{}"
    val data: String = "865733021674619,XXXXXXXX,221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14,12.75,0"

    val reqest = Api.buildHttpString(now, data)
    println(expectedData)
    println(reqest)

    assertResult(expectedData)(reqest)
  }

  test("API MQTT JSON match expected ") {
    val expected = """{"cog":"-1","_type":"location","vel":"-1","t":"p","alt":"0","batt":14,"lon":14.427030,"tid":"VW","vac":"-1","lat":50.014862,"tst":1445542318}"""

    val data: String = "865733021674619,XXXXXXXX,221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14,12.75,0"
    val mqttStr = MqttApi.prepareMessage(now, data)

    println(expected)
    println(mqttStr)

    assertResult(expected)(mqttStr)

    MqttApi.sendData(mqttStr, "owntracks/IDEA/Test")
  }

  test("EPOC time should be OK") {
    val expected: String = "22101521315800"
    val time: Long = MqttApi.getEpocTime(expected)

    val timeStr = new SimpleDateFormat("ddMMyyHHmmssSS").format(new Date(time*1000))
    assertResult(expected)(timeStr)
  }
}