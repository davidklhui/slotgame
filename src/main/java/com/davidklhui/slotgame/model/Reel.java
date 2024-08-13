package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.ReelException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 *  The Reel class:
 *      used to provide definition of a reel
 *      1. required to have set of symbols
 *
 */
@ToString
@Getter
public class Reel {

    // used to set scale for the BigDecimal
    private static final int SCALE = 9;

    // used to check if the total probability is just slightly differs from 1
    private static final BigDecimal TOLERANCE = BigDecimal.valueOf(1e-9);

    private final Set<Symbol> symbols;

    @JsonCreator
    public Reel(@JsonProperty("symbols") final Set<Symbol> symbols){
        this.symbols = symbols;

        if(! isValidReelSize()){
            throw new ReelException("Symbols set is invalid, either null or having size < 2");
        }

        adjustProbabilities();

    }

    public List<Symbol> simulate(final int size){

        if(size <= 0) throw new ReelException(String.format("Sim Size must be at least 1, given %d", size));

        final EnumeratedDistribution<Symbol> distribution = new EnumeratedDistribution<>(symbolProbabilityPair());

        return Stream.of(distribution.sample(size))
                .map(x-> (Symbol) x)
                .toList();
    }


    // convert to math3 Pair for sampling
    private List<Pair<Symbol, Double>> symbolProbabilityPair(){
        return this.symbols.stream()
                .map(symbol-> new Pair<>(symbol, symbol.getProbability().doubleValue()))
                .toList();
    }

    // avoid too messy in the constructor
    // this method is to check and adjust the probabilities
    private void adjustProbabilities(){

        /* check if the sum is precisely sum to 1
         *  if not, check if it is approximately sum to 1
         *      if yes -> put the difference value into any symbol (which is extremely small amount only)
         *      if no -> throws exception
         */
        if(! isTotalProbabilitySumToOne()){

            // check |1 - total_probs| < epsilon
            if(isTotalProbabilityApproximatelySumToOne()){
                final BigDecimal difference = totalProbabilityDifference();

                // extract one symbol from the reel symbols
                // noted that this must not be null, as construction of symbols must have size >= 2
                final Symbol selectedSymbol = symbols.iterator().next();

                // adjust the difference to the selected symbol
                // the difference must be extremely small, can be seen as negligible
                selectedSymbol.setProbability(selectedSymbol.getProbability().add(difference));
            } else {
                throw new ReelException(
                        String.format("Reel's symbols set total probability is invalid, sum to %s", totalProbability()));
            }

        }

    }

    // check if number of configured symbols are at least 2
    // otherwise it does not make any sense because the specific reel always spin the same symbol
    private boolean isValidReelSize(){

        if(this.symbols == null){
            return false;
        }
        return this.symbols.size() >= 2;
    }


    // calculate total probability from the symbol set
    private BigDecimal totalProbability(){

        final Optional<BigDecimal> totalProbs = this.symbols.stream()
                .map(Symbol::getProbability)
                .reduce(BigDecimal::add);

        return totalProbs.orElseThrow(
                ()-> new ReelException("Exception during total probability calculations")
        );

    }


    // check if the sum of probabilities is 1
    // return true if |total_prob - 1| < epsilon
    // it is used to prevent rounding errors especially when the prob is from Dirichlet distribution
    private boolean isTotalProbabilitySumToOne(){

        return totalProbability()
                .equals(BigDecimal.ONE.setScale(SCALE));

    }


    // check if the sum of probabilities is 1
    // return true if total_prob == 1
    private boolean isTotalProbabilityApproximatelySumToOne(){

        return totalProbabilityDifference().abs()
                .compareTo(TOLERANCE) <= 0;

    }

    // calculate the difference
    // 1 - total probability
    // can be negative if the total probability is 'slightly' larger than 1, say 1.0000000001
    // then the difference is -0.0000000001
    private BigDecimal totalProbabilityDifference(){

        return BigDecimal.ONE.setScale(SCALE)
                                .subtract(totalProbability());

    }


}
