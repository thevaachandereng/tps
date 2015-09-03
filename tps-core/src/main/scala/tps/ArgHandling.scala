package tps

import tps.util.LogUtils

import java.io.File

object ArgHandling {
  def parseOptions(args: Array[String]) = {
    val opts = new Options()
    ArgHandling.parser.parse(args, opts) getOrElse {
      LogUtils.terminate("Bad arguments.")
    }
  }

  private def parser = {
    new scopt.OptionParser[Options]("tps") {
      head("tps", "2.0")

      // Options:

      opt[String]("network") required() action { (v, o) =>
        o.copy(networkPath = new java.io.File(v))
      } text("input network file")

      opt[String]("timeseries") required() action { (v, o) =>
        o.copy(timeSeriesPath = new java.io.File(v))
      } text("input time series file")

      opt[String]("firstscores") required() action { (v, o) =>
        o.copy(firstScoresPath = new java.io.File(v))
      } text("significance scores for time series points w.r.t. first time point")

      opt[String]("prevscores") required() action { (v, o) =>
        o.copy(prevScoresPath = new java.io.File(v))
      } text("significance scores for time series points w.r.t. previous time point")

      opt[String]("partialmodel") unbounded() action { (v, o) =>
        o.copy(partialModelPaths = o.partialModelPaths + new java.io.File(v))
      } text("input partial model given as a network")

      opt[String]("peptidemap") action { (v, o) =>
        o.copy(peptideProteinMapPath = Some(new java.io.File(v))) 
      } text("peptide protein mapping")

      opt[String]("outlabel") action { (v, o) =>
        o.copy(outLabel = Some(v)) 
      } text("prefix that will be added to output file names")

      opt[String]("outfolder") action { (v, o) =>
        o.copy(outFolder = new java.io.File(v)) 
      } text("folder that output files should be created in")

      opt[String]("source") unbounded() action { (v, o) =>
        o.copy(sources = o.sources + v) 
      } text("network source")

      opt[Double]("threshold") required() action { (d, o) =>
        o.copy(significanceThreshold = d) 
      } text("significance score threshold")

      // SynthesisOptions:

      opt[String]("solver") action { (v, o) =>
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(solver = v)) } text(
          "solver (naive, bilateral or dataflow)")

      opt[Int]("slack") action { (i, o) =>
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(pathLengthSlack = Some(i))) } text(
          "when using a symbolic solver, limit path lengths from source to each nodes to k + shortest path")

      opt[Int]("bitvect") action { (i, o) => 
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(bitvectorWidth = Some(i))) } text(
          "use bitvectors for integer encoding")

      opt[Unit]("no-connectivity") action { (_, o) => 
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(constraintOptions = 
            o.synthesisOptions.constraintOptions.copy(
              connectivity = false))) } text(
          "do not assert conectivity constraints")

      opt[Unit]("no-temporality") action { (_, o) => 
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(constraintOptions = 
            o.synthesisOptions.constraintOptions.copy(
              temporality = false))) } text(
          "do not assert temporal constraints")

      opt[Unit]("no-monotonicity") action { (_, o) => 
        o.copy(synthesisOptions = 
          o.synthesisOptions.copy(constraintOptions = 
            o.synthesisOptions.constraintOptions.copy(
              monotonicity = false))) } text(
          "do not assert monotonicity constraints")

      help("help") text("print this help message")
    }
  }
}
