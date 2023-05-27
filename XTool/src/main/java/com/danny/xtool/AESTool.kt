/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.os.Build
import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Provider
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * AES加密
 *
 * @author danny
 * @since 2023-05-21
 */
object AESTool {
    /**
     * AES加密
     *
     * @param content 需加密内容
     * @param password 加密密码
     */
    fun encrypt(content: String, password: String): ByteArray? {
        try {
            val kGen = KeyGenerator.getInstance("AES")
            kGen.init(128, SecureRandom(password.toByteArray()))
            val secretKey = kGen.generateKey()
            val enCodeFormat = secretKey.encoded
            val key = SecretKeySpec(enCodeFormat, "AES")
            val cipher = Cipher.getInstance("AES") // 创建密码器
            val byteContent = content.toByteArray()
            cipher.init(Cipher.ENCRYPT_MODE, key) // 初始化
            return cipher.doFinal(byteContent)
        } catch (e: NoSuchAlgorithmException) {

        } catch (e: NoSuchPaddingException) {

        } catch (e: InvalidKeyException) {

        } catch (e: UnsupportedEncodingException) {

        } catch (e: IllegalBlockSizeException) {

        } catch (e: BadPaddingException) {

        } catch (e: NoSuchPaddingException) {

        }
        return null
    }

    /**
     * RSA加密,生成公钥私钥
     */
    fun getKeys(): HashMap<String, Any>? = try {
        val map = HashMap<String, Any>()
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPairGen.initialize(1024)
        val keyPair = keyPairGen.genKeyPair()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        map["public"] = publicKey
        map["private"] = privateKey
        map
    } catch (e: NoSuchAlgorithmException) {
        null
    }

    val e = "9238513401340235"
    fun encrypt1(src: String, key: String?): String? {
        key?.let {
            return when {
                key.length != 16 -> null
                else -> {
                    val raw = key.toByteArray()
                    val sKeySpec = SecretKeySpec(raw, "AES")
                    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    val iv = IvParameterSpec(e.toByteArray())
                    cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv)
                    val encrypted = cipher.doFinal(src.toByteArray())
                    Base64.encodeToString(encrypted, 0)
                }
            }
        }
        return null
    }

    /**
     * 加密
     */
    fun aesEncrypt(seed: String, clearText: String): String {
        val raw = getByteRaw(seed.toByteArray())
        val result = encrypt(raw, clearText.toByteArray())
        return toHex(result)
    }

    fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val sks = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, sks)
        return cipher.doFinal(clear)
    }

    fun toHex(result: ByteArray?): String {
        result?: return ""
        val sb = StringBuilder(2 * result.size)
        for (i in result.indices) {
            appendHex(sb, result[i])
        }
        return sb.toString()
    }

    const val HEX: String = "0123456789ABCDEF"
    private fun appendHex(sb: StringBuilder, byte: Byte) {
        sb.append(HEX[byte.toInt().shr(4).and(0x0f)])
            .append(HEX[byte.toInt().and(0x0f)])
    }

    /**
     * 解密
     */
    fun aesDecrypt(seed: String, encrypt: String): String {
        val raw = getByteRaw(seed.toByteArray())
        val encryptArray = toByte(encrypt)
        val result = decrypt(raw, encryptArray)
        return String(result, Charset.forName("UTF-8"))
    }

    private fun toByte(encrypt: String): ByteArray {
        val len = encrypt.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) {
            result[i] = Integer.valueOf(encrypt.substring(2 * i, 2 * i + 2), 16).toByte()
        }
        return result
    }

    fun decrypt(raw: ByteArray, encrypt: ByteArray): ByteArray {
        val sks = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, sks)
        return cipher.doFinal(encrypt)
    }

    /**
     * 处理密钥
     */
    private fun getByteRaw(seed: ByteArray): ByteArray {
        // AES加密
        val keyGenerator = KeyGenerator.getInstance("AES")
        val secureRandom =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // >=17
                // SHA1PRNG强随机算法,区别4.2以上版本调用
//                SecureRandom.getInstance("SHA1PRNG", "Crypto")
                SecureRandom.getInstance("SHA1PRNG", CryptoProvider())
            } else {
                SecureRandom.getInstance("SHA1PRNG")
            }
        secureRandom.setSeed(seed)
        // AES中128位密钥版本有10个加密循环
        // AES中192比特密钥版本有12个加密循环
        // AES中256比特密钥版本有14个加密循环
        keyGenerator.init(128, secureRandom)
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }

    class CryptoProvider: Provider("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)") {
        init {
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl")
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software")
        }
    }

    /**
     * 生成随机数,可以当作动态密钥 加密解密密钥必须一致,不然无法解密
     *
     * @return
     */
    fun createKey(): String? {
        try {
            val random = SecureRandom.getInstance("SHA1PRNG")
            val byteKey = ByteArray(20)
            random.nextBytes(byteKey)
            return toHex(byteKey)
        } catch (e: NoSuchAlgorithmException) {

        }
        return null
    }

    fun toHex(result: String) = toHex(result.toByteArray())

    fun fromHex(text: String) = String(toByte(text))

    /**
     * 给指定字符串按照md5算法去加密
     */
    fun md5Encrypt(psd: String): String {
        // 1.指定加密算法类型
        val digest = MessageDigest.getInstance("MD5")

        // 加盐
        val temp = psd + "xxx"

        // 2.将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
        val byteArray = digest.digest(temp.toByteArray())

        // 3.循环遍历bs,然后让其生成32位字符串,固定写法
        // 4.拼接字符串过程
        val sb = StringBuilder()
        for (b in byteArray) {
            val i = b.toInt().and(0xff)
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        return sb.toString()
    }

    /**
     * 给指定字符串按照md5算法去加密
     */
    fun md5Encrypt2(code: String): String? {
        // 加盐
        val temp = "${code}xxx"
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(code.toByteArray())
            val sb = StringBuilder()
            for (b in bytes) {
                // 循环遍历,生成32位字符串
                val i = b.toInt().and(0xFF)

                // int转16进制字符
                var hex = Integer.toHexString(i)
                if (hex.length < 2) {
                    hex = "0$hex"
                }
                sb.append(hex)
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {

        }
        return null
    }
}
