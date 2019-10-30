package submit;

// some useful things to import. add any additional imports you need.
import joeq.Compiler.Quad.*;
import flow.Flow;

import java.util.Iterator;
import java.util.Queue;

/**
 * Skeleton class for implementing the Flow.Solver interface.
 */
public class MySolver implements Flow.Solver {

    protected Flow.Analysis analysis;

    /**
     * Sets the analysis.  When visitCFG is called, it will
     * perform this analysis on a given CFG.
     *
     * @param analyzer The analysis to run
     */
    public void registerAnalysis(Flow.Analysis analyzer) {
        this.analysis = analyzer;
    }

    /**
     * Runs the solver over a given control flow graph.  Prior
     * to calling this, an analysis must be registered using
     * registerAnalysis
     *
     * @param cfg The control flow graph to analyze.
     */
    public void visitCFG(ControlFlowGraph cfg) {

        // this needs to come first.
        analysis.preprocess(cfg);

        /***********************
         * Your code goes here *
         ***********************/
        boolean changed = true;
        if (analysis.isForward()){
            //set boundary condition
            while (changed) {
                // there are more than one exit blocks, therefore i need to compute the exit value additionally
                Flow.DataflowObject exitValue = analysis.newTempVar();
                exitValue.setToTop();

                changed = false;
                QuadIterator iter = new QuadIterator(cfg, true);
                while ((iter.hasNext())){
                    Quad current = iter.next();

                    // compute the IN[B] = meet(OUT[P])
                    Flow.DataflowObject blockIn = analysis.newTempVar();
                    blockIn.setToTop();
                    Iterator<Quad> fores = iter.predecessors();
                    while (fores.hasNext()){
                        Quad fore = fores.next();
                        blockIn.meetWith(analysis.getOut(fore));
                    }
                    analysis.setIn(current, blockIn);

                    Flow.DataflowObject beforeFB = analysis.newTempVar();
                    beforeFB.copy(analysis.getOut(current));
                    // OUT[B] = fB(IN[B])
                    analysis.processQuad(current);
                    if (! beforeFB.equals(analysis.getOut(current)))
                        changed = true;

                    // additionally compute the exit value
                    Iterator<Quad> nexts = iter.successors();
                    while (nexts.hasNext()){
                        Quad tmp = nexts.next();
                        if (tmp == null)
                            exitValue.meetWith(analysis.getOut(current));
                    }

                }
                analysis.setExit(exitValue);
            }
        }else {

        }

        // this needs to come last.
        analysis.postprocess(cfg);
    }
}
