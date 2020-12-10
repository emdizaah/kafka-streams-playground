package io.inventi.tech.streams.ws.handlers

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class KrakenWsHandler(
    @Qualifier("krakenWsMessageHelper")private val wsMessageHelper: WsMessageHelper
) : CommonTickerMessageHandler(wsMessageHelper)