package edu.knowitall.apps

import edu.knowitall.scoring.AnswerScorer
import edu.knowitall.execution.QueryExecutor
import edu.knowitall.parsing.QuestionParser
import edu.knowitall.scoring.ScoredAnswerGroup
import org.slf4j.LoggerFactory
import edu.knowitall.execution.AnswerDerivation
import edu.knowitall.execution.AnswerGrouper

case class QASystem(parser: QuestionParser, executor: QueryExecutor, grouper: AnswerGrouper, scorer: AnswerScorer) {

  val logger = LoggerFactory.getLogger(this.getClass) 
  
  def answer(question: String): List[ScoredAnswerGroup] = {
    
    logger.info(s"Parsing question '$question'")
    val queries = parser.parse(question)
    
    logger.info(s"Executing queries for '$question'")
    val derivs = for (query <- queries; 
                      derivs = executor.deriveAnswers(query);
                      deriv <- derivs) yield deriv
    
    logger.info(s"Grouping answers for '$question'")
    val groups = grouper.group(derivs.toList)
                      
    logger.info(s"Scoring answers for '$question'")
    val answers = for (group <- groups;
                       scored = scorer.scoreAnswer(group)) yield scored
    answers.toList
    
  }
  
}