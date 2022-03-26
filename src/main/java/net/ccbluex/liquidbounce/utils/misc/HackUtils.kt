package net.ccbluex.liquidbounce.utils.misc

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HackUtils {
    private var lastDefaultHostVerifier: HostnameVerifier? = null
    private var lastDefaultSocketFactory: SSLSocketFactory? = null
    @Throws(Exception::class)
    fun processHacker() {
        lastDefaultHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
        lastDefaultSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory()
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            }
        )
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        val allHostsValid = HostnameVerifier { hostname, session -> true }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }

    fun revertHacker() {
        if (lastDefaultSocketFactory != null) HttpsURLConnection.setDefaultSSLSocketFactory(
            lastDefaultSocketFactory
        )
        if (lastDefaultHostVerifier != null) HttpsURLConnection.setDefaultHostnameVerifier(
            lastDefaultHostVerifier
        )
    }
}