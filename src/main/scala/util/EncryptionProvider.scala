package util

import org.abstractj.kalium.crypto.{Random, SecretBox}

/**
  * Created by drashko on 18.03.16.
  */
object EncryptionProvider {

  val crypto_secretbox_NONCEBYTES = 24
  val crypto_secretbox_KEYBYTES = 32

  val r = new Random()

  def getSecretBoxForKey(encryptionKey: String): SecretBox = {
    val key: Array[Byte] = new Array[Byte](crypto_secretbox_KEYBYTES)
    System.arraycopy(encryptionKey.getBytes("UTF-8"), 0, key, 0, encryptionKey.length());

    new SecretBox(key)
  }

  def encrypt(plaintext: String, encryptionKey: String) = {
    val nonce: Array[Byte] = r.randomBytes(crypto_secretbox_NONCEBYTES);
    val cyphertext: Array[Byte] = getSecretBoxForKey(encryptionKey).encrypt(nonce, plaintext.getBytes("UTF-8"));
    val out: Array[Byte] = new Array[Byte](crypto_secretbox_NONCEBYTES + cyphertext.length);

    System.arraycopy(nonce, 0, out, 0, crypto_secretbox_NONCEBYTES)
    System.arraycopy(cyphertext, 0, out, crypto_secretbox_NONCEBYTES, cyphertext.length);

    new String(java.util.Base64.getEncoder.encodeToString(out))
  }

  def decrypt(cyphertextb64: String, encryptionKey: String) = {
    val onTheWire = java.util.Base64.getDecoder.decode(cyphertextb64.getBytes("UTF-8"))
    val cyphertext: Array[Byte] = new Array[Byte](onTheWire.length - crypto_secretbox_NONCEBYTES)
    val nonce: Array[Byte] = r.randomBytes(crypto_secretbox_NONCEBYTES);

    System.arraycopy(onTheWire, 0, nonce, 0, crypto_secretbox_NONCEBYTES);
    System.arraycopy(onTheWire, crypto_secretbox_NONCEBYTES, cyphertext, 0, onTheWire.length - crypto_secretbox_NONCEBYTES);

    new String(getSecretBoxForKey(encryptionKey).decrypt(nonce, cyphertext))
  }
}
