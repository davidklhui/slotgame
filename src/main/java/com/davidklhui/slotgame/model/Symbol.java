package com.davidklhui.slotgame.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Table(name = "symbol")
@Entity
@Data
@NoArgsConstructor
@ToString
public class Symbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "symbol_id")
    private int symbolId;

    // display name of the symbol
    @Column(name = "symbol_name")
    private String symbolName;

    // wild symbol declaration for later use
    @Column(name = "is_wild")
    private boolean isWild;


    @JsonCreator
    public Symbol(@JsonProperty("symbolId") final int symbolId,
                  @JsonProperty("symbolName") final String symbolName,
                  @JsonProperty("isWild") final boolean isWild){
        this.symbolId = symbolId;
        this.symbolName = symbolName;
        this.isWild = isWild;
    }

    public Symbol(final int symbolId,
                  final String symbolName){

        this(symbolId, symbolName, false);
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
