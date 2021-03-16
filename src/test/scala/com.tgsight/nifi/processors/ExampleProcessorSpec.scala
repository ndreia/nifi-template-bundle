package com.tgsight.nifi.processors

import org.apache.nifi.util.TestRunners
import org.scalatest.{FunSpec, Matchers}
import com.tgsight.nifi.processors.ExampleProcessor.{E, P, R}

import scala.jdk.CollectionConverters._

class ExampleProcessorSpec extends FunSpec with Matchers {
  it("success relation") {
    val in: String = "Hello world"

    val processor = new TemplateProcessor
    val runner = TestRunners.newTestRunner(processor)
    runner.setProperty(P.example, in)

    runner.enqueue(in)
    runner.run()

    runner.assertTransferCount(R.success, 1)
    runner.assertTransferCount(R.failure, 0)

    for (flowFile <- runner.getFlowFilesForRelationship(R.success).asScala) {
      flowFile.assertAttributeEquals(P.example.getName, in.reverse)
      flowFile.assertContentEquals(in.reverse)
    }
  }

  it("failure relation") {
    val in: Array[Byte] = Array(1.toByte, 2.toByte)

    val processor = new TemplateProcessor
    val runner = TestRunners.newTestRunner(processor)

    runner.enqueue(in)
    runner.run()

    runner.assertTransferCount(R.success, 0)
    runner.assertTransferCount(R.failure, 1)

    for (flowFile <- runner.getFlowFilesForRelationship(R.failure).asScala) {
      flowFile.assertAttributeEquals("error", E.regExpMessage)
      flowFile.assertAttributeEquals(P.example.getName, null)
      flowFile.assertContentEquals(in)
    }
  }
}
