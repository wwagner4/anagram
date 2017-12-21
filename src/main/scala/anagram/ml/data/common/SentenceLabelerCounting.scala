package anagram.ml.data.common

import anagram.words.{MappingResult, WordMapper}

case class SentenceLabelerCounting(lengthFactors: Map[Int, Double], wm: WordMapper[Seq[String]]) extends SentenceLabeler {

  case class MR(
                 sentence: Sentence,
                 result: MappingResult[Seq[String]],
               )

  override def labelSentence(sentences: Seq[Sentence]): Seq[Labeled] = {
    val mappingResults = sentences.map { s =>
      val mr = wm.map(s.words)
      MR(s, mr)
    }
    val groupedMappingResults = mappingResults.groupBy(mr => mr.result).toSeq
    groupedMappingResults.flatMap { case (grp: MappingResult[Seq[String]], grpMembers: Seq[_]) =>
      val lfac: Double = lengthFactors(grp.intermediate.size)
      if (grp.intermediate.contains("?")) None
      else {
        val r: Double = rating(grpMembers, lfac)
        Some(Labeled(grp.features, r))
      }
    }
  }

  def rating(mappingResultsGroup: Seq[MR], lenFact: Double): Double = {
    val countedTypes: Seq[(SentenceType, Int)] = mappingResultsGroup
      .map(_.sentence.sentenceType)
      .groupBy(identity)
      .mapValues(_.size).toSeq
    countedTypes.foldLeft(0.0) {
      case (r, (stype, cnt)) => stype match {
        case SentenceType_COMPLETE => r + cnt * 1 * lenFact
        case SentenceType_BEGINNING => r + cnt * 5 * lenFact
        case SentenceType_OTHER => r + cnt * 10 * lenFact
        case SentenceType_RANDOM => throw new IllegalStateException("SentenceType_RANDOM makes no sense for Counting")
      }
    }
  }

}
