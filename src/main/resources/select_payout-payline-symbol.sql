
USE slotgame;

SELECT p1.payout_id,
				p1.payout_amount,
				p1.payline_id,
                p2.payline_name,
                ps.symbol_id,
                s.symbol_name,
                s.is_wild
FROM payout as p1
	INNER JOIN payline as p2 ON p1.payline_id = p2.payline_id
    INNER JOIN payout_symbols as ps ON p1.payout_id = ps.payout_id
    INNER JOIN symbol as s ON ps.symbol_id = s.symbol_id


