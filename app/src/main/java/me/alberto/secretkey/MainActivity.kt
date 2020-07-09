package me.alberto.secretkey

import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private val TRANSFORMATION = "AES"
    private val PASSPHRASE = "password"
    private val PASSOWORD = "Alberto,/90()"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encrypted = encrypt(PASSPHRASE)
        val decrypted = decrypt(encrypted, PASSPHRASE)
        output.text = decrypted
    }

    private fun decrypt(encrypted: String, passphrase: String): String {
        val key = generateSecretKey(passphrase)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val dcryptedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val dcryptedValue = cipher.doFinal(dcryptedBytes)
        return String(dcryptedValue)

    }

    @Throws(Exception::class)
    private fun encrypt(passPhrase: String): String {
        val key = generateSecretKey(passPhrase)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encByte = cipher.doFinal(PASSOWORD.toByteArray())
        val encryptedValue = Base64.encodeToString(encByte, Base64.DEFAULT)
        return encryptedValue
    }

    @Throws(Exception::class)
    private fun generateSecretKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
}