INSERT INTO waste_category (name) VALUES    -- 대분류
                                      ('soft_textile'),
                                      ('sharps'),
                                      ('tubes_vials'),
                                      ('other');

INSERT INTO waste_type (name, unit, category_id) VALUES   -- 소분류
-- soft_textile
('Cotton', 'kg', 1),
('Bandage', 'kg', 1),
('Gloves', 'pair', 1),
('Mask', 'piece', 1),
('Medical Cap', 'piece', 1),
-- sharps
('Needle', 'piece', 2),
('Scissors', 'piece', 2),
('Syringe', 'piece', 2),
-- tubes_vials
('IV Tube', 'piece', 3),
('Test Tube', 'piece', 3),
('Vial', 'piece', 3),
-- other
('General Waste', 'kg', 4);