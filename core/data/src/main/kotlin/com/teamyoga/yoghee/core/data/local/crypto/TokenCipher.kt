package com.teamyoga.yoghee.core.data.local.crypto

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

/**
 * Tink 기반 문자열 암호화 wrapper
 */
class TokenCipher(context: Context) {

    private val aead: Aead

    init {
        // Tink 초기화
        AeadConfig.register()

        // Keystore 안에 해당 이름의 마스터 키를 만들거나 가져옴
        val masterKeyUri = "android-keystore://$MASTER_KEY_ALIAS"

        // 키셋을 SharedPreferences에 저장하되, 마스터 키로 한 번 더 암호화해서 보관한다.
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, KEYSET_PREFS_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(masterKeyUri)
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    /**
     * 평문 → 암호문 (Base64).
     * 매 호출마다 Tink 내부에서 새 nonce(IV)가 생성되므로, 같은 입력도 매번 다른 결과가 나온다.
     */
    fun encrypt(plaintext: String): String {
        val cipherBytes = aead.encrypt(plaintext.toByteArray(Charsets.UTF_8), null)
        return Base64.encodeToString(cipherBytes, Base64.NO_WRAP)
    }

    /**
     * 암호문 (Base64) → 평문.
     * 키 변경/조작 등으로 복호화 실패 시 예외를 던지므로, 호출부에서 catch 해서 "토큰 없음"으로 처리한다.
     */
    fun decrypt(ciphertext: String): String {
        val cipherBytes = Base64.decode(ciphertext, Base64.NO_WRAP)
        val plainBytes = aead.decrypt(cipherBytes, null)
        return String(plainBytes, Charsets.UTF_8)
    }

    companion object {
        // Keystore에 만들어질 마스터 키의 별칭. 앱 패키지명을 prefix로 두면 충돌 위험이 적다.
        private const val MASTER_KEY_ALIAS = "yoghee_token_master_key"

        // 암호화된 키셋을 보관할 SharedPreferences 파일/키 이름.
        private const val KEYSET_PREFS_FILE = "yoghee_token_keyset_prefs"
        private const val KEYSET_NAME = "yoghee_token_keyset"
    }
}
