package mek.stripeterminal.mappings

import com.stripe.stripeterminal.external.models.ConfirmPaymentIntentConfiguration
import com.stripe.stripeterminal.external.models.SurchargeConfiguration
import com.stripe.stripeterminal.external.models.SurchargeConsent
import com.stripe.stripeterminal.external.models.SurchargeConsentCollection
import mek.stripeterminal.api.ConfirmPaymentIntentConfigurationApi
import mek.stripeterminal.api.SurchargeConfigurationApi
import mek.stripeterminal.api.SurchargeConsentApi
import mek.stripeterminal.api.SurchargeConsentCollectionApi

fun ConfirmPaymentIntentConfigurationApi.toHost(): ConfirmPaymentIntentConfiguration {
    val builder = ConfirmPaymentIntentConfiguration.Builder()
    returnUrl?.let(builder::setReturnUrl)
    surcharge?.let { builder.setSurcharge(it.toHost()) }
    return builder.build()
}

fun SurchargeConfigurationApi.toHost(): SurchargeConfiguration {
    val builder = SurchargeConfiguration.Builder(amount)
    surchargeConsent?.let { builder.setConsent(it.toHost()) }
    return builder.build()
}

fun SurchargeConsentApi.toHost(): SurchargeConsent {
    val builder = SurchargeConsent.Builder()
    builder.setCollection(collection.toHost())
    notice?.let(builder::setNotice)
    return builder.build()
}

fun SurchargeConsentCollectionApi.toHost(): SurchargeConsentCollection {
    return when (this) {
        SurchargeConsentCollectionApi.DISABLED -> SurchargeConsentCollection.DISABLED
        SurchargeConsentCollectionApi.ENABLED -> SurchargeConsentCollection.ENABLED
    }
}
