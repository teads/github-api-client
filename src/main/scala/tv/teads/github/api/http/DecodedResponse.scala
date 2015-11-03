package tv.teads.github.api.http

import com.squareup.okhttp.Response

case class DecodedResponse[T](decoded: T, rawResponse: Response)