
INSERT INTO slot (slot_id, number_of_rows, number_of_reels, slot_name, description) VALUES
(1, 3, 5, 'Demo 1', 'Demo 5x3 slot design');

INSERT INTO reel (reel_id, slot_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1);

INSERT INTO reel_symbol (id, reel_id, symbol_id, probability) VALUES
(1, 1, 1, 0.9),
(2, 1, 2, 0.05),
(3, 1, 3, 0.05),
(4, 2, 1, 0.8),
(5, 2, 2, 0.1),
(6, 2, 3, 0.1),
(7, 3, 1, 0.7),
(8, 3, 2, 0.2),
(9, 3, 3, 0.05),
(10, 3, 4, 0.05),
(11, 4, 1, 0.7),
(12, 4, 2, 0.1),
(13, 4, 3, 0.1),
(14, 4, 4, 0.05),
(15, 4, 5, 0.05),
(16, 5, 1, 0.9),
(17, 5, 2, 0.05),
(18, 5, 3, 0.05);


