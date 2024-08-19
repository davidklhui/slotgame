package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/*
    PayoutDefinition defines
    1. the payoutAmount if matched
    2. the payline definition (in what coordinates)
    3. what symbol orders is matched
    4. the payout amount
 */
@Entity
@Table(name = "payout")
@NoArgsConstructor
@Data
@JsonDeserialize(using = PayoutDefinitionDeserializer.class)
public class PayoutDefinition {

    // payout definition id
    @Id
    @Column(name = "payout_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payoutId;

    @ManyToOne
    @JoinColumn(name = "payline_id", nullable = false)
    private Payline payline;

    // required sequence of symbols, should align with the order of payline coordinates
    @ManyToMany
    @JoinTable(
            name = "payout_symbols",
            joinColumns = @JoinColumn(name = "payout_id"),
            inverseJoinColumns = @JoinColumn(name = "symbol_id")
    )
    private List<Symbol> symbols;

    // payout if matched
    @Column(name = "payout_amount")
    private int payoutAmount;

    // payout if only matched wild symbols
    // tbc

    @JsonCreator
    public PayoutDefinition(@JsonProperty("payoutId") final int payoutId,
                            @JsonProperty("payline")final Payline payline,
                            @JsonProperty("symbols")final List<Symbol> symbols,
                            @JsonProperty("payoutAmount")final int payoutAmount) {

        this.payoutId = payoutId;
        this.payline = payline;
        this.symbols = symbols;
        this.payoutAmount = payoutAmount;
    }

    public PayoutDefinition(final int payoutId,
                            final Payline payline,
                            final Symbol symbol,
                            final int payoutAmount) {

        this(payoutId,
                payline,
                Collections.nCopies(payline.getCoordinates().size(), symbol),
                payoutAmount);

    }



}
