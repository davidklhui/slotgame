package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SymbolException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *  The Symbol class:
 *      used to provide definition of a symbol
 *      e.g.: define an apple symbol with probability = 0.3, and is not a wild symbol:
 *
 *      Symbol appleSymbol = new Symbol(1, "apple", 0.3, false);
 *
 *      Throws SymbolException if the name is null / empty; or the probability fall outside valid range
 *
 *  After migrated to RDBMS, symbol definition (id,name, isWild) should be in one table
 *  where the corresponding probability and associated reel is in another table
 *
 *  Currently, this setup is adequate
 */
@ToString
@JsonDeserialize(using = SymbolDeserializer.class)
public class Symbol {

    private static final int SCALE = 9;

    // id of the symbol
    // this id is used to determine outcome pattern
    // same id implies the same symbol regardless of the other fields
    @Getter
    private final int symbolId;

    // display name of the symbol
    @Getter
    private final String symbolName;

    // probability of occurrence of symbol, must be between 0 & 1
    @Setter
    private BigDecimal probability;


    // wild symbol declaration for later use
    @Getter
    private final boolean isWild;

    public Symbol(final int symbolId,
                  final String symbolName,
                  final BigDecimal probability,
                  final boolean isWild){

        this.symbolId = symbolId;
        this.symbolName = StringUtils.trimToEmpty(symbolName);
        this.probability = probability.setScale(SCALE, RoundingMode.HALF_UP);
        this.isWild = isWild;

        if(this.symbolName.equals("")){
            throw new SymbolException("Invalid Name, either null or is an empty string");
        }

        if(! this.isProbabilityValid()){
            throw new SymbolException(
                    String.format("Invalid Probability value, must be between 0 and 1 inclusively, provided: %s", this.probability));
        }
    }

    public Symbol(final int symbolId,
                  final String symbolName,
                  final double probability,
                  final boolean isWild){

        this(symbolId, symbolName, BigDecimal.valueOf(probability), isWild);
    }


    public Symbol(final int symbolId,
                  final String symbolName,
                  final double probability){

        this(symbolId, symbolName, probability, false);
    }

    public Symbol(final int symbolId,
                  final String symbolName,
                  final boolean isWild){

        this(symbolId, symbolName, BigDecimal.ZERO, isWild);
    }

    public Symbol(final int symbolId,
                  final String symbolName){

        this(symbolId, symbolName, false);
    }

    public Symbol(final int symbolId){

        this(symbolId, String.valueOf(symbolId));
    }

    // manually define this here as we need to ignore from serialization to the JSON output
    @JsonIgnore
    public BigDecimal getProbability(){
        return this.probability;
    }


    // check if probability is not null, then check 0 <= p <= 1
    private boolean isProbabilityValid(){

        if(this.probability == null){
            return false;
        }

        return this.probability.compareTo(BigDecimal.ZERO) >= 0
                && this.probability.compareTo(BigDecimal.ONE) <= 0;
    }

    // use IDE generated equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return symbolId == symbol.symbolId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbolId);
    }


}
