
import api.Api

import org.scalatest.FunSuite

class ApiTest extends FunSuite {

  test("API string should match expected 1") {
    val expectedData = "imei=865733021674619&key=cSQ88qShwC3&d=10/08/15,23:13:52+0[100815,23135200,50.012516,14.427183,0.00,256.80,80.43,62,17]{}"
    val data:String = "865733021674619,cSQ88qShwC3,100815,23135200,50.012516,14.427183,0.00,256.80,80.43,62,17,19.61,1"

    val reqest = Api.buildHttpString(data)
    println(expectedData)
    println(reqest)

    assertResult(expectedData)(reqest)
  }

  test("API string should match expected 2") {
    val expectedData = "imei=865733021674619&key=cSQ88qShwC3&d=22/10/15,21:31:58+0[221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14]{}"
    val data:String = "865733021674619,cSQ88qShwC3,221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14,12.75,0"

    val reqest = Api.buildHttpString(data)
    println(expectedData)
    println(reqest)

    assertResult(expectedData)(reqest)
  }
}