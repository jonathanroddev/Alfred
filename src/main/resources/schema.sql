CREATE SCHEMA IF NOT EXISTS alfred_v1;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- plans

CREATE TABLE IF NOT EXISTS alfred_v1.plans (
    "name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- communities

CREATE TABLE IF NOT EXISTS alfred_v1.communities (
    "uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	"name" VARCHAR(50) UNIQUE,
	"plan" VARCHAR(50) NOT NULL REFERENCES alfred_v1.plans (name),
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- resources

CREATE TABLE IF NOT EXISTS alfred_v1.resources (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- plans_resources

CREATE TABLE IF NOT EXISTS alfred_v1.plans_resources (
	"plan" VARCHAR(50) NOT NULL REFERENCES alfred_v1.plans (name),
    "resource" VARCHAR(50) NOT NULL REFERENCES alfred_v1.resources (name),
	UNIQUE ("resource", "plan")
);

-- operations

CREATE TABLE IF NOT EXISTS alfred_v1.operations (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- permissions

CREATE TABLE IF NOT EXISTS alfred_v1.permissions (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "resource" VARCHAR(50) NOT NULL REFERENCES alfred_v1.resources (name),
    "operation" VARCHAR(50) NOT NULL REFERENCES alfred_v1.operations (name),
	UNIQUE ("resource", "operation")
);

-- roles

CREATE TABLE IF NOT EXISTS alfred_v1.roles (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(50),
    "community" UUID NOT NULL REFERENCES alfred_v1.communities (uuid),
    "_created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "_created_by" VARCHAR(255) NOT NULL,
	"_updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"_updated_by" VARCHAR(255) NOT NULL,
	UNIQUE ("name", "community"),
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text)),
	CONSTRAINT check_empty_created_by CHECK ((TRIM(BOTH FROM _created_by) <> ''::text)),
	CONSTRAINT check_empty_updated_by CHECK ((TRIM(BOTH FROM _updated_by) <> ''::text))
);

-- permission_roles

CREATE TABLE IF NOT EXISTS alfred_v1.permission_roles (
	"uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "permission" UUID NOT NULL REFERENCES alfred_v1.permissions (uuid),
    "role" UUID NOT NULL REFERENCES alfred_v1.roles (uuid)
);

-- user_types

CREATE TABLE IF NOT EXISTS alfred_v1.user_types (
	"name" VARCHAR(50) PRIMARY KEY,
	"level" INTEGER NOT NULL,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- user_status

CREATE TABLE IF NOT EXISTS alfred_v1.user_status (
	"name" VARCHAR(50) PRIMARY KEY,
	CONSTRAINT check_empty_name CHECK ((TRIM(BOTH FROM name) <> ''::text))
);

-- users

CREATE TABLE IF NOT EXISTS alfred_v1.users (
    "uuid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	"external_uuid" VARCHAR(50) NOT NULL,
    "user_status" VARCHAR(50) NOT NULL REFERENCES alfred_v1.user_status (name),
    "community" UUID NOT NULL REFERENCES alfred_v1.communities (uuid),
    "_created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "_created_by" VARCHAR(255) NOT NULL,
	"_updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	"_updated_by" VARCHAR(255) NOT NULL,
	UNIQUE ("external_uuid", "community"),
	CONSTRAINT check_empty_created_by CHECK ((TRIM(BOTH FROM _created_by) <> ''::text)),
	CONSTRAINT check_empty_updated_by CHECK ((TRIM(BOTH FROM _updated_by) <> ''::text))
);

-- user_roles

CREATE TABLE IF NOT EXISTS alfred_v1.user_roles (
    "user" UUID NOT NULL REFERENCES alfred_v1.users (uuid),
    "role" UUID NOT NULL REFERENCES alfred_v1.roles (uuid)
);

-- user_groups

CREATE TABLE IF NOT EXISTS alfred_v1.user_groups (
    "user" UUID NOT NULL REFERENCES alfred_v1.users (uuid),
    "user_type" VARCHAR(50) NOT NULL REFERENCES alfred_v1.user_types (name)
);
