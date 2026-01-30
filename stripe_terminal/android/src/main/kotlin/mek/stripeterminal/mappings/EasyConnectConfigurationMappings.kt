package mek.stripeterminal.mappings

import com.stripe.stripeterminal.external.models.AppsOnDevicesEasyConnectionConfiguration
import com.stripe.stripeterminal.external.models.DiscoveryConfiguration
import com.stripe.stripeterminal.external.models.EasyConnectConfiguration
import com.stripe.stripeterminal.external.models.EasyConnectConfiguration.AppsOnDevicesEasyConnectionConfiguration
import com.stripe.stripeterminal.external.models.EasyConnectConfiguration.InternetEasyConnectConfiguration
import com.stripe.stripeterminal.external.models.EasyConnectConfiguration.TapToPayEasyConnectConfiguration
import com.stripe.stripeterminal.external.models.InternetEasyConnectConfiguration
import com.stripe.stripeterminal.external.models.TapToPayEasyConnectConfiguration
import mek.stripeterminal.api.AppsOnDevicesEasyConnectionConfigurationApi
import mek.stripeterminal.api.EasyConnectConfigurationApi
import mek.stripeterminal.api.InternetEasyConnectConfigurationApi
import mek.stripeterminal.api.TapToPayEasyConnectConfigurationApi
import mek.stripeterminal.plugin.ReaderDelegatePlugin

fun EasyConnectConfigurationApi.toHost(readerDelegate: ReaderDelegatePlugin): EasyConnectConfiguration {
    return when (this) {
        is InternetEasyConnectConfigurationApi -> InternetEasyConnectConfiguration(
            discoveryConfiguration = requireInternetDiscovery(discoveryConfiguration),
            connectionConfiguration = connectionConfiguration.toHost(readerDelegate)
        )
        is AppsOnDevicesEasyConnectionConfigurationApi -> AppsOnDevicesEasyConnectionConfiguration(
            discoveryConfiguration = DiscoveryConfiguration.AppsOnDevicesDiscoveryConfiguration(),
            connectionConfiguration = connectionConfiguration.toHost(readerDelegate)
        )
        is TapToPayEasyConnectConfigurationApi -> TapToPayEasyConnectConfiguration(
            discoveryConfiguration = requireTapToPayDiscovery(discoveryConfiguration),
            connectionConfiguration = connectionConfiguration.toHost(readerDelegate)
        )
    }
}

private fun requireInternetDiscovery(
    discoveryConfiguration: mek.stripeterminal.api.DiscoveryConfigurationApi,
): DiscoveryConfiguration.InternetDiscoveryConfiguration {
    val host = discoveryConfiguration.toHost()
        ?: throw IllegalArgumentException("Discovery method not supported")
    return host as? DiscoveryConfiguration.InternetDiscoveryConfiguration
        ?: throw IllegalArgumentException("Internet discovery configuration required")
}

private fun requireTapToPayDiscovery(
    discoveryConfiguration: mek.stripeterminal.api.DiscoveryConfigurationApi,
): DiscoveryConfiguration.TapToPayDiscoveryConfiguration {
    val host = discoveryConfiguration.toHost()
        ?: throw IllegalArgumentException("Discovery method not supported")
    return host as? DiscoveryConfiguration.TapToPayDiscoveryConfiguration
        ?: throw IllegalArgumentException("TapToPay discovery configuration required")
}
