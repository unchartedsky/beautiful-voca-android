package pe.richard.voca.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.instantapps.InstantApps

class DeepLinks {

    companion object {

        fun newIntent(context: Context, deepLink: String): Intent {
            val intent = Intent(Intent.ACTION_VIEW, parse(context, deepLink))
            intent.setPackage(context.packageName)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            return intent
        }

        private fun parse(context: Context, deepLink: String): Uri {
            val uri = Uri.parse(deepLink)
            val iInstantApp = (uri.scheme == "https")
            val oInstantApp = InstantApps.isInstantApp(context)
            if (iInstantApp == oInstantApp) return uri

            if (oInstantApp) return convertHttps(uri)
            else return convertVoca(uri)
        }

        private fun convertHttps(uri: Uri): Uri {
            val builder = Uri.Builder().scheme("https").authority(uri.authority)

            val paths = uri.pathSegments
            if (paths.isEmpty()) return builder.encodedPath("/").encodedQuery(uri.encodedQuery).build()

            // TODO-DEEPLINK: convert to REST URI from android default deep link.
            when (paths[0]) {
                "settings" -> return builder.encodedPath("/settings/").encodedQuery(uri.encodedQuery).build()
                else -> return builder.encodedPath("/").encodedQuery(uri.encodedQuery).build()
            }
        }

        private fun convertVoca(uri: Uri): Uri {
            val builder = Uri.Builder().scheme("voca").authority(uri.authority)

            val paths = uri.pathSegments
            if (paths.isEmpty()) return builder.encodedPath("/main/").encodedQuery(uri.encodedQuery).build()

            // TODO-DEEPLINK: convert to android default deep link from REST URI.
            when (paths[0]) {
                "settings" -> return builder.encodedPath("/settings/").encodedQuery(uri.encodedQuery).build()
                else -> return builder.encodedPath("/main/").encodedQuery(uri.encodedQuery).build()
            }
        }

    }

}