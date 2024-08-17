

Currently support single game by providing slot configuration and the payout definition to perform a single game simulation


This sample slot game using 3 reels x 2 rows as a demo
In the general design this can be extended

the sample payload is:

```json
{
  "cost": 10,
  "slot": {
    "numberOfRows": 2,
    "reels": [
      {"symbols": [
        {"id": 1, "name": "apple", "probability": 0.9},
        {"id": 2, "name": "orange", "probability": 0.05},
        {"id": 3, "name": "lemon", "probability": 0.05}
      ]},
      {"symbols": [
        {"id": 1, "name": "apple", "probability": 0.1},
        {"id": 2, "name": "orange", "probability": 0.1},
        {"id": 3, "name": "lemon", "probability": 0.8}
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
        "paylineId": 1
      },
      "symbol": {"id": 1}
    },
    {
      "payoutId": 2,
      "payoutAmount": 100,
      "payline": {
        "paylineId": 2
      },
      "symbol": {"id": 2}
    },
    {
      "payoutId": 3,
      "payoutAmount": 1000,
      "payline": {
        "paylineId": 3
      },
      "symbol": {"id": 3}
    },
    {
      "payoutId": 4,
      "payoutAmount": 10000,
      "payline": {
        "paylineId": 4
      },
      "symbol": {"id": 1}
    },
    {
      "payoutId": 5,
      "payoutAmount": 20,
      "payline": {
        "paylineId": 2
      },
      "symbol": {"id": 2}
    },
    {
      "payoutId": 6,
      "payoutAmount": 30,
      "payline": {
        "paylineId": 3
      },
      "symbols": [{"id": 1}, {"id": 3}, {"id": 1}]
    }
  ]
}
```

And the sample result is (Note that you may have different result because of the randomness)
```json
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
        "id": 3,
        "name": "lemon",
        "wild": false
      }
    ],
    [
      {
        "id": 2,
        "name": "orange",
        "wild": false
      },
      {
        "id": 1,
        "name": "apple",
        "wild": false
      }
    ]
  ],
  "slotSpinPayouts": [
    {
      "payoutDefinition": {
        "payoutId": 4,
        "payline": {
          "paylineId": 4,
          "paylineName": "inverted v shape",
          "paylineCoordinates": [
            {
              "reelIndex": 0,
              "rowIndex": 1
            },
            {
              "reelIndex": 1,
              "rowIndex": 0
            },
            {
              "reelIndex": 2,
              "rowIndex": 1
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
        "payoutAmount": 10000
      },
      "payoutAmount": 10000
    }
  ],
  "spinPayout": 10000
}
```

Payline configurations check the following endpoint (currently are manual pre-defined data)

<pre><code>GET /payline/list</code></pre>

and the results:
```json
[
    {
        "paylineId": 1,
        "paylineName": "horizontal line 1",
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
    {
        "paylineId": 2,
        "paylineName": "horizontal line 2",
        "paylineCoordinates": [
            {
                "reelIndex": 0,
                "rowIndex": 1
            },
            {
                "reelIndex": 1,
                "rowIndex": 1
            },
            {
                "reelIndex": 2,
                "rowIndex": 1
            }
        ]
    },
    {
        "paylineId": 3,
        "paylineName": "v shape",
        "paylineCoordinates": [
            {
                "reelIndex": 0,
                "rowIndex": 0
            },
            {
                "reelIndex": 1,
                "rowIndex": 1
            },
            {
                "reelIndex": 2,
                "rowIndex": 0
            }
        ]
    },
    {
        "paylineId": 4,
        "paylineName": "inverted v shape",
        "paylineCoordinates": [
            {
                "reelIndex": 0,
                "rowIndex": 1
            },
            {
                "reelIndex": 1,
                "rowIndex": 0
            },
            {
                "reelIndex": 2,
                "rowIndex": 1
            }
        ]
    }
]
```
