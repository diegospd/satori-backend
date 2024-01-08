CREATE TABLE submission (
  id UUID PRIMARY KEY,
  account_name VARCHAR NOT NULL,
  lines_of_coverage TEXT[] NOT NULL,
  uw_name VARCHAR NOT NULL,
  premium_usd NUMERIC NOT NULL,
  state VARCHAR NOT NULL,
  effective_date TIMESTAMP NOT NULL,
  expiration_date TIMESTAMP NOT NULL,
  sic VARCHAR NOT NULL,
  status VARCHAR NOT NULL,
  inserted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_submission_account_name ON submission(account_name);
CREATE INDEX idx_submission_uw_name ON submission(uw_name);
CREATE INDEX idx_submission_premium_usd ON submission(premium_usd);
CREATE INDEX idx_submission_effective_date ON submission(effective_date);
CREATE INDEX idx_submission_expiration_date ON submission(expiration_date);
