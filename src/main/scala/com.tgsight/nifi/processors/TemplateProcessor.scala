package com.tgsight.nifi.processors

import java.io.OutputStream

import org.apache.nifi.annotation.behavior.InputRequirement
import org.apache.nifi.annotation.behavior.InputRequirement.Requirement
import org.apache.nifi.annotation.documentation.{CapabilityDescription, Tags}
import org.apache.nifi.components.PropertyDescriptor
import org.apache.nifi.expression.ExpressionLanguageScope
import org.apache.nifi.flowfile.FlowFile
import org.apache.nifi.processor.util.StandardValidators
import org.apache.nifi.processor.{ProcessContext, ProcessSession, Relationship}

import com.tgsight.nifi.ops._
import com.tgsight.nifi.processors.TemplateProcessor._
import com.tgsight.nifi.{FlowFileNotNull, ScalaProcessor, _}

@Tags(Array("custom", "scala"))
@InputRequirement(Requirement.INPUT_REQUIRED)
@CapabilityDescription("Template for you custom processor.")
class TemplateProcessor extends ScalaProcessor with FlowFileNotNull {

  def properties: List[PropertyDescriptor] = List (P.example)

  def relationships: Set[Relationship] = Set(R.success, R.failure)

  def onTrigger(flowFile: FlowFile)(implicit context: ProcessContext, session: ProcessSession): Unit = {
    val example = P.example.evaluate(flowFile).get
    withThrowableAsEither[Unit, FlowFile](()) { _ =>
      val input = flowFile.contentAsString
      if (!input.matches("[a-zA-Z ]*"))
        throw new RuntimeException(E.regExpMessage)
      flowFile.write{ outputStream: OutputStream => {
        withResource(outputStream) { output =>
          output.write(input.reverse)
        }
      }
      }
    } match {
      case Left(error) =>
        flowFile
          .putAttribute("error", error.getMessage)
          .transfer(R.failure)
      case Right(flowfileOutput) =>
        flowfileOutput
          .putAttribute(P.example.getName, example.reverse)
          .transfer(R.success)
    }
  }
}

object TemplateProcessor {
  object P {
    val example: PropertyDescriptor = new PropertyDescriptor.Builder()
      .name("example")
      .displayName("Example property")
      .description("Example property description")
      .defaultValue("default value")
      .required(true)
      .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
      .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
      .build()
  }

  object R {
    val success: Relationship = new Relationship.Builder()
      .name("success")
      .description("Any FlowFile that is successfully transferred is routed to this relationship")
      .build()

    val failure: Relationship = new Relationship.Builder()
      .name("failure")
      .description("Any FlowFile that fails to be transferred is routed to this relationship")
      .build()
  }

  object E {
    val regExpMessage = "accepts only regular expression [a-zA-Z ]*"
  }
}
