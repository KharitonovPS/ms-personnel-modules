CREATE EXTENSION pg_trgm;
CREATE EXTENSION btree_gin;

CREATE INDEX person_name_gin ON person USING gin (name);

SELECT * FROM person WHERE name ilike '%new%' order by name DESC LIMIT 1