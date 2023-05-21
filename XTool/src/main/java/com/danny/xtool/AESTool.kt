/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
}
