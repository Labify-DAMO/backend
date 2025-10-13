INSERT INTO waste_category (name) VALUES    -- 대분류
                                      ('soft_textile'),
                                      ('sharps'),
                                      ('tubes_vials'),
                                      ('other');

INSERT INTO waste_type (code, name, unit, category_id) VALUES   -- 소분류
-- soft_textile
('cotton', 'Cotton', 'kg', 1),
('bandage', 'Bandage', 'kg', 1),
('gloves', 'Gloves', 'pair', 1),
('mask', 'Mask', 'piece', 1),
('medical_cap', 'Medical Cap', 'piece', 1),
-- sharps
('needle', 'Needle', 'piece', 2),
('scissors', 'Scissors', 'piece', 2),
('syringe', 'Syringe', 'piece', 2),
-- tubes_vials
('ivtube', 'IV Tube', 'piece', 3),
('test_tube', 'Test Tube', 'piece', 3),
('vial', 'Vial', 'piece', 3),
-- other
('waste', 'General Waste', 'kg', 4);