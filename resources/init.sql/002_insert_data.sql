INSERT INTO submission (id, account_name, lines_of_coverage, uw_name, premium_usd, state, effective_date, expiration_date, sic, status)
VALUES
  ('123e4567-e89b-12d3-a456-426614174001', 'Account1', ARRAY['Coverage1', 'Coverage2'], 'UW1', 100.0, 'Active', '2023-01-01', '2023-12-31', 'SIC123', 'Approved'),
  ('223e4567-e89b-12d3-a456-426614174002', 'Account2', ARRAY['Coverage3'], 'UW2', 150.0, 'Inactive', '2023-02-01', '2023-11-30', 'SIC456', 'Pending'),
