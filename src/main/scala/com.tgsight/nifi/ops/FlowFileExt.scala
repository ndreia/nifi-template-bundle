package com.tgsight.nifi.ops

import java.nio.charset.{Charset, StandardCharsets}

import org.apache.commons.io.IOUtils
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.processor._

/** Extends FlowFile with new convenience methods */
class FlowFileExt(val flowFile: FlowFile) extends AnyVal {

  def contentAsString(implicit session: ProcessSession, charset: Charset = StandardCharsets.UTF_8): String = {
    var result: String = null
    flowFile.read(in => result = IOUtils.toString(in, charset))
    result
  }

}
