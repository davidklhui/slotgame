package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SlotException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@NoArgsConstructor
@Data
@Entity
@Table(name = "reel_symbol")
public class SymbolProb {

    // used to set scale for the BigDecimal
    private static final int SCALE = 9;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn(name = "symbol_id", nullable = false,
                referencedColumnName = "symbol_id")
    private Symbol symbol;

    @ManyToOne
    @JoinColumn(name = "reel_id", nullable = false)
    @JsonIgnore
    private Reel reel;

    @Column(name = "probability", precision = 10, scale = 10)
    private BigDecimal probability;

    @JsonCreator
    public SymbolProb(@JsonProperty("symbol") final Symbol symbol,
                      @JsonProperty("probability") final BigDecimal probability){
        this.symbol = symbol;
        this.probability = probability;

        if(! isProbabilityValid()){
            throw new SlotException(
                    String.format("Invalid Probability: given value=%s", probability)
            );
        }
    }

    public SymbolProb(final Symbol symbol,
                      final double probability){

        this(symbol, BigDecimal.valueOf(probability).setScale(SCALE, RoundingMode.HALF_UP));
    }


    // check if probability is not null, then check 0 <= p <= 1
    private boolean isProbabilityValid(){

        if(this.probability == null){
            return false;
        }

        return this.probability.compareTo(BigDecimal.ZERO) >= 0
                && this.probability.compareTo(BigDecimal.ONE) <= 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolProb that = (SymbolProb) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }
}
