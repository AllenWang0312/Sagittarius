package edu.tjrac.swant.fingerprint.core

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Created by popfisher on 2016/11/7.
 */
@TargetApi(Build.VERSION_CODES.M)
class CryptoObjectCreator(createListener: ICryptoObjectCreateListener?) {
    var cryptoObject: FingerprintManager.CryptoObject? = null
        private set
    private var mKeyStore: KeyStore?
    private val mKeyGenerator: KeyGenerator?
    private var mCipher: Cipher?

    interface ICryptoObjectCreateListener {
        fun onDataPrepared(cryptoObject: FingerprintManager.CryptoObject?)
    }

    private fun prepareData(createListener: ICryptoObjectCreateListener?) {
        object : Thread("FingerprintLogic:InitThread") {
            override fun run() {
                try {
                    if (cryptoObject != null) {
                        createKey()
                        // Set up the crypto object for later. The object will be authenticated by use
                        // of the fingerprint.
                        if (!initCipher()) {
                            //                            FPLog.log("Failed to init Cipher.");
                        }
                    }
                } catch (e: Exception) {
                    //                    FPLog.log(" Failed to init Cipher, e:" + Log.getStackTraceString(e));
                }
                createListener?.onDataPrepared(cryptoObject)
            }
        }.start()
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    private fun createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore!!.load(null)
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            mKeyGenerator!!.init(KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC) // Require the user to authenticate with a fingerprint to authorize every use
                // of the key
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build())
            mKeyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            //            FPLog.log(" Failed to createKey, e:" + Log.getStackTraceString(e));
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Initialize the [Cipher] instance with the created key in the [.createKey]
     * method.
     *
     * @return `true` if initialization is successful, `false` if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private fun initCipher(): Boolean {
        return try {
            mKeyStore!!.load(null)
            val key = mKeyStore!!.getKey(KEY_NAME, null) as SecretKey
            mCipher!!.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: KeyPermanentlyInvalidatedException) {
            //            FPLog.log(" Failed to initCipher, e:" + Log.getStackTraceString(e));
            false
        } catch (e: KeyStoreException) {
            //            FPLog.log(" Failed to initCipher, e :" + Log.getStackTraceString(e));
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    fun onDestroy() {
        mCipher = null
        cryptoObject = null
        mCipher = null
        mKeyStore = null
    }

    companion object {
        private const val KEY_NAME = "crypto_object_fingerprint_key"
        fun providesKeystore(): KeyStore? {
            return try {
                KeyStore.getInstance("AndroidKeyStore")
            } catch (e: Throwable) {
                null
            }
        }

        fun providesKeyGenerator(): KeyGenerator? {
            return try {
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            } catch (e: Throwable) {
                null
            }
        }

        fun providesCipher(keyStore: KeyStore?): Cipher? {
            return try {
                Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            } catch (e: Throwable) {
                null
            }
        }
    }

    init {
        mKeyStore = providesKeystore()
        mKeyGenerator = providesKeyGenerator()
        mCipher = providesCipher(mKeyStore)
        if (mKeyStore != null && mKeyGenerator != null && mCipher != null) {
            cryptoObject = FingerprintManager.CryptoObject(mCipher!!)
        }
        prepareData(createListener)
    }
}