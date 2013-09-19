package edu.knowitall.paralex

import edu.knowitall.parsing.regex.RegexQuestionParser
import edu.knowitall.execution.IdentityExecutor
import edu.knowitall.triplestore.SolrClient
import edu.knowitall.execution.BasicAnswerGrouper
import edu.knowitall.scoring.UniformAnswerScorer
import edu.knowitall.apps.QASystem
import org.slf4j.LoggerFactory
import edu.knowitall.tool.stem.MorphaStemmer
import edu.knowitall.tool.tokenize.ClearTokenizer
import edu.knowitall.execution.SimpleQuery
import edu.knowitall.execution.UQuery
import edu.knowitall.execution.ListConjunctiveQuery
import edu.knowitall.parsing.QWord
import edu.knowitall.parsing.QuestionParser
import edu.knowitall.tool.tokenize.Tokenizer
import edu.knowitall.parsing.QToken
import edu.knowitall.execution.Search
import edu.knowitall.parsing.ArgVar
import edu.knowitall.execution.TConjunct
import edu.knowitall.execution.TVariable
import edu.knowitall.execution.UnquotedTLiteral
import edu.knowitall.triplestore.TriplestoreClient
import edu.knowitall.execution.QueryExecutor
import edu.knowitall.execution.Search.FieldPhrase
import edu.knowitall.triplestore.CachedTriplestoreClient
import edu.knowitall.execution.Search.FieldKeywords

case class QACluster(answers: Seq[String], questions: Seq[String])

case object QACluster {
  val prefix = "^([qa]:)(.*)".r
  def trim(s: String) = s match {
    case prefix(l, r) => r
    case _ => s
  }
  def fromString(s: String): QACluster = {
    val parts = s.split("\t").toSeq
    val questions = parts.filter(p => p.startsWith("q:")).map(trim).distinct
    val answers = parts.filter(p => p.startsWith("a:")).map(trim).distinct
    QACluster(answers, questions)
  }
}

case class OutputRecord(template1: Seq[QToken], template2: Seq[QToken]) {
  def stem(tpl: Seq[QToken]) = tpl.map(qt => qt match {
    case QWord(w) => QWord(MorphaStemmer.stem(w))
    case x => x
  }).mkString(" ")
  val stemmedTemplate1 = stem(template1)
  val stemmedTemplate2 = stem(template2)
  override def toString = stemmedTemplate1 + "\t" + stemmedTemplate2
  def ordered = if (stemmedTemplate1 < stemmedTemplate2) {
    OutputRecord(template1, template2)
  } else {
    OutputRecord(template2, template1)
  }
}

case class ParsedQuestion(string: String, qWords: List[QWord], parses: List[TConjunct])

case class AbstractedArg(arg: List[QWord], conjunct: TConjunct)

case class TemplateGenerator(parser: QuestionParser = RegexQuestionParser(),
    tokenizer: Tokenizer = new ClearTokenizer()) {
  
  def filterQuery(uq: UQuery): Option[TConjunct] = uq match {
    case ListConjunctiveQuery(List(v), List(c)) => Some(TConjunct(c.name, c.values))
    case _ => None
  }
  
  def getArg(sq: TConjunct): AbstractedArg = {
    val fields = sq.literalFields
    val fields2 = for ((f, v) <- fields; if f == Search.arg1 || f == Search.arg2) 
      yield (f,v)
    val argField = fields2 match {
      case List((Search.arg1, v)) => Search.arg1
      case List((Search.arg2, v)) => Search.arg2
      case _ => throw new IllegalStateException(s"Could not find non-variable arg in $fields")
    }
    val arg = toQWords(sq.values(argField).toString().toLowerCase())
    val newVals = sq.values + (argField -> TVariable("y"))
    val newConj = TConjunct(sq.name, newVals)
    AbstractedArg(arg, newConj)
  }
  
  def toQWords(s: String) = tokenizer.tokenize(s).map(t => QWord(t.string)).toList
  
  def parseQuestion(q: String): ParsedQuestion = {
    val parses = parser.parse(q).flatMap(filterQuery).toList
    val qWords = toQWords(q.toLowerCase())
    ParsedQuestion(q, qWords, parses)
  }
  
  def getArgs(pq: ParsedQuestion): List[AbstractedArg] = pq.parses.map(getArg)
  
  def abstractArg(pq: ParsedQuestion, arg: List[QWord]): List[QToken] = {
    val qw = pq.qWords
    val i = qw.indexOfSlice(arg)
    if (i >= 0) {
      qw.slice(0, i) ++ List(ArgVar) ++ qw.slice(i+arg.size, qw.size)
    } else {
      pq.qWords
    }
  }
  
  def isAbstracted(aq: List[QToken]) = aq.count(t => t == ArgVar) == 1
  
  def generateTemplates(c: QACluster): List[OutputRecord] = {
    try {
      val qs = c.questions.map(parseQuestion) 
      val records = 
        for (q1 <- qs;
        	 arg <- getArgs(q1);
        	 abs1 = abstractArg(q1, arg.arg);
        	 q2 <- qs;
        	 abs2 = abstractArg(q2, arg.arg);
        	 if isAbstracted(abs2)) 
          yield OutputRecord(abs1, abs2).ordered
      records.toList.distinct.filter(a => a.template1 != a.template2)
    } catch {
      case e: Throwable => {
        e.printStackTrace(System.err)
        List()
      }
    }
  }
  
}

