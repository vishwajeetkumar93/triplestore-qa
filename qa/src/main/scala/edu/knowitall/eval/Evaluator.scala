package edu.knowitall.eval

import scala.io.Source
import java.io.PrintWriter
import com.typesafe.config.ConfigFactory
import java.io.File

object Evaluator extends App {
  
  def evaluate(writer: PrintWriter, questions: List[String], oracle: QAOracle, output: QASystemOutput) {
    var numCorrect = 0.0
    var numAnswered = 0.0
    for (q <- questions; a <- output.topAnswerFor(q)) {
      numAnswered += 1
      if (oracle.hasLabel(q, a)) {
      	if (oracle.isCorrect(q, a)) {
      	  numCorrect += 1
      	}
      } else {
        throw new IllegalArgumentException(s"No label for $q => $a")
      }
    }
    val precision = numCorrect / numAnswered
    val accuracy = numCorrect / questions.size
    writer.println(s"${questions.size} questions")
    writer.println(s"${numAnswered} answered")
    writer.println(s"${numCorrect} correct")
    writer.println(s"${precision} precision")
    writer.println(s"${accuracy} accuracy")
  }
  
  val questionsPath = args(0)
  val labelsPath = args(1)
  val outputPath = args(2)
  
  val questions = Source.fromFile(questionsPath, "UTF8").getLines.toList
  val oracle = new FileQAOracle(labelsPath)
  val output = QASystemOutput.fromPath(outputPath)
  
  val conf = ConfigFactory.load()
  val writer = new PrintWriter(new File(outputPath, conf.getString("eval.score.file")))

  
  evaluate(writer, questions, oracle, output)
  writer.close()

}