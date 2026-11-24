package mek.stripeterminal.plugin

import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.callable.TerminalListener
import com.stripe.stripeterminal.external.models.ConnectionStatus
import com.stripe.stripeterminal.external.models.ConnectionTokenException
import com.stripe.stripeterminal.external.models.PaymentStatus
import com.stripe.stripeterminal.external.models.Reader
import mek.stripeterminal.api.TerminalHandlersApi
import mek.stripeterminal.mappings.toApi
import mek.stripeterminal.runOnMainThread

class TerminalDelegatePlugin(handlers: TerminalHandlersApi) :
    ConnectionTokenProvider, TerminalListener {
    // The Stripe SDK keeps this delegate for the life of the process, so the
    // handlers must follow whichever Flutter engine is current. See
    // TerminalPlatformPlugin.onInit.
    @Volatile
    private var _handlers: TerminalHandlersApi = handlers

    fun rebind(handlers: TerminalHandlersApi) {
        _handlers = handlers
    }

    override fun fetchConnectionToken(callback: ConnectionTokenCallback) = runOnMainThread {
        _handlers.requestConnectionToken(
            { error ->
                callback.onFailure(
                    ConnectionTokenException(error.message ?: "", error)
                )
            },
            { token -> callback.onSuccess(token) }
        )
    }

    // region Terminal listeners
    override fun onConnectionStatusChange(status: ConnectionStatus) = runOnMainThread {
        _handlers.connectionStatusChange(status.toApi())
    }

    override fun onPaymentStatusChange(status: PaymentStatus) = runOnMainThread {
        _handlers.paymentStatusChange(status.toApi())
    }
    // endregion
}
