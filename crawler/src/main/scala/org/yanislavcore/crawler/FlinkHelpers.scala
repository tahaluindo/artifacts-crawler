package org.yanislavcore.crawler

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.api.scala.OutputTag
import org.yanislavcore.common.data.{FailedUrlData, FetchFailureData, FetchSuccessData, ScheduledUrlData}
import org.yanislavcore.common.stream.{AsyncUrlFetchFunction, FetchSuccessSplitter}
import org.yanislavcore.crawler.stream.{MetGloballyChecker, ScheduledUrlDeserializer}

object FlinkHelpers {
  /** Type Information, required by Flink */
  implicit val scheduledUrlTypeInfo: TypeInformation[ScheduledUrlData] =
    ScheduledUrlDeserializer.getProducedType
  implicit val fetchedDataTypeInfo: TypeInformation[(ScheduledUrlData, Either[FetchFailureData, FetchSuccessData])] =
    AsyncUrlFetchFunction.getProducedType
  implicit val successFetchTypeInfo: TypeInformation[(ScheduledUrlData, FetchSuccessData)] =
    TypeExtractor.getForClass(classOf[(ScheduledUrlData, FetchSuccessData)])
  implicit val stringTypeInfo: TypeInformation[String] =
    TypeExtractor.getForClass(classOf[String])
  implicit val clusterCheckerTypeInfo: TypeInformation[(ScheduledUrlData, Boolean)] =
    MetGloballyChecker.getProducedType
  implicit val fetchFailureTypeInfo: TypeInformation[FailedUrlData] =
    FetchSuccessSplitter.getProducedSideType

  val ArtifactTag: OutputTag[ScheduledUrlData] = OutputTag[ScheduledUrlData]("ArtifactStream")
}
