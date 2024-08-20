package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SlotException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Data
@Entity
@Table(name = "slot")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Slot {

    /**
         Defines the Slot physical definition
         1. Reels
         2. Number of Rows
         3. name
         4. description
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Integer slotId;

    @Column(name = "number_of_rows")
    private int numberOfRows;

    @Column(name = "slot_name")
    private String slotName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "slot", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Reel> reels;


    @JsonCreator
    public Slot(@JsonProperty("reels") final List<Reel> reels,
                @JsonProperty("numberOfRows") final int numberOfRows,
                @JsonProperty("slotName") final String slotName,
                @JsonProperty("description") final String description){

        if(reels == null){
            throw new SlotException("Incorrect Slot configuration: given reels is null");
        }
        if(reels.size() < 3){
            throw new SlotException(
                    String.format("Incorrect Slot configuration, reels size <3, given %d", reels.size()));
        }
        if(numberOfRows <= 0){
            throw new SlotException(
                    String.format("Incorrect Slot configuration: number of rows is invalid: give %d", numberOfRows));
        }

        this.reels = reels;
        this.numberOfRows = numberOfRows;
        this.slotName = slotName;
        this.description = description;

    }


    // get the number of reels
    public int getNumberOfReels(){
        return this.reels.size();
    }

    // find out configured symbols in the slot machine
    public Set<Symbol> distinctSymbols(){
        return this.reels.stream()
                .flatMap(x-> x.getSymbolProbSet().stream())
                .map(SymbolProb::getSymbol)
                .collect(Collectors.toSet());
    }

    // get number of different symbols configured in the slot machine
    public int numberOfDistinctSymbols(){
        return this.distinctSymbols().size();
    }

    /* perform simulations (a spin)
     * for each reel -> perform a simulation of the given size
     * then collect them
     *
     * suppose there are 5 reels with 3 rows
     * this will result in an outer-list have 5 items, with inner-list have 3 items
     */
    public List<List<Symbol>> spin(){

        return this.reels.stream()
                .map(reel-> reel.simulate(numberOfRows))
                .toList();

    }

}
