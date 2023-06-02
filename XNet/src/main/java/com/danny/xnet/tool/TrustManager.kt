/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.tool

import android.content.Context
import java.io.BufferedInputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SignatureException
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * 509证书管理
 *
 * @author x
 * @since 2023-06-02
 */
class TrustManager(private val ca: Certificate): X509TrustManager {

    /**
     * 默认接受任意的客户端证书
     */
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

    /**
     *
     */
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        when {
            null == chain || chain.isEmpty() -> {}
            authType.isNullOrEmpty() -> {}
            else -> {
                for (i in chain.indices) {
                    // 校验证书有效期
                    chain[i].checkValidity()

                    // 校验证书签名
                    try {
                        chain[i].verify(ca.publicKey)
                    } catch (e: NoSuchAlgorithmException) {

                    } catch (e: InvalidKeyException) {

                    } catch (e: NoSuchProviderException) {

                    } catch (e: SignatureException) {

                    }
                }
            }
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    fun init(context: Context) {
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = BufferedInputStream(context.assets.open("asce1885.crt")) // 证书位于assets
        caInput.use {
            val ca = cf.generateCertificate(it)
            val trustManager = TrustManager(ca)
            val sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        }
    }
}
