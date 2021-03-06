package tps.synthesis

import tps.Graphs._

import tps.util.LogUtils

class NaiveSymbolicSolver(
  graph: UndirectedGraph,
  partialModel: SignedDirectedGraph,
  opts: SynthesisOptions,
  interpretation: Interpretation
) extends AbstractSymbolicSolver(graph, partialModel, opts, interpretation) {
  private def testSolution(
    sg: SymbolicGraph, 
    si: SymbolicInterpretation, 
    e: Edge, 
    es: SignedDirectedEdgeLabel
  ): Boolean = {
    z3Solver.push

    val toCheck = Map(e -> Set(es))
    val formulaToCheck = sg.graphSolutionFormula(toCheck)
    assertExpr(formulaToCheck)

    val sw = new tps.util.Stopwatch(s"Testing: $e for $es", false).start
    val result = z3Solver.check match {
      case Some(true) => true
      case _ => false
    }
    sw.stop

    z3Solver.pop(1)

    result
  }

  def summary(): SignedDirectedGraph = {
    restart()
    val (sg, si, validModelFla) = createSymbolicGraphInterpretation()
    assertExpr(validModelFla)

    var solution: SignedDirectedGraph = Map.empty

    val total = graph.E.size
    LogUtils.log(s"$total edges to test")
    var ctr = 0

    for (e <- graph.bfsEdgeOrder) {
      var possibleSols = activeEdgeLabelChoices filter {
        es => ctr += 1; testSolution(sg, si, e, es)
      }

      val newPartialModel = Map(e -> possibleSols)
      solution ++= newPartialModel

      // assert what's already known
      assertExpr(sg.graphSolutionFormula(newPartialModel))
    }
    LogUtils.log("# queries: " + ctr)

    solution
  }
}
