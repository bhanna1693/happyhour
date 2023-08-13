-- Create the Business table
CREATE TABLE business (
                          id SERIAL PRIMARY KEY,
                          yelp_id VARCHAR(255) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          website VARCHAR(255) NOT NULL,
                          last_check_for_specials TIMESTAMPTZ,
                          special_status VARCHAR(255),
                          created_at TIMESTAMPTZ NOT NULL,
                          updated_at TIMESTAMPTZ,
                          CONSTRAINT unique_yelp_id UNIQUE (yelp_id)
);

-- Create the SpecialDetail table
CREATE TABLE special_detail (
                                id UUID PRIMARY KEY,
                                business_id INT REFERENCES business(id) ON DELETE CASCADE,
                                type VARCHAR(255),
                                day VARCHAR(255),
                                time VARCHAR(255),
                                details VARCHAR(255),
                                created_at TIMESTAMPTZ NOT NULL,
                                updated_at TIMESTAMPTZ,
                                FOREIGN KEY (business_id) REFERENCES business(id)
);
