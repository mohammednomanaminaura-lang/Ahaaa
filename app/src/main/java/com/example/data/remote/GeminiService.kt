package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Moshi data models for Gemini REST API
data class GeminiContentRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String? = null
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

data class GeminiCandidate(
    val content: GeminiContent? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiContentRequest
    ): GeminiResponse
}

object GeminiService {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(GeminiApi::class.java)

    suspend fun askGemini(
        prompt: String,
        systemInstruction: String = "You are the AI System Assistant of the Shadow Monarch Realm, serving Master Mohammad Noman Amin."
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // High-fidelity Solo Leveling & Anime offline intelligence fallback
            return@withContext generateSmartFallback(prompt, systemInstruction)
        }

        try {
            val request = GeminiContentRequest(
                contents = listOf(
                    GeminiContent(
                        role = "user",
                        parts = listOf(GeminiPart(text = prompt))
                    )
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = systemInstruction))
                )
            )
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "The Monarch Realm connection fluctuated. No response received."
        } catch (e: Exception) {
            generateSmartFallback(prompt, systemInstruction)
        }
    }

    private fun generateSmartFallback(prompt: String, systemInstruction: String): String {
        val p = prompt.lowercase()
        val isHindi = p.contains("kya") || p.contains("hai") || p.contains("kaise") || p.contains("namaste") || p.contains("mera") || p.contains("batao")

        return when {
            // Character Chat: Sung Jin-Woo
            systemInstruction.contains("Sung Jin-Woo", ignoreCase = true) -> {
                if (isHindi) {
                    "System notification: 'Main Sung Jin-Woo hoon. Monarch Mohammad Noman, hamesha apne limits ko push karo. Jab tak tum haar nahi maante, tab tak tumhara shadow army hamesha badhta rahega. Arise!'"
                } else {
                    "System Alert: 'I am Sung Jin-Woo. Monarch Mohammad Noman, never allow weakness in your mind. Train your stats daily, clear the SSC academic dungeons, and conquer every gate. Arise!'"
                }
            }
            // Character Chat: Naruto
            systemInstruction.contains("Naruto", ignoreCase = true) -> {
                if (isHindi) {
                    "Dattebayo! Monarch Mohammad Noman bhai, chahe kitna bhi mushkil SSC exam ya workout ho, apna ninja way kabhi mat chodna! Main Hokage banunga aur aap Absolute Shadow Monarch ho!"
                } else {
                    "Dattebayo! Monarch Mohammad Noman, no matter how tough the daily quest or SSC exam is, never back down! That's our ninja way! Keep leveling up!"
                }
            }
            // Character Chat: Goku
            systemInstruction.contains("Goku", ignoreCase = true) -> {
                if (isHindi) {
                    "Yo! Main Goku! Noman, chalo 100 pushups aur sparring session karte hain! Jitna zyada heavy gravity mein train karoge, utna hi Super Saiyan level badhega! Let's go!"
                } else {
                    "Hey! It's me, Goku! Monarch Mohammad Noman, are you ready for today's training? Push past your limits and let's reach Super Saiyan Monarch tier!"
                }
            }
            // AI Advisor: Igris
            systemInstruction.contains("Igris", ignoreCase = true) || p.contains("igris") -> {
                "My Liege, Absolute Shadow Monarch Mohammad Noman Amin. Your humble servant Igris stands ready. Do not neglect your daily SSC study milestones or physical conditioning. A true Monarch's mind and blade are equally sharp."
            }
            // AI Advisor: Beru
            systemInstruction.contains("Beru", ignoreCase = true) || p.contains("beru") -> {
                "KIEEEEEK! MY SOVEREIGN MOHAMMAD NOMAN! This loyal servant shall obliterate any obstacle blocking your ascension! Have you completed your 100 push-ups and SSC Mathematics drills today, My Liege?!"
            }
            // Anime Trivia / Knowledge base
            p.contains("solo leveling") || p.contains("jin-woo") || p.contains("arise") || p.contains("monarch") -> {
                "**Solo Leveling Archive Data:**\n- **Protagonist:** Sung Jin-Woo (Shadow Monarch, Successor to Ashborn).\n- **Key Skills:** Shadow Extraction ('Arise'), Ruler's Authority, Shadow Exchange, Monarch's Domain, Dragon's Fear.\n- **Shadow Army Command:** Grand Marshal Bellion, Marshal Igris, Marshal Beru, Greed, Iron, Tank, Tusk, Kaisel.\n- **Master of Realm:** Absolute Shadow Monarch Mohammad Noman Amin."
            }
            p.contains("dragon ball") || p.contains("goku") || p.contains("vegeta") -> {
                "**Dragon Ball Multiverse Lore:**\n- **Ultra Instinct:** The ultimate martial art state of the gods, reacting purely on autonomic instinct without thought lag.\n- **Power Scaling:** Saiyan physiology grants Zenkai boosts after near-fatal battles, directly mirroring the Monarch leveling system."
            }
            p.contains("naruto") || p.contains("sasuke") || p.contains("itachi") -> {
                "**Shinobi World Archive:**\n- **Six Paths Chakra:** Divine celestial energy granted to the reincarnations of Asura and Indra.\n- **Shadow Clone Jutsu:** Advanced forbidden technique for rapid parallel knowledge accumulation and tactical warfare."
            }
            // Avatar Generator Prompt
            systemInstruction.contains("Avatar", ignoreCase = true) || p.contains("avatar") || p.contains("sketch") -> {
                "**Manhwa Transformation Synthesis Complete:**\nGenerated Solo Leveling S-Rank Manhwa Persona for Master Mohammad Noman Amin:\n- **Aesthetic:** Dark obsidian shadow plate armor with pulsing electric neon blue mana veins.\n- **Aura:** Wisps of dark purple void energy radiating from the eyes and silhouette.\n- **Weapon:** Twin daggers crafted from Monarch dragon scales glowing with azure runes."
            }
            else -> {
                "System Operational Notice: Command received from Absolute Shadow Monarch Mohammad Noman Amin. All System parameters, shadow reserves, and tracker matrices are operating at peak 100% capacity."
            }
        }
    }
}
