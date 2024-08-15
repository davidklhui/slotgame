
Currently support single game by providing slot configuration and the payout definition to perform a single game simulation.
This JSON maybe messed up if you browse here. You can view it beautified if you download and open in an editor

the sample payload is:

```json
{
    "cost": 10,
    "slot": {
        "numberOfRows": 2,
        "reels": [
            {"symbols": [
                    {"id": 1, "name": "apple", "probability": 0.8},
                    {"id": 2, "name": "orange", "probability": 0.1},
                    {"id": 3, "name": "lemon", "probability": 0.1}
            ]},
            {"symbols": [
                    {"id": 1, "name": "apple", "probability": 0.9},
                    {"id": 2, "name": "orange", "probability": 0.05},
                    {"id": 3, "name": "lemon", "probability": 0.05}
            ]},
            {"symbols": [
                {"id": 1, "name": "apple", "probability": 0.75},
                {"id": 2, "name": "orange", "probability": 0.15},
                {"id": 3, "name": "lemon", "probability": 0.1}
           ]}
        ]
    },
    "payoutDefinitions": [
        {
            "payoutId": 1, 
            "payoutAmount": 10,
            "payline": {
                "paylineId": 1,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 0}, 
                    {"reelIndex": 1, "rowIndex": 0},
                    {"reelIndex": 2, "rowIndex": 0}
                ]
            },            
            "symbols": [{"id": 1}, {"id": 1}, {"id": 1}]
        },
        {
            "payoutId": 2, 
            "payoutAmount": 20,
            "payline": {
                "paylineId": 1,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 0}, 
                    {"reelIndex": 1, "rowIndex": 0},
                    {"reelIndex": 2, "rowIndex": 0}
                ]
            },            
            "symbols": [{"id": 2}, {"id": 2}, {"id": 2}]
        },
         {
            "payoutId": 3, 
            "payoutAmount": 30,
            "payline": {
                "paylineId": 1,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 0}, 
                    {"reelIndex": 1, "rowIndex": 0},
                    {"reelIndex": 2, "rowIndex": 0}
                ]
            },            
            "symbols": [{"id": 3}, {"id": 3}, {"id": 3}]
        },
        {
            "payoutId": 4, 
            "payoutAmount": 100,
            "payline": {
                "paylineId": 1,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 1}, 
                    {"reelIndex": 1, "rowIndex": 1},
                    {"reelIndex": 2, "rowIndex": 1}
                ]
            },            
            "symbols": [{"id": 1}, {"id": 1}, {"id": 1}]
        },
         {
            "payoutId": 5, 
            "payoutAmount": 200,
            "payline": {
                "paylineId": 2,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 1}, 
                    {"reelIndex": 1, "rowIndex": 1},
                    {"reelIndex": 2, "rowIndex": 1}
                ]
            },            
            "symbols": [{"id": 2}, {"id": 2}, {"id": 2}]
        },
         {
            "payoutId": 6, 
            "payoutAmount": 300,
            "payline": {
                "paylineId": 2,
                "paylineCoordinates": [
                    {"reelIndex": 0, "rowIndex": 1}, 
                    {"reelIndex": 1, "rowIndex": 1},
                    {"reelIndex": 2, "rowIndex": 1}
                ]
            },            
            "symbols": [{"id": 3}, {"id": 3}, {"id": 3}]
        }
    ]
}


And the sample result is (Note that you may have different result because of the randomness)

{
    "spinOutcomes": [
        [
            {
                "id": 1,
                "name": "apple",
                "wild": false
            },
            {
                "id": 1,
                "name": "apple",
                "wild": false
            }
        ],
        [
            {
                "id": 1,
                "name": "apple",
                "wild": false
            },
            {
                "id": 1,
                "name": "apple",
                "wild": false
            }
        ],
        [
            {
                "id": 1,
                "name": "apple",
                "wild": false
            },
            {
                "id": 3,
                "name": "lemon",
                "wild": false
            }
        ]
    ],
    "payouts": [
        {
            "payoutDefinition": {
                "payoutId": 1,
                "payline": {
                    "paylineId": 1,
                    "paylineCoordinates": [
                        {
                            "reelIndex": 0,
                            "rowIndex": 0
                        },
                        {
                            "reelIndex": 1,
                            "rowIndex": 0
                        },
                        {
                            "reelIndex": 2,
                            "rowIndex": 0
                        }
                    ]
                },
                "symbols": [
                    {
                        "id": 1,
                        "name": "1",
                        "wild": false
                    },
                    {
                        "id": 1,
                        "name": "1",
                        "wild": false
                    },
                    {
                        "id": 1,
                        "name": "1",
                        "wild": false
                    }
                ],
                "payoutAmount": 10
            },
            "payoutAmount": 10
        }
    ],
    "spinPayout": 10
}
```
