package io.inventi.tech.streams.ws.handlers

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class HoubiWsHandler(
    @Qualifier("huobiWsMessageHelper") private val wsMessageHelper: WsMessageHelper
) : CommonTickerMessageHandler(wsMessageHelper)