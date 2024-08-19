package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.ReelException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  The Reel class:
 *      used to provide definition of a reel
 *      1. required to have set of symbols
 *
 */
@ToString
@Entity
@Table(name = "reel")
@Data
@NoArgsConstructor
public class Reel {

    // used to set scale for the BigDecimal
    private static final int SCALE = 9;

    // used to check if the total probability is just slightly differs from 1
    private static final BigDecimal TOLERANCE = BigDecimal.valueOf(1e-9).setScale(SCALE);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reel_id")
    private int id;

    /*
        defined the symbol-probability pair
     */
    @OneToMany(mappedBy = "reel", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<SymbolProb> symbolProbSet;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnore
    private Slot slot;

    @JsonCreator
    public Reel(@JsonProperty("symbolProbSet") final Set<SymbolProb> symbolProbSet){

        this.symbolProbSet = symbolProbSet;

        if(! isValidReelSize()){
            throw new ReelException("Symbols set is invalid, either null or having size < 2");
        }

        adjustProbabilities();
    }

    public Reel(final Map<Symbol, BigDecimal> symbolProbMap){

        this(symbolProbMap.entrySet()
                .stream()
                .map(entry-> new SymbolProb(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet()));

    }

    /*
        helper class to handle the map to accept BigDecimal or Double without worrying about the scale issue
     */
    public static class SymbolMapBuilder {
        private Map<Symbol, BigDecimal> symbols = new HashMap<>();

        public SymbolMapBuilder put(final Symbol symbol, final BigDecimal probability){
            this.symbols.put(symbol, probability.setScale(SCALE, RoundingMode.HALF_UP));
            return this;
        }

        public SymbolMapBuilder put(final Symbol symbol, final Double probability){
            return put(symbol, BigDecimal.valueOf(probability));
        }

        public Set<SymbolProb> build(){
            final Set<SymbolProb> symbolProbSet = symbols.entrySet()
                    .stream()
                    .map(entry-> new SymbolProb(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());

            // clear existing put data to avoid further usage
            this.symbols.clear();

            return symbolProbSet;
        }


    }

    public int numberOfSymbols(){
        return symbolProbSet.size();
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
        return this.symbolProbSet.stream()
                .map(symbolProb-> new Pair<>(symbolProb.getSymbol(), symbolProb.getProbability().doubleValue()))
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
                final SymbolProb selectedSymbol = symbolProbSet.iterator().next();

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

        if(this.symbolProbSet == null){
            return false;
        }
        return this.symbolProbSet.size() >= 2;
    }


    // calculate total probability from the symbol set
    private BigDecimal totalProbability(){

        final Optional<BigDecimal> totalProbs = this.symbolProbSet.stream()
                .map(SymbolProb::getProbability)
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
