package com.tgsight.nifi.ops

import java.io.OutputStream
import java.nio.charset.{Charset, StandardCharsets}

class OutputStreamOps(val os: OutputStream) {

  def write(s: String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit =
    os.write(s.getBytes(charset))

}
