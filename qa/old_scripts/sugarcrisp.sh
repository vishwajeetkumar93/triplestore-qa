JAVA_OPTS=-Xmx16g ./target/start -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test

JAVA_OPTS=-Xmx16g ./target/start -Dsearch.transitions.templateParaphrase=false -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-notemplates

JAVA_OPTS=-Xmx16g ./target/start -Dsearch.transitions.relSyn=false -Dsearch.transitions.isaRelSyn=false -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-norelsyn

JAVA_OPTS=-Xmx16g ./target/start -Dparaphrase.template.useTypes=false -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-notemplatetypes

JAVA_OPTS=-Xmx16g ./target/start -Dtriplestore.skipNamespaces="reverb|openie4" -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-noopenie

JAVA_OPTS=-Xmx16g ./target/start -Dtriplestore.skipNamespaces="freebase" -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-nofreebase

JAVA_OPTS=-Xmx16g ./target/start -Dtriplestore.skipNamespaces="probase" -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-noprobase

JAVA_OPTS=-Xmx16g ./target/start -Dtriplestore.skipNamespaces="nell" -Dscoring.weights=eval/qa/models/sugarcrisp-2014-01-29-192116/model.1.avg.txt -Dparsing.cg.lexicalRuleKeep='^fullPattern|^.*Identity$' -Dparaphrase.template.maxHits=100 -Dsearch.maxSearchTimeSec=20 edu.knowitall.eval.qa.QASystemRunner eval/qa/questions/webquestions.test.txt eval/qa/output/eval/sugarcrisp-2014-01-29-192116-model.1.avg-webquestions.test-nonell
