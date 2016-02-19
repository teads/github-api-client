package tv.teads.github.api.model

import io.circe._

trait ReferenceCodec {
  self: ReferenceObjectCodec ⇒
  implicit lazy val referenceDecoder = Decoder.instance { cursor ⇒
    for {
      ref ← cursor.downField("ref").as[String]
      url ← cursor.downField("url").as[String]
      obj ← cursor.downField("object").as[ReferenceObject]
    } yield Reference(ref, url, obj)
  }
}
case class Reference(ref: String, url: String, obj: ReferenceObject)

trait ReferenceObjectCodec {
  implicit lazy val referenceObjectDecoder: Decoder[ReferenceObject] = Decoder.instance { cursor ⇒
    for {
      objectType ← cursor.downField("type").as[ObjectType]
      sha ← cursor.downField("sha").as[String]
      url ← cursor.downField("url").as[String]
    } yield ReferenceObject(objectType, sha, url)
  }
}
case class ReferenceObject(objectType: ObjectType, sha: String, url: String)
