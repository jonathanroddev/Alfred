DROP SCHEMA IF EXISTS alfred_v1 CASCADE;

CREATE SCHEMA alfred_v1 AUTHORIZATION username;


DROP EXTENSION IF EXISTS pgcrypto;

CREATE EXTENSION pgcrypto;



-- plans

CREATE TABLE alfred_v1.plans (
    "name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.plans OWNER TO username;
GRANT ALL ON TABLE alfred_v1.plans TO username;



-- communities

CREATE TABLE alfred_v1.communities (
    "uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	"name" VARCHAR(50) UNIQUE,
	"plan" VARCHAR(50) NOT NULL REFERENCES alfred_v1.plans (name),
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.communities OWNER TO username;
GRANT ALL ON TABLE alfred_v1.communities TO username;



-- resources

CREATE TABLE alfred_v1.resources (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.resources OWNER TO username;
GRANT ALL ON TABLE alfred_v1.resources TO username;



-- plans_resources

CREATE TABLE alfred_v1.plans_resources (
	"plan" VARCHAR(50) NOT NULL REFERENCES alfred_v1.plans (name),
    "resource" VARCHAR(50) NOT NULL REFERENCES alfred_v1.resources (name),
	UNIQUE ("resource", "plan")
);

ALTER TABLE alfred_v1.plans_resources OWNER TO username;
GRANT ALL ON TABLE alfred_v1.plans_resources TO username;



-- operations

CREATE TABLE alfred_v1.operations (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.operations OWNER TO username;
GRANT ALL ON TABLE alfred_v1.operations TO username;



-- permissions

CREATE TABLE alfred_v1.permissions (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "resource" VARCHAR(50) NOT NULL REFERENCES alfred_v1.resources (name),
    "operation" VARCHAR(50) NOT NULL REFERENCES alfred_v1.operations (name),
	UNIQUE ("resource", "operation")
);

ALTER TABLE alfred_v1.permissions OWNER TO username;
GRANT ALL ON TABLE alfred_v1.permissions TO username;



-- roles

CREATE TABLE alfred_v1.roles (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(50) UNIQUE,
    "_created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "_created_by" VARCHAR(255) NOT NULL,
	"_updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"_updated_by" VARCHAR(255) NOT NULL,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text)),
	CONSTRAINT check_empty_created_by CHECK ((TRIM(BOTH FROM _created_by) <> ''::text)),
	CONSTRAINT check_empty_updated_by CHECK ((TRIM(BOTH FROM _updated_by) <> ''::text))
);

ALTER TABLE alfred_v1.roles OWNER TO username;
GRANT ALL ON TABLE alfred_v1.roles TO username;



-- permission_roles

CREATE TABLE alfred_v1.permission_roles (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "permission" UUID NOT NULL REFERENCES alfred_v1.permissions (uuid),
    "role" UUID NOT NULL REFERENCES alfred_v1.roles (uuid)
);

ALTER TABLE alfred_v1.permission_roles OWNER TO username;
GRANT ALL ON TABLE alfred_v1.permission_roles TO username;



-- user_types

CREATE TABLE alfred_v1.user_types (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.user_types OWNER TO username;
GRANT ALL ON TABLE alfred_v1.user_types TO username;



-- user_status

CREATE TABLE alfred_v1.user_status (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

ALTER TABLE alfred_v1.user_status OWNER TO username;
GRANT ALL ON TABLE alfred_v1.user_status TO username;



-- users

CREATE TABLE alfred_v1.users (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "user_status" VARCHAR(50) NOT NULL REFERENCES alfred_v1.user_status (name),
    "community" UUID NOT NULL REFERENCES alfred_v1.communities (uuid),
    "_created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "_created_by" VARCHAR(255) NOT NULL,
	"_updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"_updated_by" VARCHAR(255) NOT NULL,
	CONSTRAINT check_empty_created_by CHECK ((TRIM(BOTH FROM _created_by) <> ''::text)),
	CONSTRAINT check_empty_updated_by CHECK ((TRIM(BOTH FROM _updated_by) <> ''::text))
);

ALTER TABLE alfred_v1.users OWNER TO username;
GRANT ALL ON TABLE alfred_v1.users TO username;



-- user_roles

CREATE TABLE alfred_v1.user_roles (
    "user" UUID NOT NULL REFERENCES alfred_v1.users (uuid),
    "role" UUID NOT NULL REFERENCES alfred_v1.roles (uuid)
);

ALTER TABLE alfred_v1.user_roles OWNER TO username;
GRANT ALL ON TABLE alfred_v1.user_roles TO username;



-- user_groups

CREATE TABLE alfred_v1.user_groups (
    "user" UUID NOT NULL REFERENCES alfred_v1.users (uuid),
    "user_type" VARCHAR(50) NOT NULL REFERENCES alfred_v1.user_types (name)
);

ALTER TABLE alfred_v1.user_groups OWNER TO username;
GRANT ALL ON TABLE alfred_v1.user_groups TO username;


-- DB Auth

GRANT ALL ON SCHEMA alfred_v1 TO username;