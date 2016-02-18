package tv.teads.github.api.http

import okhttp3.Response

case class DecodedResponse[T](decoded: T, rawResponse: Response)
